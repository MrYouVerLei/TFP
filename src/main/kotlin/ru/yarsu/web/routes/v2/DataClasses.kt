package ru.yarsu.web.routes.v2

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import ru.yarsu.TypeSwg
import ru.yarsu.UnitOfMeasurement
import java.time.LocalDateTime
import java.util.UUID

data class PaginationParameters(
    val page: Int,
    val recordsPerPage: Int,
)

@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy::class)
data class NewShipment(
    val title: String,
    @JsonProperty("SWG")
    val swg: TypeSwg,
    val measure: UnitOfMeasurement,
    val count: Double,
    val price: Double,
    val cost: Double,
    val shipmentDateTime: LocalDateTime = LocalDateTime.now(),
    val dumpTruck: UUID,
    val washing: Boolean = false,
    val manager: UUID,
)

@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy::class)
data class EditShipment(
    val title: String,
    @JsonProperty("SWG")
    val swg: TypeSwg,
    val measure: UnitOfMeasurement,
    val count: Double,
    val price: Double,
    val cost: Double,
    val shipmentDateTime: LocalDateTime? = null,
    val dumpTruck: UUID,
    val washing: Boolean? = null,
    val manager: UUID,
)
