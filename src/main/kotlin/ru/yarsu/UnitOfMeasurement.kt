package ru.yarsu

import com.fasterxml.jackson.annotation.JsonValue

enum class UnitOfMeasurement(
    @JsonValue
    val russianName: String,
) {
    CUBIC_METER("м3"),
    TON("т"),
}
