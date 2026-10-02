package ru.yarsu

import com.fasterxml.jackson.annotation.JsonValue

enum class TypeSwg(
    @JsonValue
    val russianName: String,
    val bulkDensity: Double,
) {
    RIVER_SAND("Песок речной", 1.5),
    QUARRY_SAND("Песок карьерный", 1.5),
    CRUSHED_GRANITE("Щебень гранитный", 1.4),
    GRAVEL_RUBBLE("Щебень гравийный", 1.43),
    SLAG_RUBBLE("Щебень шлаковый", 1.17),
    SAND_GRAVEL_MIXTURE("Песчано-гравийная смесь", 1.6),
}
