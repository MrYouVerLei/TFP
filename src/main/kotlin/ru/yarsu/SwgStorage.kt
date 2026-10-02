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
    private val swgMap = mutableMapOf<UUID, Swg>()

    init {
        for (item in swgList) {
            swgMap[item.id] = item
        }
    }

    fun writeFile(file: File) {
        csvWriter().open(file) {
            writeRow("Id,Title,SWG,Measure,Count,Price,Cost,ShipmentDateTime,Model,Registration,Washing,Manager")
            for (item in swgMap) {
                writeRow(item.value.toStringInCsvFile())
            }
        }
    }

    fun getSwgList(): List<Swg> = swgMap.values.toList()

    fun getSwgListList(): List<List<String>> {
        val text = mutableListOf<List<String>>()
        for (item in getSwgList()) {
            text.add(item.toStringInCsvFile())
        }
        return text
    }

    fun get(id: UUID): Swg? {
        for (item in swgMap) {
            if (item.value.id == id) {
                return item.value
            }
        }
        return null
    }

    fun add(swg: Swg): UUID {
        if (swgMap.containsKey(swg.id)) {
            throw IllegalArgumentException("Акт отгрузки ПГС с идентификатором ${swg.id} уже существует")
        } else {
            swgMap[swg.id] = swg
            return swg.id
        }
    }

    fun getSwgList(
        page: Int,
        recordsPerPage: Int,
    ): List<Swg> {
        val swgSort = swgMap.values.toList().sortedWith(compareBy<Swg> { it.shipmentDateTime }.thenBy { it.id })

        val startIndex = recordsPerPage * (page - 1)
        if (startIndex > swgSort.size) {
            return listOf()
        }
        var lastIndex = recordsPerPage * page
        if (lastIndex > swgSort.size) {
            lastIndex = swgSort.size
        }

        return swgSort.subList(startIndex, lastIndex)
    }

    fun getSwgList(pagParam: PaginationParameters): List<Swg> {
        val page = pagParam.page
        val recordsPerPage = pagParam.recordsPerPage
        val swgSort = swgMap.values.toList().sortedWith(compareBy<Swg> { it.shipmentDateTime }.thenBy { it.id })

        val startIndex = recordsPerPage * (page - 1)
        if (startIndex > swgSort.size) {
            return listOf()
        }
        var lastIndex = recordsPerPage * page
        if (lastIndex > swgSort.size) {
            lastIndex = swgSort.size
        }

        return swgSort.subList(startIndex, lastIndex)
    }

    fun getSwgListByTruckId(id: UUID): List<Swg> {
        val shipmentsActsFilter = swgMap.values.filter { it.dumpTruck == id }
        val shipmentsActsSort =
            shipmentsActsFilter.sortedWith(compareByDescending<Swg> { it.shipmentDateTime }.thenByDescending { it.id })
        return shipmentsActsSort
    }

    fun getShipmentsCountForTrucks(trucksList: List<DumpTruck>): Map<DumpTruck, Int> {
        val resultmap = mutableMapOf<DumpTruck, Int>()
        for (item in trucksList) {
            resultmap[item] = getSwgList().count { it.dumpTruck == item.id }
        }
        return resultmap
    }

    fun getListSwgByType(
        type: TypeSwg,
        page: Int,
        recordsPerPage: Int,
    ): List<Swg> {
        val shipmentsActsFilter = swgMap.values.filter { it.swg == type }
        val shipmentsActsSort =
            shipmentsActsFilter.sortedWith(compareByDescending<Swg> { it.shipmentDateTime }.thenBy { it.id })

        val startIndex = recordsPerPage * (page - 1)
        if (startIndex > shipmentsActsSort.size) {
            return listOf()
        }
        var lastIndex = recordsPerPage * page
        if (lastIndex > shipmentsActsSort.size) {
            lastIndex = shipmentsActsSort.size
        }

        return shipmentsActsSort.subList(startIndex, lastIndex)
    }

    fun getListSwgByType(
        type: TypeSwg,
        pagParam: PaginationParameters,
    ): List<Swg> {
        val page = pagParam.page
        val recordsPerPage = pagParam.recordsPerPage
        val shipmentsActsFilter = swgMap.values.filter { it.swg == type }
        val shipmentsActsSort =
            shipmentsActsFilter.sortedWith(compareByDescending<Swg> { it.shipmentDateTime }.thenBy { it.id })

        val startIndex = recordsPerPage * (page - 1)
        if (startIndex > shipmentsActsSort.size) {
            return listOf()
        }
        var lastIndex = recordsPerPage * page
        if (lastIndex > shipmentsActsSort.size) {
            lastIndex = shipmentsActsSort.size
        }

        return shipmentsActsSort.subList(startIndex, lastIndex)
    }

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

        val startIndex = recordsPerPage * (page - 1)
        if (startIndex > shipmentsActsFilter.size) {
            return listOf()
        }
        var lastIndex = recordsPerPage * page
        if (lastIndex > shipmentsActsFilter.size) {
            lastIndex = shipmentsActsFilter.size
        }

        return shipmentsActsFilter.subList(startIndex, lastIndex)
    }

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

        val startIndex = recordsPerPage * (page - 1)
        if (startIndex > shipmentsActsFilter.size) {
            return listOf()
        }
        var lastIndex = recordsPerPage * page
        if (lastIndex > shipmentsActsFilter.size) {
            lastIndex = shipmentsActsFilter.size
        }

        return shipmentsActsFilter.subList(startIndex, lastIndex)
    }

    fun getListSwgForYear(year: Int): List<Swg> = swgMap.values.filter { it.shipmentDateTime.year == year }

    fun create(newShipment: NewShipment): UUID {
        var uuid = UUID.randomUUID()
        while (get(uuid) != null) {
            uuid = UUID.randomUUID()
        }

        val swg =
            Swg(
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
        swgMap[uuid] = swg

        return uuid
    }

    fun edit(
        id: UUID,
        shipment: Swg,
        editShipment: EditShipment,
    ) {
        val shipmentDateTime: LocalDateTime
        val washing: Boolean

        if (editShipment.shipmentDateTime == null) {
            shipmentDateTime = shipment.shipmentDateTime
        } else {
            shipmentDateTime = editShipment.shipmentDateTime
        }

        if (editShipment.washing == null) {
            washing = shipment.washing
        } else {
            washing = editShipment.washing
        }

        val editSwg =
            Swg(
                id,
                editShipment.title,
                editShipment.swg,
                editShipment.measure,
                editShipment.count,
                editShipment.price,
                editShipment.cost,
                shipmentDateTime,
                editShipment.dumpTruck,
                washing,
                editShipment.manager,
            )
        swgMap[id] = editSwg
        return
    }
}
