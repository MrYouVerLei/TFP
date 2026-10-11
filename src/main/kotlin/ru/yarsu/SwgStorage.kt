package ru.yarsu

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import ru.yarsu.web.routes.v2.EditShipment
import ru.yarsu.web.routes.v2.NewShipment
import ru.yarsu.web.routes.v2.PaginationParameters
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class SwgStorage(
    swgList: List<Swg>,
) {
    // A
    private val swgMap = mutableMapOf<UUID, Swg>()

    // A
    init {
        for (item in swgList) {
            swgMap[item.id] = item
        }
    }

    // A
    fun writeFile(file: File) {
        csvWriter().open(file) {
            writeRow("Id,Title,SWG,Measure,Count,Price,Cost,ShipmentDateTime,Model,Registration,Washing,Manager")
            for (item in swgMap) {
                writeRow(item.value.toStringInCsvFile())
            }
        }
    }

    // A
    fun getSwgList(): List<Swg> = swgMap.values.toList()

    // A
    fun getSwgListList(): List<List<String>> {
        val text = mutableListOf<List<String>>()
        for (item in getSwgList()) {
            text.add(item.toStringInCsvFile())
        }
        return text
    }

    // A
    fun get(id: UUID): Swg? {
        for (item in swgMap) {
            if (item.value.id == id) {
                return item.value
            }
        }
        return null
    }

    // A
    fun add(swg: Swg): UUID {
        if (swgMap.containsKey(swg.id)) {
            throw IllegalArgumentException("Акт отгрузки ПГС с идентификатором ${swg.id} уже существует")
        } else {
            swgMap[swg.id] = swg
            return swg.id
        }
    }

    // A
    fun getSwgList(
        page: Int,
        recordsPerPage: Int,
    ): List<Swg> {
        val swgSort = swgMap.values.toList().sortedWith(compareBy<Swg> { it.shipmentDateTime }.thenBy { it.id })
        val shipmentsActsPagination = calculatePagination(swgSort, page, recordsPerPage)

        return shipmentsActsPagination
    }

    // A
    fun getSwgList(pagParam: PaginationParameters): List<Swg> {
        val page = pagParam.page
        val recordsPerPage = pagParam.recordsPerPage
        val swgSort = swgMap.values.toList().sortedWith(compareBy<Swg> { it.shipmentDateTime }.thenBy { it.id })
        val shipmentsActsPagination = calculatePagination(swgSort, page, recordsPerPage)

        return shipmentsActsPagination
    }

    // A
    fun getSwgListByTruckId(id: UUID): List<Swg> {
        val shipmentsActsFilter = swgMap.values.filter { it.dumpTruck == id }
        val shipmentsActsSort =
            shipmentsActsFilter.sortedWith(compareByDescending<Swg> { it.shipmentDateTime }.thenByDescending { it.id })
        return shipmentsActsSort
    }

    // A
    fun getShipmentsCountForTrucks(trucksList: List<DumpTruck>): Map<DumpTruck, Int> {
        val resultmap = mutableMapOf<DumpTruck, Int>()
        for (item in trucksList) {
            resultmap[item] = getSwgList().count { it.dumpTruck == item.id }
        }
        return resultmap
    }

    // A
    fun getListSwgByType(
        type: TypeSwg,
        page: Int,
        recordsPerPage: Int,
    ): List<Swg> {
        val shipmentsActsFilter = swgMap.values.filter { it.swg == type }
        val shipmentsActsSort =
            shipmentsActsFilter.sortedWith(compareByDescending<Swg> { it.shipmentDateTime }.thenBy { it.id })
        val shipmentsActsPagination = calculatePagination(shipmentsActsSort, page, recordsPerPage)

        return shipmentsActsPagination
    }

    // C
    private fun <T> calculatePagination(list: List<T>, page: Int, recordsPerPage: Int,): List<T> {
        val startIndex = recordsPerPage * (page - 1)
        if (startIndex > list.size) {
            return listOf()
        }
        var lastIndex = recordsPerPage * page
        if (lastIndex > list.size) {
            lastIndex = list.size
        }

        return list.subList(startIndex, lastIndex).toList()
    }

    // A
    fun getListSwgByType(
        type: TypeSwg,
        pagParam: PaginationParameters,
    ): List<Swg> {
        val page = pagParam.page
        val recordsPerPage = pagParam.recordsPerPage
        val shipmentsActsFilter = swgMap.values.filter { it.swg == type }
        val shipmentsActsSort =
            shipmentsActsFilter.sortedWith(compareByDescending<Swg> { it.shipmentDateTime }.thenBy { it.id })
        val shipmentsActsPagination = calculatePagination(shipmentsActsSort, page, recordsPerPage)

        return shipmentsActsPagination
    }

    // A
    fun getListSwgForPeriod(
        from: LocalDate,
        to: LocalDate,
        page: Int,
        recordsPerPage: Int,
    ): List<Swg> {
        val shipmentsActsSort = swgMap.values.sortedWith(compareBy<Swg> { it.shipmentDateTime }.thenBy { it.id })
        val shipmentsActsFilter =
            shipmentsActsSort.filter {
                (
                    it.shipmentDateTime.toLocalDate().isAfter(from) ||
                        it.shipmentDateTime
                            .toLocalDate()
                            .isEqual(from)
                ) &&
                    (
                        it.shipmentDateTime.toLocalDate().isBefore(to) ||
                            it.shipmentDateTime
                                .toLocalDate()
                                .isEqual(to)
                    )
            }
        val shipmentsActsPagination = calculatePagination(shipmentsActsFilter, page, recordsPerPage)

        return shipmentsActsPagination
    }

    // A
    fun getListSwgForPeriod(
        from: LocalDate,
        to: LocalDate,
        pagParam: PaginationParameters,
    ): List<Swg> {
        val page = pagParam.page
        val recordsPerPage = pagParam.recordsPerPage
        val shipmentsActsSort = swgMap.values.sortedWith(compareBy<Swg> { it.shipmentDateTime }.thenBy { it.id })
        val shipmentsActsFilter =
            shipmentsActsSort.filter {
                (
                    it.shipmentDateTime.toLocalDate().isAfter(from) ||
                        it.shipmentDateTime
                            .toLocalDate()
                            .isEqual(from)
                ) &&
                    (
                        it.shipmentDateTime.toLocalDate().isBefore(to) ||
                            it.shipmentDateTime
                                .toLocalDate()
                                .isEqual(to)
                    )
            }
        val shipmentsActsPagination = calculatePagination(shipmentsActsFilter, page, recordsPerPage)

        return shipmentsActsPagination
    }

    // A
    fun getListSwgForYear(year: Int): List<Swg> = swgMap.values.filter { it.shipmentDateTime.year == year }

    // A
    fun create(newShipment: NewShipment): UUID {
        val uuid = generateId(swgMap.keys)
        val swg = makeShipment(newShipment, uuid)
        val newMap = putShipment(swgMap, swg)
        swgMap.clear()
        swgMap.putAll(newMap)

        return uuid
    }

    // C
    private fun putShipment(map: Map<UUID, Swg>, shipment: Swg): Map<UUID, Swg> {
        val newMap = map.toMutableMap()
        newMap[shipment.id] = shipment
        return newMap
    }

    // A
    private fun generateId(ids: Set<UUID>): UUID {
        var uuid = UUID.randomUUID()
        while (ids.contains(uuid)) {
            uuid = UUID.randomUUID()
        }

        return uuid
    }

    // C
    private fun makeShipment(newShipment: NewShipment, uuid: UUID): Swg {
        return Swg(
            uuid,
            newShipment.title,
            newShipment.swg,
            newShipment.measure,
            newShipment.count,
            newShipment.price,
            newShipment.cost,
            newShipment.shipmentDateTime,
            newShipment.dumpTruck,
            newShipment.washing,
            newShipment.manager,
        )
    }

    // A
    fun edit(
        shipment: Swg,
        editShipment: EditShipment,
    ) {
        val editSwg = updateSwgFields(shipment, editShipment)
        val newMap = putShipment(swgMap, editSwg)
        swgMap.clear()
        swgMap.putAll(newMap)

        return
    }

    // C
    private fun updateSwgFields(shipment: Swg, editShipment: EditShipment): Swg {
        return shipment.copy(
            title = editShipment.title,
            swg = editShipment.swg,
            measure = editShipment.measure,
            count = editShipment.count,
            price = editShipment.price,
            cost = editShipment.cost,
            shipmentDateTime = editShipment.shipmentDateTime ?: shipment.shipmentDateTime,
            dumpTruck = editShipment.dumpTruck,
            washing = editShipment.washing ?: shipment.washing,
            manager = editShipment.manager
        )
    }
}
