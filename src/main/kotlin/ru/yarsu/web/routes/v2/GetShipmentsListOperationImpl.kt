package ru.yarsu.web.routes.v2

import ru.yarsu.Swg
import ru.yarsu.SwgStorage

interface GetShipmentsListOperation {
    fun get(pagParam: PaginationParameters): List<Swg>
}

class GetShipmentsListOperationImpl(
    private val storage: SwgStorage,
) : GetShipmentsListOperation {
    override fun get(pagParam: PaginationParameters) = storage.getSwgList(pagParam)
}
