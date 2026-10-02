package ru.yarsu.web.routes.v2

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.FORBIDDEN
import org.http4k.core.Status.Companion.NO_CONTENT
import org.http4k.core.with
import org.http4k.lens.LensFailure
import ru.yarsu.jsonError
import ru.yarsu.jsonTruckNotFound
import ru.yarsu.web.routes.v2.Lens.jsonBodyLens

class DeleteDumpTruckHandler(
    private val getTruckByIdOperation: GetTruckByIdOperation,
    private val getShipmentsListByTruckIdOperation: GetShipmentsListByTruckIdOperation,
    private val deleteDumpTruckOperation: DeleteDumpTruckOperation,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val id =
            try {
                Lens.dumpTruckIdLens(request)
            } catch (e: LensFailure) {
                return Response(BAD_REQUEST)
                    .with(
                        jsonBodyLens of
                            jsonError(
                                "Некорректное значение переданного параметра id. " +
                                    "Ожидается UUID, но получено ${request.uri.path.replace("/v2/dump-trucks/", "")}",
                            ),
                    )
            }

        val truck =
            getTruckByIdOperation.get(id)
                ?: return Response(Status.NOT_FOUND).with(jsonBodyLens of jsonTruckNotFound(id))

        if (getShipmentsListByTruckIdOperation.get(id).isNotEmpty()) {
            return Response(FORBIDDEN)
        }

        deleteDumpTruckOperation.delete(id)
        return Response(NO_CONTENT)
    }
}
