package ru.yarsu.web.routes

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonFactoryBuilder
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import org.http4k.core.ContentType
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Method.PUT
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.OK
import org.http4k.core.UriTemplate
import org.http4k.core.hasContentToRead
import org.http4k.core.with
import org.http4k.lens.LensFailure
import org.http4k.lens.contentType
import org.http4k.routing.RoutedRequest
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import ru.yarsu.DumpTruckStorage
import ru.yarsu.EmployeeStorage
import ru.yarsu.SwgStorage
import ru.yarsu.jsonError
import ru.yarsu.web.routes.v1.GetEmployeeOperationImpl
import ru.yarsu.web.routes.v1.GetListSwgByTypeOperationImpl
import ru.yarsu.web.routes.v1.GetListSwgForPeriodOperationImpl
import ru.yarsu.web.routes.v1.GetListSwgForYearOperationImpl
import ru.yarsu.web.routes.v1.GetListSwgOperationImpl
import ru.yarsu.web.routes.v1.GetSwgOperationImpl
import ru.yarsu.web.routes.v1.ListSwgByTypeHandler
import ru.yarsu.web.routes.v1.ListSwgForPeriodHandler
import ru.yarsu.web.routes.v1.ListSwgHandler
import ru.yarsu.web.routes.v1.ReportForYearHandler
import ru.yarsu.web.routes.v1.SwgHandler
import ru.yarsu.web.routes.v2.CreateNewDumpTruckByInvoiceOperationImpl
import ru.yarsu.web.routes.v2.CreateNewShipmentOperationImpl
import ru.yarsu.web.routes.v2.CreateShipmentHandler
import ru.yarsu.web.routes.v2.DeleteDumpTruckHandler
import ru.yarsu.web.routes.v2.DeleteDumpTruckOperationImpl
import ru.yarsu.web.routes.v2.DumpTruckHandler
import ru.yarsu.web.routes.v2.EmployeesListHandler
import ru.yarsu.web.routes.v2.GetEmployeeByIdOperationImpl
import ru.yarsu.web.routes.v2.GetEmployeesListOperationImpl
import ru.yarsu.web.routes.v2.GetShipmentByIdOperationImpl
import ru.yarsu.web.routes.v2.GetShipmentsListByPeriodOperationImpl
import ru.yarsu.web.routes.v2.GetShipmentsListByTruckIdOperationImpl
import ru.yarsu.web.routes.v2.GetShipmentsListByTypeOperationImpl
import ru.yarsu.web.routes.v2.GetShipmentsListOperationImpl
import ru.yarsu.web.routes.v2.GetTruckByIdOperationImpl
import ru.yarsu.web.routes.v2.GetTrucksByModelAndRegistrationOperationImpl
import ru.yarsu.web.routes.v2.GetTrucksListWithShipmentsCountOperationImpl
import ru.yarsu.web.routes.v2.Lens.jsonBodyLens
import ru.yarsu.web.routes.v2.PutShipmentHandler
import ru.yarsu.web.routes.v2.PutShipmentOperationImpl
import ru.yarsu.web.routes.v2.RegisterInvoiceHandler
import ru.yarsu.web.routes.v2.ShipmentHandler
import ru.yarsu.web.routes.v2.ShipmentsListByPeriodHandler
import ru.yarsu.web.routes.v2.ShipmentsListByTypeHandler
import ru.yarsu.web.routes.v2.ShipmentsListHandler
import ru.yarsu.web.routes.v2.ShipmentsReportHandler
import java.io.StringWriter

fun applicationRoutes(
    swgStorage: SwgStorage,
    employeeStorage: EmployeeStorage,
    truckStorage: DumpTruckStorage,
): RoutingHttpHandler {
    val stringWriter = StringWriter()
    val factory: JsonFactory = JsonFactoryBuilder().build()
    val generator: JsonGenerator = factory.createGenerator(stringWriter)
    generator.prettyPrinter = DefaultPrettyPrinter()

    val getListSwgOperation = GetListSwgOperationImpl(swgStorage)
    val listSwgHandler = ListSwgHandler(stringWriter, generator, getListSwgOperation)

    val getSwgOperation = GetSwgOperationImpl(swgStorage)
    val getEmployeeOperation = GetEmployeeOperationImpl(employeeStorage)
    val swgHandler = SwgHandler(stringWriter, generator, getSwgOperation, getEmployeeOperation)

    val getListSwgByTypeOperation = GetListSwgByTypeOperationImpl(swgStorage)
    val listSwgByTypeHandler = ListSwgByTypeHandler(stringWriter, generator, getListSwgByTypeOperation)

    val getListSwgForPeriodOperation = GetListSwgForPeriodOperationImpl(swgStorage)
    val listSwgForPeriodHandler = ListSwgForPeriodHandler(stringWriter, generator, getListSwgForPeriodOperation)

    val getReportOperation = GetListSwgForYearOperationImpl(swgStorage)
    val reportHandler = ReportForYearHandler(stringWriter, generator, getReportOperation)

    val getShipmentsListOperation = GetShipmentsListOperationImpl(swgStorage)
    val shipmentsListHandler = ShipmentsListHandler(getShipmentsListOperation)

    val getShipmentByIdOperation = GetShipmentByIdOperationImpl(swgStorage)
    val getTruckByIdOperation = GetTruckByIdOperationImpl(truckStorage)
    val getEmployeeByIdOperation = GetEmployeeByIdOperationImpl(employeeStorage)
    val shipmentHandler = ShipmentHandler(getShipmentByIdOperation, getTruckByIdOperation, getEmployeeByIdOperation)

    val getShipmentsListByTypeOperation = GetShipmentsListByTypeOperationImpl(swgStorage)
    val shipmentsListByTypeHandler = ShipmentsListByTypeHandler(getShipmentsListByTypeOperation)

    val getShipmentsListByPeriodOperation = GetShipmentsListByPeriodOperationImpl(swgStorage)
    val shipmentsListByPeriodHandler = ShipmentsListByPeriodHandler(getShipmentsListByPeriodOperation)

    val shipmentsReportHandler = ShipmentsReportHandler(getReportOperation)

    val getShipmentsListByTruckIdOperation = GetShipmentsListByTruckIdOperationImpl(swgStorage)
    val dumpTruckHandler = DumpTruckHandler(getTruckByIdOperation, getShipmentsListByTruckIdOperation)

    val getEmployeesListOperation = GetEmployeesListOperationImpl(employeeStorage)
    val employeesListHandler = EmployeesListHandler(getEmployeesListOperation)

    val deleteDumpTruckOperation = DeleteDumpTruckOperationImpl(truckStorage)
    val deleteDumpTruckHandler = DeleteDumpTruckHandler(getTruckByIdOperation, getShipmentsListByTruckIdOperation, deleteDumpTruckOperation)

    val createNewShipmentOperation = CreateNewShipmentOperationImpl(swgStorage)
    val createShipmentHandler = CreateShipmentHandler(getTruckByIdOperation, getEmployeeByIdOperation, createNewShipmentOperation)

    val getTrucksByModelAndRegistrationOperation = GetTrucksByModelAndRegistrationOperationImpl(truckStorage)
    val getTrucksListWithShipmentsCountOperation = GetTrucksListWithShipmentsCountOperationImpl(swgStorage)
    val createNewDumpTruckByInvoiceOperation = CreateNewDumpTruckByInvoiceOperationImpl(truckStorage)
    val registerInvoiceHandler =
        RegisterInvoiceHandler(
            getEmployeeByIdOperation,
            getTrucksByModelAndRegistrationOperation,
            getTrucksListWithShipmentsCountOperation,
            createNewDumpTruckByInvoiceOperation,
            createNewShipmentOperation,
        )

    val putShipmentOperation = PutShipmentOperationImpl(swgStorage)
    val putShipmentHandler =
        PutShipmentHandler(getTruckByIdOperation, getEmployeeByIdOperation, getShipmentByIdOperation, putShipmentOperation)

    val v1Router =
        routes(
            "/list-swg" bind GET to listSwgHandler,
            "/swg/{shipment-id}" bind GET to { req: Request ->
                swgHandler.invoke(RoutedRequest(req, UriTemplate.from("/v1/swg/{shipment-id}")))
            },
            "/swg/" bind GET to swgHandler,
            "/list-swg-by-type" bind GET to listSwgByTypeHandler,
            "/list-swg-by-period" bind GET to listSwgForPeriodHandler,
            "/report" bind GET to reportHandler,
        )

    val v2RouterGet =
        routes(
            "/shipments" bind GET to shipmentsListHandler,
            "/shipments/by-type" bind GET to shipmentsListByTypeHandler,
            "/shipments/by-period" bind GET to shipmentsListByPeriodHandler,
            "/shipments/report" bind GET to shipmentsReportHandler,
            "/shipments/{shipment-id}" bind GET to shipmentHandler,
            "/dump-trucks/{dump-truck-id}" bind GET to dumpTruckHandler,
            "/employees" bind GET to employeesListHandler,
        )

    val v2RouterPost =
        routes(
            "/shipments" bind POST to createShipmentHandler,
            "/employees/{employee-id}/register-invoice" bind POST to registerInvoiceHandler,
        )

    return routes(
        "/ping" bind GET to { Response(OK) },
//        "/v1" bind GET to v1Router,
        "/v2" bind GET to v2RouterGet,
        "/v2" bind POST to v2RouterPost,
        "/v2/dump-trucks/{dump-truck-id}" bind DELETE to deleteDumpTruckHandler,
        "/v2/shipments/{shipment-id}" bind PUT to putShipmentHandler,
    ).withFilter(lensFailureFilter()).withFilter(jsonContentTypeFilter())
}

fun jsonContentTypeFilter(): Filter =
    Filter { next: HttpHandler ->
        { request ->
            val response = next(request)
            if (response.body.hasContentToRead()) {
                response.contentType(ContentType.APPLICATION_JSON)
            } else {
                response
            }
        }
    }

fun lensFailureFilter(): Filter =
    Filter { next: HttpHandler ->
        { request: Request ->
            try {
                next(request)
            } catch (lensFailure: LensFailure) {
                Response(Status.BAD_REQUEST)
                    .with(jsonBodyLens of jsonError(lensFailure))
            }
        }
    }
