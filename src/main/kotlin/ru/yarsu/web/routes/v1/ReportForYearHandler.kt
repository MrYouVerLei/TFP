package ru.yarsu.web.routes.v1

import com.fasterxml.jackson.core.JsonGenerator
import org.http4k.core.HttpHandler
import org.http4k.core.Parameters
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.core.findSingle
import org.http4k.core.queries
import ru.yarsu.Swg
import ru.yarsu.jsonBadRequestNoParameter
import ru.yarsu.jsonReportForYear
import ru.yarsu.jsonReportForYearBadRequest
import ru.yarsu.validYear
import java.io.StringWriter

class ReportForYearHandler(
    private val stringWriter: StringWriter,
    private val generator: JsonGenerator,
    private val getListSwgForYearOperation: GetListSwgForYearOperation,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val uriParameters: Parameters = request.uri.queries()
        val yearString: String =
            uriParameters.findSingle("year")
                ?: return Response(BAD_REQUEST)
                    .header("Content-Type", "application/json; charset=utf-8")
                    .body(jsonBadRequestNoParameter(stringWriter, generator, "year"))

        val year: Int
        try {
            year = validYear(yearString)
        } catch (e: NumberFormatException) {
            return Response(BAD_REQUEST)
                .header("Content-Type", "application/json; charset=utf-8")
                .body(jsonReportForYearBadRequest(stringWriter, generator, yearString))
        }

        val swgList: List<Swg> = getListSwgForYearOperation.get(year)
        return Response(OK)
            .header("Content-Type", "application/json; charset=utf-8")
            .body(jsonReportForYear(stringWriter, generator, swgList))
    }
}
