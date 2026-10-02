package ru.yarsu.web.routes.v2

import ru.yarsu.DumpTruck
import ru.yarsu.SwgStorage

interface GetTrucksListWithShipmentsCountOperation {
    fun get(trucksList: List<DumpTruck>): Map<DumpTruck, Int>
}

class GetTrucksListWithShipmentsCountOperationImpl(
    private val swgStorage: SwgStorage,
) : GetTrucksListWithShipmentsCountOperation {
    override fun get(trucksList: List<DumpTruck>) = swgStorage.getShipmentsCountForTrucks(trucksList)
}
