package ru.yarsu

import java.util.UUID

data class DumpTruck(
    val id: UUID,
    val model: String,
    val registration: String,
    val capacity: Double,
    val volume: Double,
) {
    fun toStringInCsvFile(): List<String> = listOf("$id", model, registration, "$capacity", "$volume")
}
