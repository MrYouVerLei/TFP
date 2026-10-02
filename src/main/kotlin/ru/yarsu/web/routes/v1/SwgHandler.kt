package ru.yarsu.web.routes.v1

import com.fasterxml.jackson.core.JsonGenerator
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.path
import ru.yarsu.Employee
import ru.yarsu.Swg
import ru.yarsu.jsonBadRequestNoParameter
import ru.yarsu.jsonSwg
import ru.yarsu.jsonSwgBadRequest
import ru.yarsu.jsonSwgNotFount
import ru.yarsu.jsonUserNotFount
import java.io.StringWriter
import java.util.UUID

class SwgHandler(
    private val stringWriter: StringWriter,
    private val generator: JsonGenerator,
    private val getSwgByIdOperation: GetSwgOperation,
    private val getUserByIdOperation: GetEmployeeOperation,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val id: String =
            request.path("shipment-id")
                ?: return Response(BAD_REQUEST)
                    .header("Content-Type", "application/json; charset=utf-8")
                    .body(jsonBadRequestNoParameter(stringWriter, generator, "shipment-id"))

        val uuid: UUID
        try {
            uuid = UUID.fromString(id)
        } catch (e: Exception) {
            return Response(BAD_REQUEST)
                .header("Content-Type", "application/json; charset=utf-8")
                .body(jsonSwgBadRequest(stringWriter, generator, id))
        }

        val swg: Swg =
            getSwgByIdOperation.get(uuid)
                ?: return Response(NOT_FOUND)
                    .header("Content-Type", "application/json; charset=utf-8")
                    .body(jsonSwgNotFount(stringWriter, generator, uuid))

        val employee: Employee =
            getUserByIdOperation.get(swg.manager)
                ?: return Response(NOT_FOUND)
                    .header("Content-Type", "application/json; charset=utf-8")
                    .body(jsonUserNotFount(stringWriter, generator, uuid))

        return Response(OK)
            .header("Content-Type", "application/json; charset=utf-8")
            .body(jsonSwg(stringWriter, generator, swg, employee))
    }
}
