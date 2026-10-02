package ru.yarsu

import java.time.LocalDateTime
import java.util.UUID

data class Swg(
    val id: UUID,
    val title: String,
    val swg: TypeSwg,
    val measure: UnitOfMeasurement,
    val count: Double,
    val price: Double,
    val cost: Double,
    val shipmentDateTime: LocalDateTime,
    val dumpTruck: UUID,
    val washing: Boolean,
    val manager: UUID,
) {
    fun toStringInCsvFile(): List<String> =
        listOf(
            "$id",
            title,
            swg.russianName,
            measure.russianName,
            "$count",
            "$price",
            "$cost",
            "$shipmentDateTime",
            "$dumpTruck",
            "$washing",
            "$manager",
        )
}
