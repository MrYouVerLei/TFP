package ru.yarsu.web.routes.v2

import ru.yarsu.DumpTruckStorage
import java.util.UUID

interface DeleteDumpTruckOperation {
    fun delete(id: UUID): Unit
}

class DeleteDumpTruckOperationImpl(
    private val truckStorage: DumpTruckStorage,
) : DeleteDumpTruckOperation {
    override fun delete(id: UUID) = truckStorage.delete(id)
}
