package ru.yarsu.web.routes.v2

import ru.yarsu.Employee
import ru.yarsu.EmployeeStorage

interface GetEmployeesListOperation {
    fun get(): List<Employee>
}

class GetEmployeesListOperationImpl(
    private val employeeStorage: EmployeeStorage,
) : GetEmployeesListOperation {
    override fun get() = employeeStorage.getEmployeesList().sortedWith(compareBy<Employee> { it.name }.thenBy { it.id })
}
