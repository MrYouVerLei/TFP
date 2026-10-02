package ru.yarsu

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import java.util.UUID

class DumpTruckStorage(
    tracksList: List<DumpTruck>,
) {
    private val trucksMap = mutableMapOf<UUID, DumpTruck>()

    init {
        for (item in tracksList) {
            trucksMap[item.id] = item
        }
    }

    fun writeFile(file: File) {
        csvWriter().open(file) {
            writeRow("Id,Model,Registration,Capacity,Volume")
            for (item in trucksMap) {
                writeRow(item.value.toStringInCsvFile())
            }
        }
    }

    fun getTrucksList(): List<DumpTruck> = trucksMap.values.toList()

    fun getTrucksListList(): List<List<String>> {
        val text = mutableListOf<List<String>>()
        for (item in getTrucksList()) {
            text.add(item.toStringInCsvFile())
        }
        return text
    }

    fun get(id: UUID): DumpTruck? {
        for (item in trucksMap) {
            if (item.value.id == id) {
                return item.value
            }
        }
        return null
    }

    fun delete(id: UUID) {
        for (item in trucksMap) {
            if (item.value.id == id) {
                trucksMap.remove(id)
                break
            }
        }
    }

    fun searchTrucks(
        model: String,
        registration: String,
    ): List<DumpTruck> {
        val trucks = mutableListOf<DumpTruck>()
        for (item in getTrucksList()) {
            if (item.model == model && item.registration == registration) {
                trucks.add(item)
            }
        }
        return trucks.sortedWith(compareBy<DumpTruck> { it.capacity }.thenBy { it.id })
    }

    fun create(
        model: String,
        registration: String,
        capacity: Double,
        volume: Double,
    ): UUID {
        var uuid = UUID.randomUUID()
        while (get(uuid) != null) {
            uuid = UUID.randomUUID()
        }

        val truck = DumpTruck(uuid, model, registration, capacity, volume)
        trucksMap[uuid] = truck

        return uuid
    }

    fun add(truck: DumpTruck): UUID {
        if (trucksMap.containsKey(truck.id)) {
            throw IllegalArgumentException("Самосвал с идентификатором ${truck.id} уже существует")
        } else {
            trucksMap[truck.id] = truck
            return truck.id
        }
    }
}
