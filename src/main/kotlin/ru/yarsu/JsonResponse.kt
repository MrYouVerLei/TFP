package ru.yarsu

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import org.http4k.format.Jackson
import org.http4k.lens.LensFailure
import org.http4k.lens.WebForm
import java.io.StringWriter
import java.time.format.DateTimeFormatter
import java.util.UUID

fun jsonListSwg(
    stringWriter: StringWriter,
    outputGenerator: JsonGenerator,
    swgList: List<Swg>,
): String {
    stringWriter.buffer.setLength(0)
    with(outputGenerator) {
        writeStartArray()
        for (item in swgList) {
            writeStartObject()
            writeFieldName("Id")
            writeString(item.id.toString())
            writeFieldName("Title")
            writeString(item.title)
            writeFieldName("Cost")
            writeNumber(item.cost)
            writeEndObject()
        }
        writeEndArray()
        flush()
    }
    return stringWriter.toString()
}

fun jsonListSwgBadRequest(
    stringWriter: StringWriter,
    outputGenerator: JsonGenerator,
    parameter: String,
    value: String?,
): String {
    stringWriter.buffer.setLength(0)
    with(outputGenerator) {
        writeStartObject()
        writeFieldName("error")
        if (parameter == "page") {
            writeString("Некорректное значение параметра $parameter. Ожидается натуральное число, но получено $value")
        } else {
            writeString("Некорректное значение параметра $parameter. Ожидается один из вариантов: 5, 10, 20, 50. Но получено $value")
        }
        writeEndObject()
        flush()
    }
    return stringWriter.toString()
}

fun jsonSwgBadRequest(
    stringWriter: StringWriter,
    outputGenerator: JsonGenerator,
    value: String?,
): String {
    stringWriter.buffer.setLength(0)
    with(outputGenerator) {
        writeStartObject()
        writeFieldName("error")
        writeString(
            "Некорректный идентификатор акта отгрузки ПГС. Для параметра shipment-id ожидается UUID, " +
                "но получено значение «$value»",
        )
        writeEndObject()
        flush()
    }
    return stringWriter.toString()
}

fun jsonSwgNotFount(
    stringWriter: StringWriter,
    outputGenerator: JsonGenerator,
    id: UUID,
): String {
    stringWriter.buffer.setLength(0)
    with(outputGenerator) {
        writeStartObject()
        writeFieldName("shipment-id")
        writeString(id.toString())
        writeFieldName("error")
        writeString("Акт отгрузки ПГС не найден")
        writeEndObject()
        flush()
    }
    return stringWriter.toString()
}

fun jsonUserNotFount(
    stringWriter: StringWriter,
    outputGenerator: JsonGenerator,
    id: UUID,
): String {
    stringWriter.buffer.setLength(0)
    with(outputGenerator) {
        writeStartObject()
        writeFieldName("user-id")
        writeString(id.toString())
        writeFieldName("error")
        writeString("Пользователь не найден")
        writeEndObject()
        flush()
    }
    return stringWriter.toString()
}

fun jsonSwg(
    stringWriter: StringWriter,
    outputGenerator: JsonGenerator,
    swg: Swg,
    employee: Employee,
): String {
    stringWriter.buffer.setLength(0)
    with(outputGenerator) {
        writeStartObject()
        writeFieldName("Id")
        writeString(swg.id.toString())
        writeFieldName("Title")
        writeString(swg.title)
        writeFieldName("SWG")
        writeString(swg.swg.russianName)
        writeFieldName("Measure")
        writeString(swg.measure.russianName)
        writeFieldName("Count")
        writeNumber(swg.count)
        writeFieldName("Price")
        writeNumber(swg.price)
        writeFieldName("Cost")
        writeNumber(swg.cost)
        writeFieldName("ShipmentDateTime")
        writeString(swg.shipmentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
        writeFieldName("Washing")
        writeBoolean(swg.washing)
        writeFieldName("ManagerEmail")
        writeString(employee.email)
        writeEndObject()
        flush()
    }
    return stringWriter.toString()
}

fun jsonBadRequestNoParameter(
    stringWriter: StringWriter,
    outputGenerator: JsonGenerator,
    parameter: String,
): String {
    stringWriter.buffer.setLength(0)
    with(outputGenerator) {
        writeStartObject()
        writeFieldName("error")
        writeString("Отсутствует обязательный параметр $parameter")
        writeEndObject()
        flush()
    }
    return stringWriter.toString()
}

fun jsonListSwgByTypeBadRequest(
    stringWriter: StringWriter,
    outputGenerator: JsonGenerator,
    value: String?,
): String {
    stringWriter.buffer.setLength(0)
    with(outputGenerator) {
        writeStartObject()
        writeFieldName("error")
        writeString("Некорректный тип ПГС. Для параметра by-swg-type ожидается тип ПГС, но получено значение «$value»")
        writeEndObject()
        flush()
    }
    return stringWriter.toString()
}

fun jsonListSwgForPeriodBadRequest(
    stringWriter: StringWriter,
    outputGenerator: JsonGenerator,
    parameter: String,
    value: String?,
): String {
    stringWriter.buffer.setLength(0)
    with(outputGenerator) {
        writeStartObject()
        writeFieldName("error")
        writeString("Некорректный тип ПГС. Для параметра $parameter ожидается дата, но получено значение «$value»")
        writeEndObject()
        flush()
    }
    return stringWriter.toString()
}

fun jsonReportForYearBadRequest(
    stringWriter: StringWriter,
    outputGenerator: JsonGenerator,
    value: String?,
): String {
    stringWriter.buffer.setLength(0)
    with(outputGenerator) {
        writeStartObject()
        writeFieldName("error")
        writeString("Некорректная дата. Для параметра year ожидается натуральное число, но получено значение «$value»")
        writeEndObject()
        flush()
    }
    return stringWriter.toString()
}

fun jsonListSwgByType(
    stringWriter: StringWriter,
    outputGenerator: JsonGenerator,
    swgList: List<Swg>,
): String {
    stringWriter.buffer.setLength(0)
    with(outputGenerator) {
        writeStartArray()
        for (item in swgList) {
            writeStartObject()
            writeFieldName("Id")
            writeString(item.id.toString())
            writeFieldName("Title")
            writeString(item.title)
            writeFieldName("Cost")
            writeNumber(item.cost)
            writeEndObject()
        }
        writeEndArray()
        flush()
    }
    return stringWriter.toString()
}

fun jsonListSwgForPeriod(
    stringWriter: StringWriter,
    outputGenerator: JsonGenerator,
    swgList: List<Swg>,
): String {
    stringWriter.buffer.setLength(0)
    with(outputGenerator) {
        writeStartArray()
        for (item in swgList) {
            writeStartObject()
            writeFieldName("Id")
            writeString(item.id.toString())
            writeFieldName("Title")
            writeString(item.title)
            writeFieldName("ShipmentDate")
            writeString(item.shipmentDateTime.toLocalDate().toString())
            writeFieldName("Cost")
            writeNumber(item.cost)
            writeEndObject()
        }
        writeEndArray()
        flush()
    }
    return stringWriter.toString()
}

fun jsonReportForYear(
    stringWriter: StringWriter,
    outputGenerator: JsonGenerator,
    swgList: List<Swg>,
): String {
    stringWriter.buffer.setLength(0)
    val monthInRussian: List<String> =
        listOf(
            "Январь",
            "Февраль",
            "Март",
            "Апрель",
            "Май",
            "Июнь",
            "Июль",
            "Август",
            "Сентябрь",
            "Октябрь",
            "Ноябрь",
            "Декабрь",
        )

    with(outputGenerator) {
        writeStartArray()
        for (index in monthInRussian.indices) {
            if (swgList.any { it.shipmentDateTime.monthValue == index + 1 }) {
                val actsFilterByMonth = swgList.filter { it.shipmentDateTime.monthValue == index + 1 }
                writeStartObject()
                writeFieldName("month")
                writeNumber(index + 1)
                writeFieldName("monthTitle")
                writeString(monthInRussian[index])
                writeFieldName("count")
                writeNumber(actsFilterByMonth.count())
                writeFieldName("cost")
                writeNumber(actsFilterByMonth.sumOf { it.cost })
                writeFieldName("weight")
                writeNumber(actsFilterByMonth.filter { it.measure == UnitOfMeasurement.TON }.sumOf { it.count })
                writeFieldName("volume")
                writeNumber(actsFilterByMonth.filter { it.measure == UnitOfMeasurement.CUBIC_METER }.sumOf { it.count })
                writeEndObject()
            }
        }
        writeEndArray()
        flush()
    }
    return stringWriter.toString()
}

fun jsonShipmentsList(swgList: List<Swg>): JsonNode {
    val json = Jackson
    val responseJson = mutableListOf<JsonNode>()
    for (item in swgList) {
        val swgJson: JsonNode =
            json {
                obj(
                    "Id" to string(item.id.toString()),
                    "Title" to string(item.title),
                    "Cost" to number(item.cost),
                )
            }
        responseJson.add(swgJson)
    }
    return json.array(responseJson)
}

fun jsonTrucksList(trucksWithShipmentsCount: Map<DumpTruck, Int>): JsonNode {
    val json = Jackson
    val responseJson = mutableListOf<JsonNode>()
    for (item in trucksWithShipmentsCount) {
        val swgJson: JsonNode =
            json {
                obj(
                    "Id" to string(item.key.id.toString()),
                    "Capacity" to number(item.key.capacity),
                    "Volume" to number(item.key.volume),
                    "ShipmentsCount" to number(item.value),
                )
            }
        responseJson.add(swgJson)
    }
    return json.array(responseJson)
}

fun jsonShipmentsListByPeriod(swgList: List<Swg>): JsonNode {
    val json = Jackson
    val responseJson = mutableListOf<JsonNode>()
    for (item in swgList) {
        val swgJson: JsonNode =
            json {
                obj(
                    "Id" to string(item.id.toString()),
                    "Title" to string(item.title),
                    "ShipmentDate" to string(item.shipmentDateTime.toLocalDate().toString()),
                    "Cost" to number(item.cost),
                )
            }
        responseJson.add(swgJson)
    }
    return json.array(responseJson)
}

fun jsonError(message: String?): JsonNode {
    val json = Jackson
    val errorJson: JsonNode =
        json {
            obj(
                "Error" to string(message.toString()),
            )
        }
    return errorJson
}

fun jsonError(lensFailure: LensFailure): JsonNode {
    val invalidErrorsMap =
        mapOf(
            "page" to "Некорректное значение параметра page. Ожидается натуральное число",
            "records-per-page" to "Некорректное значение параметра records-per-page. Ожидается один из вариантов: 5, 10, 20, 50",
            "year" to "Некорректная дата. Для параметра year ожидается натуральное число",
        )

    if (lensFailure.failures.isEmpty()) {
        return jsonError(lensFailure.message)
    }

    val json = Jackson
    val errorJson: JsonNode
    when (
        lensFailure.failures
            .first()
            .type.name
    ) {
        "Missing" -> {
            errorJson =
                json {
                    obj(
                        "Error" to string("Отсутствует обязательный параметр ${lensFailure.failures.first().meta.name}"),
                    )
                }
        }
        "Invalid" -> {
            errorJson =
                if (invalidErrorsMap[
                        lensFailure.failures
                            .first()
                            .meta.name,
                    ] != null
                ) {
                    json {
                        obj(
                            "Error" to
                                string(
                                    invalidErrorsMap[
                                        lensFailure.failures
                                            .first()
                                            .meta.name,
                                    ].toString(),
                                ),
                        )
                    }
                } else {
                    json {
                        obj(
                            "Error" to string(lensFailure.message.toString()),
                        )
                    }
                }
        }
        else -> {
            errorJson =
                json {
                    obj(
                        "Error" to string(lensFailure.message.toString()),
                    )
                }
        }
    }
    return errorJson
}

fun jsonId(id: UUID): JsonNode {
    val json = Jackson
    val errorJson: JsonNode =
        json {
            obj(
                "Id" to string(id.toString()),
            )
        }
    return errorJson
}

fun jsonValueError(
    value: String?,
    message: String?,
): JsonNode {
    val json = Jackson
    val errorJson: JsonNode =
        json {
            obj(
                "Value" to string(value.toString()),
                "Error" to string(message.toString()),
            )
        }
    return errorJson
}

fun jsonShipmentNotFound(id: UUID): JsonNode {
    val json = Jackson
    val errorJson: JsonNode =
        json {
            obj(
                "ShipmentId" to string(id.toString()),
                "Error" to string("Акт отгрузки ПГС не найден"),
            )
        }
    return errorJson
}

fun jsonTruckNotFound(id: UUID): JsonNode {
    val json = Jackson
    val errorJson: JsonNode =
        json {
            obj(
                "DumpTruckId" to string(id.toString()),
                "Error" to string("Самосвал не найден"),
            )
        }
    return errorJson
}

fun jsonEmployeeNotFound(id: UUID): JsonNode {
    val json = Jackson
    val errorJson: JsonNode =
        json {
            obj(
                "EmployeeId" to string(id.toString()),
                "Error" to string("Работник не найден"),
            )
        }
    return errorJson
}

fun jsonRegisterInvoice(
    shipmentId: UUID,
    truckId: UUID,
): JsonNode {
    val json = Jackson
    val errorJson: JsonNode =
        json {
            obj(
                "ShipmentId" to string(shipmentId.toString()),
                "DumpTruckId" to string(truckId.toString()),
            )
        }
    return errorJson
}

fun jsonShipment(
    shipment: Swg,
    truck: DumpTruck,
    employee: Employee,
): JsonNode {
    val json = Jackson
    val shipmentJson: JsonNode =
        json {
            obj(
                "Id" to string(shipment.id.toString()),
                "Title" to string(shipment.title),
                "SWG" to string(shipment.swg.russianName),
                "Measure" to string(shipment.measure.russianName),
                "Count" to number(shipment.count),
                "Price" to number(shipment.price),
                "Cost" to number(shipment.cost),
                "ShipmentDateTime" to string(shipment.shipmentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                "DumpTruckRegistration" to string(truck.registration),
                "Washing" to boolean(shipment.washing),
                "ManagerEmail" to string(employee.email),
            )
        }
    return shipmentJson
}

fun jsonReport(swgList: List<Swg>): JsonNode {
    val monthInRussian: List<String> =
        listOf(
            "Январь",
            "Февраль",
            "Март",
            "Апрель",
            "Май",
            "Июнь",
            "Июль",
            "Август",
            "Сентябрь",
            "Октябрь",
            "Ноябрь",
            "Декабрь",
        )

    val json = Jackson
    val responseJson = mutableListOf<JsonNode>()

    for (index in monthInRussian.indices) {
        if (swgList.any { it.shipmentDateTime.monthValue == index + 1 }) {
            val actsFilterByMonth = swgList.filter { it.shipmentDateTime.monthValue == index + 1 }
            val reportJson: JsonNode =
                json {
                    obj(
                        "Month" to number(index + 1),
                        "MonthTitle" to string(monthInRussian[index]),
                        "Count" to number(actsFilterByMonth.count()),
                        "Cost" to number(actsFilterByMonth.sumOf { it.cost }),
                        "Weight" to
                            number(
                                actsFilterByMonth
                                    .filter { it.measure == UnitOfMeasurement.TON }
                                    .sumOf { it.count },
                            ),
                        "Volume" to
                            number(
                                actsFilterByMonth
                                    .filter { it.measure == UnitOfMeasurement.CUBIC_METER }
                                    .sumOf { it.count },
                            ),
                    )
                }
            responseJson.add(reportJson)
        }
    }
    return json.array(responseJson)
}

fun jsonShipmentsForDumpTruck(swgList: List<Swg>): JsonNode {
    val json = Jackson
    val swgListJson = mutableListOf<JsonNode>()
    for (item in swgList) {
        val swgJson: JsonNode =
            json {
                obj(
                    "Id" to string(item.id.toString()),
                    "Title" to string(item.title),
                    "ShipmentDateTime" to string(item.shipmentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                )
            }
        swgListJson.add(swgJson)
    }
    return json.array(swgListJson)
}

fun jsonDumpTruck(
    truck: DumpTruck,
    swgList: List<Swg>,
): JsonNode {
    val json = Jackson
    val swgListJson = jsonShipmentsForDumpTruck(swgList)
    val truckJson: JsonNode =
        json {
            obj(
                "Id" to string(truck.id.toString()),
                "Model" to string(truck.model),
                "Registration" to string(truck.registration),
                "Capacity" to number(truck.capacity),
                "Volume" to number(truck.volume),
                "Shipments" to swgListJson,
            )
        }
    return truckJson
}

fun jsonEmployeesList(employees: List<Employee>): JsonNode {
    val json = Jackson
    val responseJson = mutableListOf<JsonNode>()
    for (item in employees) {
        val swgJson: JsonNode =
            json {
                obj(
                    "Id" to string(item.id.toString()),
                    "Name" to string(item.name),
                    "Position" to string(item.position),
                    "RegistrationDateTime" to string(item.registrationDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                    "Email" to string(item.email),
                )
            }
        responseJson.add(swgJson)
    }
    return json.array(responseJson)
}

fun jsonErrorsForm(form: WebForm): JsonNode {
    val invalidErrorsMap =
        mapOf(
            "InvoiceTitle" to "Ожидается строка",
            "InvoiceType" to "Ожидается тип ПГС из списка",
            "InvoiceWeight" to "Ожидается положительное вещественное число",
            "InvoicePrice" to "Ожидается положительное вещественное число",
            "InvoiceCost" to "Ожидается положительное вещественное число",
            "DumpTruckModel" to "Ожидается строка",
            "DumpTruckRegistration" to "Ожидается строка",
        )
    val factory = JsonNodeFactory.instance
    val node = factory.objectNode()
    for (item in form.errors) {
        if (item.type.name == "Missing") {
            if (form.fields[item.meta.name] == null) {
                node.putObject(item.meta.name).apply {
                    putNull("Value")
                    put("Error", "Отсутствует поле ${item.meta.name}")
                }
            } else {
                node.putObject(item.meta.name).apply {
                    put("Value", form.fields[item.meta.name]?.get(0))
                    put("Error", invalidErrorsMap[item.meta.name])
                }
            }
        } else if (item.type.name == "Invalid") {
            node.putObject(item.meta.name).apply {
                put("Value", form.fields[item.meta.name]?.get(0))
                put("Error", invalidErrorsMap[item.meta.name])
            }
        }
    }
    return node
}
