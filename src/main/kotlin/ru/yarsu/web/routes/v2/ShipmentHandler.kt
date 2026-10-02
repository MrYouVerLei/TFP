package ru.yarsu.web.routes.v2

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.with
import org.http4k.lens.LensFailure
import ru.yarsu.jsonEmployeeNotFound
import ru.yarsu.jsonError
import ru.yarsu.jsonShipment
import ru.yarsu.jsonShipmentNotFound
import ru.yarsu.jsonTruckNotFound
import ru.yarsu.web.routes.v2.Lens.jsonBodyLens

class ShipmentHandler(
    private val getShipmentByIdOperation: GetShipmentByIdOperation,
    private val getTruckByIdOperation: GetTruckByIdOperation,
    private val getEmployeeByIdOperation: GetEmployeeByIdOperation,
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

        val shipment =
            getShipmentByIdOperation.get(id)
                ?: return Response(Status.NOT_FOUND).with(jsonBodyLens of jsonShipmentNotFound(id))

        val truck =
            getTruckByIdOperation.get(shipment.dumpTruck)
                ?: return Response(Status.NOT_FOUND).with(jsonBodyLens of jsonTruckNotFound(id))

        val employee =
            getEmployeeByIdOperation.get(shipment.manager)
                ?: return Response(Status.NOT_FOUND).with(jsonBodyLens of jsonEmployeeNotFound(id))

        return Response(Status.OK).with(jsonBodyLens of jsonShipment(shipment, truck, employee))
    }
}
