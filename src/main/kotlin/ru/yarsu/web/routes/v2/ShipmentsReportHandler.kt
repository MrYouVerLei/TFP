package ru.yarsu.web.routes.v2

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import ru.yarsu.Swg
import ru.yarsu.jsonReport
import ru.yarsu.web.routes.v1.GetListSwgForYearOperation
import ru.yarsu.web.routes.v2.Lens.jsonBodyLens

class ShipmentsReportHandler(
    private val getListSwgForYearOperation: GetListSwgForYearOperation,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val year = Lens.yearLens(request)

        val shipments: List<Swg> = getListSwgForYearOperation.get(year)
        return Response(OK).with(jsonBodyLens of jsonReport(shipments))
    }
}
