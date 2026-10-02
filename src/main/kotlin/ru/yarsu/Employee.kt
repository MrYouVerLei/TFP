package ru.yarsu

import java.time.LocalDateTime
import java.util.UUID

data class Employee(
    val id: UUID,
    val name: String,
    val position: String,
    val registrationDateTime: LocalDateTime,
    val email: String,
) {
    fun toStringInCsvFile(): List<String> = listOf("$id", name, position, "$registrationDateTime", email)
}
