package ru.yarsu.web.routes.v2

import ru.yarsu.DumpTruckStorage
import java.util.UUID

interface CreateNewDumpTruckByInvoiceOperation {
    fun create(
        model: String,
        registration: String,
        capacity: Double,
        volume: Double,
    ): UUID
}

class CreateNewDumpTruckByInvoiceOperationImpl(
    private val truckStorage: DumpTruckStorage,
) : CreateNewDumpTruckByInvoiceOperation {
    override fun create(
        model: String,
        registration: String,
        capacity: Double,
        volume: Double,
    ) = truckStorage.create(model, registration, capacity, volume)
}
