package ru.yarsu.web.routes.v2

import org.http4k.core.Body
import org.http4k.format.Jackson.auto
import org.http4k.lens.FormField
import org.http4k.lens.LensFailure
import org.http4k.lens.Path
import org.http4k.lens.Query
import org.http4k.lens.Validator
import org.http4k.lens.composite
import org.http4k.lens.double
import org.http4k.lens.int
import org.http4k.lens.map
import org.http4k.lens.nonBlankString
import org.http4k.lens.string
import org.http4k.lens.uuid
import org.http4k.lens.webForm
import ru.yarsu.TypeSwg
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object Lens {
    val shipmentIdLens = Path.uuid().of("shipment-id")
    val dumpTruckIdLens = Path.uuid().of("dump-truck-id")
    val employeeIdLens = Path.uuid().of("employee-id")
    val newShipmentLens = Body.auto<NewShipment>().toLens()
    val editShipmentLens = Body.auto<EditShipment>().toLens()

    val titleField = FormField.nonBlankString().required("InvoiceTitle")
    val typeField =
        FormField
            .nonBlankString()
            .map { string ->
                TypeSwg.entries.find { it.russianName == string } ?: throw IllegalArgumentException()
            }.required("InvoiceType")
    val weightField =
        FormField
            .double()
            .map { number ->
                if (number <= 0) {
                    throw IllegalArgumentException()
                }
                number
            }.required("InvoiceWeight")
    val priceField =
        FormField
            .double()
            .map { number ->
                if (number <= 0) {
                    throw IllegalArgumentException()
                }
                number
            }.required("InvoicePrice")
    val costField =
        FormField
            .double()
            .map { number ->
                if (number <= 0) {
                    throw IllegalArgumentException()
                }
                number
            }.optional("InvoiceCost")
    val dumpTruckModelField = FormField.nonBlankString().required("DumpTruckModel")
    val dumpTruckRegistrationField = FormField.nonBlankString().required("DumpTruckRegistration")
    val formLens =
        Body
            .webForm(
                Validator.Feedback,
                titleField,
                typeField,
                weightField,
                priceField,
                costField,
                dumpTruckModelField,
                dumpTruckRegistrationField,
            ).toLens()

    val bySwgTypeLens =
        Query
            .string()
            .map(
                { type ->
                    TypeSwg.entries.find { it.russianName == type }
                        ?: throw LensFailure(
                            message = "Некорректный тип ПГС. Для параметра by-swg-type ожидается тип ПГС, но получено значение «$type»",
                        )
                },
                { type -> type.russianName },
            ).required("by-swg-type")

    val fromDateLens =
        Query
            .string()
            .map(
                { date ->
                    try {
                        LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
                    } catch (e: DateTimeParseException) {
                        throw LensFailure(message = "Некорректная дата. Для параметра from ожидается дата, но получено значение «$date»")
                    }
                },
                { date ->
                    date.toString()
                },
            ).required("from")

    val toDateLens =
        Query
            .string()
            .map(
                { date ->
                    try {
                        LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
                    } catch (e: DateTimeParseException) {
                        throw LensFailure(message = "Некорректная дата. Для параметра to ожидается дата, но получено значение «$date»")
                    }
                },
                { date ->
                    date.toString()
                },
            ).required("to")

    val yearLens =
        Query
            .int()
            .map(
                { number ->
                    if (number < 1) {
                        throw LensFailure(
                            message = "Некорректная дата. Для параметра year ожидается натуральное число, но получено значение «$number»",
                        )
                    }
                    number
                },
                { number -> number },
            ).required("year")

    val jsonBodyLens = Body.auto<Any>().toLens()

    val pageLens =
        Query
            .int()
            .map(
                { number ->
                    if (number < 1) {
                        throw LensFailure(
                            message = "Параметр page должен быть положительным",
                        )
                    }
                    number
                },
                { number -> number },
            ).defaulted("page", 1)

    val recordsPerPageLens =
        Query
            .int()
            .map(
                { number ->
                    if (!listOf(5, 10, 20, 50).contains(number)) {
                        throw LensFailure(
                            message = "Для параметра records-per-page ожидается один из вариантов: 5, 10, 20, 50",
                        )
                    }
                    number
                },
                { number -> number },
            ).defaulted("records-per-page", 10)

    val paginationParametersLens =
        Query.composite { request ->
            PaginationParameters(
                pageLens(request),
                recordsPerPageLens(request),
            )
        }
}
