package ru.yarsu.web.routes.v2

import ru.yarsu.Swg
import ru.yarsu.SwgStorage
import ru.yarsu.TypeSwg

interface GetShipmentsListByTypeOperation {
    fun get(
        type: TypeSwg,
        pagParam: PaginationParameters,
    ): List<Swg>
}

class GetShipmentsListByTypeOperationImpl(
    private val storage: SwgStorage,
) : GetShipmentsListByTypeOperation {
    override fun get(
        type: TypeSwg,
        pagParam: PaginationParameters,
    ) = storage.getListSwgByType(type, pagParam)
}
