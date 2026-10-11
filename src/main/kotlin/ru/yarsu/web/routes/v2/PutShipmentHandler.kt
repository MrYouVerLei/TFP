package ru.yarsu.web.routes.v2

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.FORBIDDEN
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.NO_CONTENT
import org.http4k.core.with
import org.http4k.lens.LensFailure
import ru.yarsu.UnitOfMeasurement
import ru.yarsu.checkShipmentJson
import ru.yarsu.jsonError
import ru.yarsu.jsonShipmentNotFound
import ru.yarsu.jsonValueError
import ru.yarsu.web.routes.v2.Lens.editShipmentLens
import ru.yarsu.web.routes.v2.Lens.jsonBodyLens
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.util.UUID

class PutShipmentHandler(
    private val getTruckByIdOperation: GetTruckByIdOperation,
    private val getEmployeeByIdOperation: GetEmployeeByIdOperation,
    private val getShipmentByIdOperation: GetShipmentByIdOperation,
    private val putShipmentOperation: PutShipmentOperation,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val id =
            try {
                Lens.shipmentIdLens(request)
            } catch (e: LensFailure) {
                return Response(BAD_REQUEST)
                    .with(
                        jsonBodyLens of
                            jsonError(
                                "Некорректное значение переданного параметра id. " +
                                    "Ожидается UUID, но получено ${request.uri.path.replace("/v2/shipments/", "")}",
                            ),
                    )
            }

        val body = request.bodyString()
        val mapper = jacksonObjectMapper()

        val jsonObject =
            try {
                mapper.readTree(body)
            } catch (e: Exception) {
                return Response(BAD_REQUEST).with(
                    jsonBodyLens of
                        jsonValueError(
                            body,
                            "Тело запроса не является JSON-документом",
                        ),
                )
            }

        val result = checkShipmentJson(jsonObject, getTruckByIdOperation, getEmployeeByIdOperation)
        if (!result.isEmpty) {
            return Response(BAD_REQUEST).with(
                jsonBodyLens of result,
            )
        }

        val editShipment = editShipmentLens(request)

        val dumpTrackId: UUID = editShipment.dumpTruck
        val truck = getTruckByIdOperation.get(dumpTrackId)
        if (truck != null) {
            if ((
                    jsonObject["Measure"].textValue() == UnitOfMeasurement.CUBIC_METER.russianName &&
                        jsonObject["Count"].numberValue().toDouble() > truck.volume
                ) ||
                (
                    jsonObject["Measure"].textValue() == UnitOfMeasurement.TON.russianName &&
                        jsonObject["Count"].numberValue().toDouble() > truck.capacity
                )
            ) {
                return Response(FORBIDDEN).with(
                    jsonBodyLens of jsonError("Объём отгрузки превышает объём кузова самосвала"),
                )
            }
        }

        val employeeId: UUID = editShipment.manager
        val employee = getEmployeeByIdOperation.get(employeeId)
        if (employee != null) {
            if (LocalDateTime.parse(jsonObject["ShipmentDateTime"].textValue(), ISO_LOCAL_DATE_TIME) < employee.registrationDateTime) {
                return Response(FORBIDDEN).with(
                    jsonBodyLens of jsonError("Отгрузка произведена до регистрации работника"),
                )
            }
        }

        val shipment =
            getShipmentByIdOperation.get(id)
                ?: return Response(NOT_FOUND).with(jsonBodyLens of jsonShipmentNotFound(id))

        putShipmentOperation.edit(shipment, editShipment)

        return Response(NO_CONTENT)
    }
}
