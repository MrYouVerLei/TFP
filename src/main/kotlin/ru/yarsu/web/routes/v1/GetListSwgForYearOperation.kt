package ru.yarsu.web.routes.v1

import ru.yarsu.Swg
import ru.yarsu.SwgStorage

interface GetListSwgForYearOperation {
    fun get(year: Int): List<Swg>
}

class GetListSwgForYearOperationImpl(
    private val storage: SwgStorage,
) : GetListSwgForYearOperation {
    override fun get(year: Int) = storage.getListSwgForYear(year)
}
