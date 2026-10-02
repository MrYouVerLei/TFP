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
import ru.yarsu.jsonListSwgBadRequest
import ru.yarsu.jsonListSwgForPeriod
import ru.yarsu.jsonListSwgForPeriodBadRequest
import ru.yarsu.validPage
import ru.yarsu.validRecordsPerPage
import java.io.StringWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ListSwgForPeriodHandler(
    private val stringWriter: StringWriter,
    private val generator: JsonGenerator,
    private val getListSwgForPeriodOperation: GetListSwgForPeriodOperation,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val uriParameters: Parameters = request.uri.queries()
        val fromString: String =
            uriParameters.findSingle("from")
                ?: return Response(BAD_REQUEST)
                    .header("Content-Type", "application/json; charset=utf-8")
                    .body(jsonBadRequestNoParameter(stringWriter, generator, "from"))
        val toString: String =
            uriParameters.findSingle("to")
                ?: return Response(BAD_REQUEST)
                    .header("Content-Type", "application/json; charset=utf-8")
                    .body(jsonBadRequestNoParameter(stringWriter, generator, "to"))

        val pageString: String? = uriParameters.findSingle("page")
        val recordsPerPageString: String? = uriParameters.findSingle("records-per-page")

        val dateFrom: LocalDate
        val dateTo: LocalDate
        val page: Int
        val recordsPerPage: Int

        try {
            dateFrom = LocalDate.parse(fromString, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (e: DateTimeParseException) {
            return Response(BAD_REQUEST)
                .header("Content-Type", "application/json; charset=utf-8")
                .body(jsonListSwgForPeriodBadRequest(stringWriter, generator, "from", fromString))
        }

        try {
            dateTo = LocalDate.parse(toString, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (e: DateTimeParseException) {
            return Response(BAD_REQUEST)
                .header("Content-Type", "application/json; charset=utf-8")
                .body(jsonListSwgForPeriodBadRequest(stringWriter, generator, "to", fromString))
        }

        try {
            page = validPage(pageString)
        } catch (e: NumberFormatException) {
            return Response(BAD_REQUEST)
                .header("Content-Type", "application/json; charset=utf-8")
                .body(jsonListSwgBadRequest(stringWriter, generator, "page", pageString))
        }

        try {
            recordsPerPage = validRecordsPerPage(recordsPerPageString)
        } catch (e: NumberFormatException) {
            return Response(BAD_REQUEST)
                .header("Content-Type", "application/json; charset=utf-8")
                .body(jsonListSwgBadRequest(stringWriter, generator, "records-per-page", recordsPerPageString))
        }

        val swgList: List<Swg> = getListSwgForPeriodOperation.get(dateFrom, dateTo, page, recordsPerPage)
        return Response(OK)
            .header("Content-Type", "application/json; charset=utf-8")
            .body(jsonListSwgForPeriod(stringWriter, generator, swgList))
    }
}
