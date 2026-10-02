package ru.yarsu.web.routes.v2

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import ru.yarsu.Swg
import ru.yarsu.jsonShipmentsList
import ru.yarsu.web.routes.v2.Lens.jsonBodyLens

class ShipmentsListByTypeHandler(
    private val getShipmentsListByTypeOperation: GetShipmentsListByTypeOperation,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val type = Lens.bySwgTypeLens(request)

        val paginationParam: PaginationParameters = Lens.paginationParametersLens(request)

        val shipments: List<Swg> = getShipmentsListByTypeOperation.get(type, paginationParam)
        return Response(OK).with(jsonBodyLens of jsonShipmentsList(shipments))
    }
}
