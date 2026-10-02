package ru.yarsu.web.routes.v2

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import ru.yarsu.Swg
import ru.yarsu.jsonShipmentsList
import ru.yarsu.web.routes.v2.Lens.jsonBodyLens

class ShipmentsListHandler(
    private val getShipmentsListOperation: GetShipmentsListOperation,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val paginationParam: PaginationParameters = Lens.paginationParametersLens(request)
//        }
//        catch (e: LensFailure) {
//            return Response(BAD_REQUEST).with(jsonBodyLens of jsonError(e.message))
//        }

        val shipments: List<Swg> = getShipmentsListOperation.get(paginationParam)
        return Response(OK).with(jsonBodyLens of jsonShipmentsList(shipments))
    }
}
