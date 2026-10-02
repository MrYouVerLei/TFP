package ru.yarsu.web.routes.v1

import ru.yarsu.Swg
import ru.yarsu.SwgStorage
import ru.yarsu.TypeSwg

interface GetListSwgByTypeOperation {
    fun get(
        type: TypeSwg,
        page: Int,
        recordsPerPage: Int,
    ): List<Swg>
}

class GetListSwgByTypeOperationImpl(
    private val storage: SwgStorage,
) : GetListSwgByTypeOperation {
    override fun get(
        type: TypeSwg,
        page: Int,
        recordsPerPage: Int,
    ) = storage.getListSwgByType(type, page, recordsPerPage)
}
