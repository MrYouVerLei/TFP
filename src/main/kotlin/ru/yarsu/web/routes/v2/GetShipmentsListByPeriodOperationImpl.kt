package ru.yarsu.web.routes.v2

import ru.yarsu.Swg
import ru.yarsu.SwgStorage
import java.time.LocalDate

interface GetShipmentsListByPeriodOperation {
    fun get(
        from: LocalDate,
        to: LocalDate,
        pagParam: PaginationParameters,
    ): List<Swg>
}

class GetShipmentsListByPeriodOperationImpl(
    private val storage: SwgStorage,
) : GetShipmentsListByPeriodOperation {
    override fun get(
        from: LocalDate,
        to: LocalDate,
        pagParam: PaginationParameters,
    ) = storage.getListSwgForPeriod(from, to, pagParam)
}
