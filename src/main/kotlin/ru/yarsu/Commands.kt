package ru.yarsu

import com.beust.jcommander.IParameterValidator
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import com.beust.jcommander.Parameters
import org.http4k.server.Netty
import org.http4k.server.asServer
import ru.yarsu.web.routes.applicationRoutes

@Parameters(separators = "=")
class HTTPServerCommand {
    @Parameter(names = ["--port"], required = true, validateWith = [CheckPort::class])
    var port: Int? = null

    @Parameter(names = ["--swg-file"], required = true)
    var swgFile: String? = null

    @Parameter(names = ["--dump-trucks-file"], required = true)
    var dumpTrucksFile: String? = null

    @Parameter(names = ["--users-file"], required = true)
    var usersFile: String? = null

    fun startServer(
        swgStorage: SwgStorage,
        employeeStorage: EmployeeStorage,
        trackStorage: DumpTruckStorage,
    ) {
        val router = applicationRoutes(swgStorage, employeeStorage, trackStorage)
        val portValue = port
        if (portValue != null) {
            router.asServer(Netty(portValue)).start()
        }
    }
}

class CheckPort : IParameterValidator {
    override fun validate(
        name: String,
        value: String,
    ) {
        try {
            val n = value.toInt()
            if (n !in 1024..65536) {
                throw ParameterException("Error: invalid port $value")
            }
        } catch (e: NumberFormatException) {
            throw ParameterException("Error: number was expected for port, but received $value")
        }
    }
}
