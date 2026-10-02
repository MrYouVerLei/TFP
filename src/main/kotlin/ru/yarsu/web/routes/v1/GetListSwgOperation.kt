package ru.yarsu.web.routes.v1

import ru.yarsu.Swg
import ru.yarsu.SwgStorage

interface GetListSwgOperation {
    fun get(
        page: Int,
        recordsPerPage: Int,
    ): List<Swg>
}

class GetListSwgOperationImpl(
    private val storage: SwgStorage,
) : GetListSwgOperation {
    override fun get(
        page: Int,
        recordsPerPage: Int,
    ) = storage.getSwgList(page, recordsPerPage)
}
