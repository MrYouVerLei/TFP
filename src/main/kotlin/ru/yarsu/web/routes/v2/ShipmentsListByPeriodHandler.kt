package ru.yarsu.web.routes.v2

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import ru.yarsu.Swg
import ru.yarsu.jsonShipmentsListByPeriod
import ru.yarsu.web.routes.v2.Lens.jsonBodyLens

class ShipmentsListByPeriodHandler(
    private val getShipmentsListByPeriodOperation: GetShipmentsListByPeriodOperation,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val from = Lens.fromDateLens(request)
        val to = Lens.toDateLens(request)
        val paginationParam: PaginationParameters = Lens.paginationParametersLens(request)

        val shipments: List<Swg> = getShipmentsListByPeriodOperation.get(from, to, paginationParam)
        return Response(OK).with(jsonBodyLens of jsonShipmentsListByPeriod(shipments))
    }
}
