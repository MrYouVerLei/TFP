package ru.yarsu.web.routes.v2

import ru.yarsu.Swg
import ru.yarsu.SwgStorage
import java.util.UUID

interface GetShipmentByIdOperation {
    fun get(id: UUID): Swg?
}

class GetShipmentByIdOperationImpl(
    private val swgStorage: SwgStorage,
) : GetShipmentByIdOperation {
    override fun get(id: UUID) = swgStorage.get(id)
}
