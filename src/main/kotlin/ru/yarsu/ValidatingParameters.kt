package ru.yarsu

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import ru.yarsu.web.routes.v2.GetEmployeeByIdOperation
import ru.yarsu.web.routes.v2.GetTruckByIdOperation
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.util.UUID
import java.util.UUID.fromString

fun validPage(pageString: String?): Int {
    var page = 1
    if (pageString != null) {
        page = pageString.toInt()
        if (page < 1) {
            throw NumberFormatException()
        }
    }
    return page
}

fun validRecordsPerPage(recordsPerPageString: String?): Int {
    var recordsPerPage = 10
    if (recordsPerPageString != null) {
        recordsPerPage = recordsPerPageString.toInt()
        val acceptableValues: List<Int> = listOf(5, 10, 20, 50)
        if (!acceptableValues.contains(recordsPerPage)) {
            throw NumberFormatException()
        }
    }
    return recordsPerPage
}

fun validYear(yearString: String): Int {
    val year = yearString.toInt()
    if (year <= 0) {
        throw NumberFormatException()
    }
    return year
}

fun validTypeSwg(typeString: String): TypeSwg? {
    var type: TypeSwg? = null
    for (item in TypeSwg.entries) {
        if (item.russianName == typeString) {
            type = item
            break
        }
    }
    return type
}

fun checkShipmentJson(
    jsonObject: JsonNode,
    getTruckByIdOperation: GetTruckByIdOperation,
    getEmployeeByIdOperation: GetEmployeeByIdOperation,
): ObjectNode {
    val factory = JsonNodeFactory.instance
    val node = factory.objectNode()

    if (jsonObject["Title"] == null) {
        node.putObject("Title").apply {
            putNull("Value")
            put("Error", "Отсутствует поле Title")
        }
    } else if (!jsonObject["Title"].isTextual) {
        node.putObject("Title").apply {
            replace("Value", jsonObject["Title"])
            put("Error", "Ожидается строка")
        }
    }

    if (jsonObject["SWG"] == null) {
        node.putObject("SWG").apply {
            putNull("Value")
            put("Error", "Отсутствует поле SWG")
        }
    } else if (TypeSwg.entries.find { it.russianName == jsonObject["SWG"].textValue() } == null) {
        node.putObject("SWG").apply {
            replace("Value", jsonObject["SWG"])
            put("Error", "Ожидается тип ПГС из списка")
        }
    }

    if (jsonObject["Measure"] == null) {
        node.putObject("Measure").apply {
            putNull("Value")
            put("Error", "Отсутствует поле Measure")
        }
    } else if (UnitOfMeasurement.entries.find { it.russianName == jsonObject["Measure"].textValue() } == null) {
        node.putObject("Measure").apply {
            replace("Value", jsonObject["Measure"])
            put("Error", "Ожидается единица измерения из списка")
        }
    }

    if (jsonObject["Count"] == null) {
        node.putObject("Count").apply {
            putNull("Value")
            put("Error", "Отсутствует поле Count")
        }
    } else if (!jsonObject["Count"].isNumber || jsonObject["Count"].numberValue().toDouble() <= 0) {
        node.putObject("Count").apply {
            replace("Value", jsonObject["Count"])
            put("Error", "Ожидается положительное вещественное число")
        }
    }

    if (jsonObject["Price"] == null) {
        node.putObject("Price").apply {
            putNull("Value")
            put("Error", "Отсутствует поле Price")
        }
    } else if (!jsonObject["Price"].isNumber || jsonObject["Price"].numberValue().toDouble() <= 0) {
        node.putObject("Price").apply {
            replace("Value", jsonObject["Price"])
            put("Error", "Ожидается положительное вещественное число")
        }
    }

    if (jsonObject["Cost"] == null) {
        node.putObject("Cost").apply {
            putNull("Value")
            put("Error", "Отсутствует поле Cost")
        }
    } else if (!jsonObject["Cost"].isNumber || jsonObject["Cost"].numberValue().toDouble() <= 0) {
        node.putObject("Cost").apply {
            replace("Value", jsonObject["Cost"])
            put("Error", "Ожидается положительное вещественное число")
        }
    }

    if (jsonObject["ShipmentDateTime"] != null) {
        try {
            LocalDateTime.parse(jsonObject["ShipmentDateTime"].textValue(), ISO_LOCAL_DATE_TIME)
        } catch (e: Exception) {
            node.putObject("ShipmentDateTime").apply {
                replace("Value", jsonObject["ShipmentDateTime"])
                put("Error", "Ожидается дата и время")
            }
        }
    }

    if (jsonObject["Washing"] != null && !jsonObject["Washing"].isBoolean) {
        node.putObject("Washing").apply {
            replace("Value", jsonObject["Washing"])
            put("Error", "Ожидается логический тип")
        }
    }

    if (jsonObject["DumpTruck"] == null) {
        node.putObject("DumpTruck").apply {
            putNull("Value")
            put("Error", "Отсутствует поле DumpTruck")
        }
    } else {
        try {
            val id: UUID = fromString(jsonObject["DumpTruck"].textValue())
            if (getTruckByIdOperation.get(id) == null) {
                node.putObject("DumpTruck").apply {
                    replace("Value", jsonObject["DumpTruck"])
                    put("Error", "Самосвал не найден")
                }
            }
        } catch (e: Exception) {
            node.putObject("DumpTruck").apply {
                replace("Value", jsonObject["DumpTruck"])
                put("Error", "Ожидается корректный UUID")
            }
        }
    }

    if (jsonObject["Manager"] == null) {
        node.putObject("Manager").apply {
            putNull("Value")
            put("Error", "Отсутствует поле Manager")
        }
    } else {
        try {
            val id: UUID = fromString(jsonObject["Manager"].textValue())
            if (getEmployeeByIdOperation.get(id) == null) {
                node.putObject("Manager").apply {
                    replace("Value", jsonObject["Manager"])
                    put("Error", "Работник не найден")
                }
            }
        } catch (e: Exception) {
            node.putObject("Manager").apply {
                replace("Value", jsonObject["Manager"])
                put("Error", "Ожидается корректный UUID")
            }
        }
    }

    return node
}
