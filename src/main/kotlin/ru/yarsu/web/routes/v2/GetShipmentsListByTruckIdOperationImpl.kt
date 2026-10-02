package ru.yarsu.web.routes.v2

import ru.yarsu.Swg
import ru.yarsu.SwgStorage
import java.util.UUID

interface GetShipmentsListByTruckIdOperation {
    fun get(id: UUID): List<Swg>
}

class GetShipmentsListByTruckIdOperationImpl(
    private val storage: SwgStorage,
) : GetShipmentsListByTruckIdOperation {
    override fun get(id: UUID) = storage.getSwgListByTruckId(id)
}
