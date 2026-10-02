package ru.yarsu.web.routes.v2

import ru.yarsu.Employee
import ru.yarsu.EmployeeStorage
import java.util.UUID

interface GetEmployeeByIdOperation {
    fun get(id: UUID): Employee?
}

class GetEmployeeByIdOperationImpl(
    private val employeeStorage: EmployeeStorage,
) : GetEmployeeByIdOperation {
    override fun get(id: UUID) = employeeStorage.get(id)
}
