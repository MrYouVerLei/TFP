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
import ru.yarsu.TypeSwg
import ru.yarsu.jsonBadRequestNoParameter
import ru.yarsu.jsonListSwgBadRequest
import ru.yarsu.jsonListSwgByType
import ru.yarsu.jsonListSwgByTypeBadRequest
import ru.yarsu.validPage
import ru.yarsu.validRecordsPerPage
import ru.yarsu.validTypeSwg
import java.io.StringWriter

class ListSwgByTypeHandler(
    private val stringWriter: StringWriter,
    private val generator: JsonGenerator,
    private val getListSwgByTypeOperation: GetListSwgByTypeOperation,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val uriParameters: Parameters = request.uri.queries()
        val typeString: String =
            uriParameters.findSingle("by-swg-type")
                ?: return Response(BAD_REQUEST)
                    .header("Content-Type", "application/json; charset=utf-8")
                    .body(jsonBadRequestNoParameter(stringWriter, generator, "by-swg-type"))

        val pageString: String? = uriParameters.findSingle("page")
        val recordsPerPageString: String? = uriParameters.findSingle("records-per-page")

        val type: TypeSwg? = validTypeSwg(typeString)
        val page: Int
        val recordsPerPage: Int

        if (type == null) {
            return Response(BAD_REQUEST)
                .header("Content-Type", "application/json; charset=utf-8")
                .body(jsonListSwgByTypeBadRequest(stringWriter, generator, typeString))
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

        val swgList: List<Swg> = getListSwgByTypeOperation.get(type, page, recordsPerPage)
        return Response(OK)
            .header("Content-Type", "application/json; charset=utf-8")
            .body(jsonListSwgByType(stringWriter, generator, swgList))
    }
}
