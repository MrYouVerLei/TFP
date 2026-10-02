package ru.yarsu.web.routes.v2

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import ru.yarsu.Employee
import ru.yarsu.jsonEmployeesList
import ru.yarsu.web.routes.v2.Lens.jsonBodyLens

class EmployeesListHandler(
    private val getEmployeesListOperation: GetEmployeesListOperation,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val employees: List<Employee> = getEmployeesListOperation.get()
        return Response(OK).with(jsonBodyLens of jsonEmployeesList(employees))
    }
}
