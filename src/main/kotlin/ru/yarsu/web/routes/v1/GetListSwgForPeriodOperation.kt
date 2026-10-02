package ru.yarsu.web.routes.v1

import ru.yarsu.Swg
import ru.yarsu.SwgStorage
import java.time.LocalDate

interface GetListSwgForPeriodOperation {
    fun get(
        from: LocalDate,
        to: LocalDate,
        page: Int,
        recordsPerPage: Int,
    ): List<Swg>
}

class GetListSwgForPeriodOperationImpl(
    private val storage: SwgStorage,
) : GetListSwgForPeriodOperation {
    override fun get(
        from: LocalDate,
        to: LocalDate,
        page: Int,
        recordsPerPage: Int,
    ) = storage.getListSwgForPeriod(from, to, page, recordsPerPage)
}
