package ru.yarsu

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import java.util.UUID

class EmployeeStorage(
    employeesList: List<Employee>,
) {
    private val employeesMap = mutableMapOf<UUID, Employee>()

    init {
        for (item in employeesList) {
            employeesMap[item.id] = item
        }
    }

    fun writeFile(file: File) {
        csvWriter().open(file) {
            writeRow("Id,Name,Position,RegistrationDateTime,Email")
            for (item in employeesMap) {
                writeRow(item.value.toStringInCsvFile())
            }
        }
    }

    fun getEmployeesList(): List<Employee> = employeesMap.values.toList()

    fun getEmployeesListList(): List<List<String>> {
        val text = mutableListOf<List<String>>()
        for (item in getEmployeesList()) {
            text.add(item.toStringInCsvFile())
        }
        return text
    }

    fun get(id: UUID): Employee? {
        for (item in employeesMap) {
            if (item.value.id == id) {
                return item.value
            }
        }
        return null
    }

    fun add(employee: Employee): UUID {
        if (employeesMap.containsKey(employee.id)) {
            throw IllegalArgumentException("Работник с идентификатором ${employee.id} уже существует")
        } else {
            employeesMap[employee.id] = employee
            return employee.id
        }
    }
}
