package ru.yarsu

import com.beust.jcommander.JCommander
import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.util.UUID.fromString
import kotlin.concurrent.thread
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val httpServer = HTTPServerCommand()

    val commander: JCommander =
        JCommander
            .newBuilder()
            .addObject(httpServer)
            .build()

    try {
        commander.parse(*args)
        val command: String? = commander.parsedCommand

        if (command == null) {
            val swgStorage = checkingFileAndSwgStorageCreation(httpServer.swgFile)
            val employeeStorage = checkingFileAndEmployeesStorageCreation(httpServer.usersFile)
            val truckStorage = checkingFileAndTrucksStorageCreation(httpServer.dumpTrucksFile)

            val myThread =
                thread(start = false) {
                    writeSwgFile(swgStorage, httpServer.swgFile)
                    writeEmployeesFile(employeeStorage, httpServer.usersFile)
                    writeTrucksFile(truckStorage, httpServer.dumpTrucksFile)
                }
            Runtime.getRuntime().addShutdownHook(myThread)

            httpServer.startServer(swgStorage, employeeStorage, truckStorage)
            println("Сервер запущен")
        }
    } catch (e: Exception) {
        println(e.message)
        commander.usage()
        exitProcess(1)
    }
}

fun checkingFileAndEmployeesStorageCreation(fileString: String?): EmployeeStorage {
    if (fileString == null) {
        exitProcess(1)
    }
    var file: String = fileString
    file = file.removeSurrounding("\"")
    if (File(file).isFile) {
        return EmployeeStorage(readEmployeesFile(File(file)))
    } else {
        throw IllegalArgumentException("Error: file not found «$fileString»")
    }
}

fun checkingFileAndTrucksStorageCreation(fileString: String?): DumpTruckStorage {
    if (fileString == null) {
        exitProcess(1)
    }
    var file: String = fileString
    file = file.removeSurrounding("\"")
    if (File(file).isFile) {
        return DumpTruckStorage(readTrucksFile(File(file)))
    } else {
        throw IllegalArgumentException("Error: file not found «$fileString»")
    }
}

fun readEmployeesFile(file: File): List<Employee> {
    val employeesList = mutableListOf<Employee>()
    val text = CsvReader().readAllWithHeader(file)
    for (item in text) {
        val id = fromString(item["Id"])
        val name = item["Name"] ?: continue
        val position = item["Position"] ?: continue
        val regDate = item["RegistrationDateTime"]?.let { LocalDateTime.parse(it, ISO_LOCAL_DATE_TIME) } ?: continue
        val email = item["Email"] ?: continue
        employeesList.add(Employee(id, name, position, regDate, email))
    }
    return employeesList
}

fun readTrucksFile(file: File): List<DumpTruck> {
    val trucksList = mutableListOf<DumpTruck>()
    val text = CsvReader().readAllWithHeader(file)
    for (item in text) {
        val id = fromString(item["Id"])
        val model = item["Model"] ?: continue
        val registration = item["Registration"] ?: continue
        val capacity = item["Capacity"]?.toDouble() ?: continue
        val volume = item["Volume"]?.toDouble() ?: continue
        trucksList.add(DumpTruck(id, model, registration, capacity, volume))
    }
    return trucksList
}

fun readSwgFile(file: File): List<Swg> {
    val swgList = mutableListOf<Swg>()
    val text = CsvReader().readAllWithHeader(file)
    for (item in text) {
        val id = fromString(item["Id"])
        val title = item["Title"] ?: continue
        val swg: TypeSwg = TypeSwg.entries.find { it.russianName == item["SWG"] } ?: continue
        val measure: UnitOfMeasurement =
            UnitOfMeasurement.entries.find { it.russianName == item["Measure"] } ?: continue
        val count = item["Count"]?.toDouble() ?: continue
        val price = item["Price"]?.toDouble() ?: continue
        val cost = item["Cost"]?.toDouble() ?: continue
        val shipmentDateTime =
            item["ShipmentDateTime"]?.let { LocalDateTime.parse(it.trim(), ISO_LOCAL_DATE_TIME) } ?: continue
        val dumpTrack = fromString(item["DumpTruck"])
        val washing = item["Washing"].toBoolean()
        val manager = fromString(item["Manager"])

        if (count > 0 && price > 0 && cost > 0) {
            swgList.add(
                Swg(
                    id,
                    title,
                    swg,
                    measure,
                    count,
                    price,
                    cost,
                    shipmentDateTime,
                    dumpTrack,
                    washing,
                    manager,
                ),
            )
        }
    }
    return swgList
}

fun checkingFileAndSwgStorageCreation(fileString: String?): SwgStorage {
    if (fileString == null) {
        exitProcess(1)
    }
    var file: String = fileString
    file = file.removeSurrounding("\"")
    if (File(file).isFile) {
        return SwgStorage(readSwgFile(File(file)))
    } else {
        throw IllegalArgumentException("Error: file not found «$fileString»")
    }
}

fun writeEmployeesFile(
    storage: EmployeeStorage,
    file: String?,
) {
    val fileObject = File(file.toString())
    if (!fileObject.exists()) {
        fileObject.createNewFile()
    }

    val text = mutableListOf(mutableListOf<String>())
    text.add(mutableListOf("Id", "Name", "Position", "RegistrationDateTime", "Email"))
    val users = storage.getEmployeesListList()
    for (item in users) {
        text.add(item.toMutableList())
    }
    CsvWriter().writeAll(text, fileObject)
}

fun writeSwgFile(
    storage: SwgStorage,
    file: String?,
) {
    val fileObject = File(file.toString())
    if (!fileObject.exists()) {
        fileObject.createNewFile()
    }

    val text = mutableListOf(mutableListOf<String>())
    text.add(
        mutableListOf(
            "Id",
            "Title",
            "SWG",
            "Measure",
            "Count",
            "Price",
            "Cost",
            "ShipmentDateTime",
            "DumpTruck",
            "Washing",
            "Manager",
        ),
    )
    val acts = storage.getSwgListList()
    for (item in acts) {
        text.add(item.toMutableList())
    }
    CsvWriter().writeAll(text, fileObject)
}

fun writeTrucksFile(
    storage: DumpTruckStorage,
    file: String?,
) {
    val fileObject = File(file.toString())
    if (!fileObject.exists()) {
        fileObject.createNewFile()
    }

    val text = mutableListOf(mutableListOf<String>())
    text.add(mutableListOf("Id", "Model", "Registration", "Capacity", "Volume"))
    val trucks = storage.getTrucksListList()
    for (item in trucks) {
        text.add(item.toMutableList())
    }
    CsvWriter().writeAll(text, fileObject)
}
