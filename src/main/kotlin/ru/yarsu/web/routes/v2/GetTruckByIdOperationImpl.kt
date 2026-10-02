package ru.yarsu.web.routes.v2

import ru.yarsu.DumpTruck
import ru.yarsu.DumpTruckStorage
import java.util.UUID

interface GetTruckByIdOperation {
    fun get(id: UUID): DumpTruck?
}

class GetTruckByIdOperationImpl(
    private val truckStorage: DumpTruckStorage,
) : GetTruckByIdOperation {
    override fun get(id: UUID) = truckStorage.get(id)
}
