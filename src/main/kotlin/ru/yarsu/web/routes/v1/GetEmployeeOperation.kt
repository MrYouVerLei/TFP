package ru.yarsu.web.routes.v1

import ru.yarsu.Employee
import ru.yarsu.EmployeeStorage
import java.util.UUID

interface GetEmployeeOperation {
    fun get(id: UUID): Employee?
}

class GetEmployeeOperationImpl(
    private val employeeStorage: EmployeeStorage,
) : GetEmployeeOperation {
    override fun get(id: UUID) = employeeStorage.get(id)
}
