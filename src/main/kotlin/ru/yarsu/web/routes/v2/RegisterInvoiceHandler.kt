package ru.yarsu.web.routes.v2

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.CONFLICT
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.FORBIDDEN
import org.http4k.core.with
import org.http4k.lens.LensFailure
import ru.yarsu.UnitOfMeasurement
import ru.yarsu.jsonEmployeeNotFound
import ru.yarsu.jsonError
import ru.yarsu.jsonErrorsForm
import ru.yarsu.jsonRegisterInvoice
import ru.yarsu.jsonTrucksList
import ru.yarsu.web.routes.v2.Lens.costField
import ru.yarsu.web.routes.v2.Lens.dumpTruckModelField
import ru.yarsu.web.routes.v2.Lens.dumpTruckRegistrationField
import ru.yarsu.web.routes.v2.Lens.formLens
import ru.yarsu.web.routes.v2.Lens.jsonBodyLens
import ru.yarsu.web.routes.v2.Lens.priceField
import ru.yarsu.web.routes.v2.Lens.titleField
import ru.yarsu.web.routes.v2.Lens.typeField
import ru.yarsu.web.routes.v2.Lens.weightField
import java.util.UUID

class RegisterInvoiceHandler(
    private val getEmployeeByIdOperation: GetEmployeeByIdOperation,
    private val getTrucksByModelAndRegistrationOperation: GetTrucksByModelAndRegistrationOperation,
    private val getTrucksListWithShipmentsCountOperation: GetTrucksListWithShipmentsCountOperation,
    private val createNewDumpTruckByInvoiceOperation: CreateNewDumpTruckByInvoiceOperation,
    private val createNewShipmentOperation: CreateNewShipmentOperation,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val employeeId =
            try {
                Lens.employeeIdLens(request)
            } catch (e: LensFailure) {
                return Response(BAD_REQUEST)
                    .with(
                        jsonBodyLens of jsonError("Некорректное значение переданного параметра id"),
                    )
            }

        val form = formLens(request)
        if (form.errors.isNotEmpty()) {
            return Response(BAD_REQUEST).with(jsonBodyLens of jsonErrorsForm(form))
        }

        val title = titleField(form)
        val type = typeField(form)
        val weight = weightField(form)
        val price = priceField(form)
        val cost: Double = costField(form) ?: (weight * price)
        val dumpTruckModel = dumpTruckModelField(form)
        val dumpTruckRegistration = dumpTruckRegistrationField(form)

        val employee =
            getEmployeeByIdOperation.get(employeeId)
                ?: return Response(Status.NOT_FOUND).with(jsonBodyLens of jsonEmployeeNotFound(employeeId))

        val trucks = getTrucksByModelAndRegistrationOperation.get(dumpTruckModel, dumpTruckRegistration)
        val truckId: UUID
        if (trucks.size > 1) {
            return Response(CONFLICT)
                .with(jsonBodyLens of jsonTrucksList(getTrucksListWithShipmentsCountOperation.get(trucks)))
        } else if (trucks.size == 1) {
            val truck = trucks.first()
            if (weight > truck.capacity) {
                return Response(FORBIDDEN)
                    .with(jsonBodyLens of jsonError("Вес отгрузки превышает грузоподъёмность кузова самосвала"))
            }
            truckId = truck.id
        } else {
            truckId = createNewDumpTruckByInvoiceOperation.create(dumpTruckModel, dumpTruckRegistration, weight, weight / type.bulkDensity)
        }

        val shipmentId =
            createNewShipmentOperation
                .create(NewShipment(title, type, UnitOfMeasurement.TON, weight, price, cost, dumpTruck = truckId, manager = employeeId))

        return Response(CREATED).with(jsonBodyLens of jsonRegisterInvoice(shipmentId, truckId))
    }
}
