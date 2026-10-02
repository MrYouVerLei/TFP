package ru.yarsu.web.routes.v1

import ru.yarsu.Swg
import ru.yarsu.SwgStorage
import java.util.UUID

interface GetSwgOperation {
    fun get(id: UUID): Swg?
}

class GetSwgOperationImpl(
    private val swgStorage: SwgStorage,
) : GetSwgOperation {
    override fun get(id: UUID) = swgStorage.get(id)
}
