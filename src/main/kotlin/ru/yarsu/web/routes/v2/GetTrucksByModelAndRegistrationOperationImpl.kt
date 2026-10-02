package ru.yarsu.web.routes.v2

import ru.yarsu.DumpTruck
import ru.yarsu.DumpTruckStorage

interface GetTrucksByModelAndRegistrationOperation {
    fun get(
        model: String,
        registration: String,
    ): List<DumpTruck>
}

class GetTrucksByModelAndRegistrationOperationImpl(
    private val truckStorage: DumpTruckStorage,
) : GetTrucksByModelAndRegistrationOperation {
    override fun get(
        model: String,
        registration: String,
    ) = truckStorage.searchTrucks(model, registration)
}
