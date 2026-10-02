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
import ru.yarsu.jsonListSwg
import ru.yarsu.jsonListSwgBadRequest
import ru.yarsu.validPage
import ru.yarsu.validRecordsPerPage
import java.io.StringWriter

class ListSwgHandler(
    private val stringWriter: StringWriter,
    private val generator: JsonGenerator,
    private val getListSwgOperation: GetListSwgOperation,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val uriParameters: Parameters = request.uri.queries()
        val pageString: String? = uriParameters.findSingle("page")
        val recordsPerPageString: String? = uriParameters.findSingle("records-per-page")

        val page: Int
        val recordsPerPage: Int

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

        val swgList: List<Swg> = getListSwgOperation.get(page, recordsPerPage)
        return Response(OK)
            .header("Content-Type", "application/json; charset=utf-8")
            .body(jsonListSwg(stringWriter, generator, swgList))
    }
}
