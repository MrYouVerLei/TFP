package ru.yarsu.web.routes.v2

import ru.yarsu.SwgStorage
import java.util.UUID

interface CreateNewShipmentOperation {
    fun create(newShipment: NewShipment): UUID
}

class CreateNewShipmentOperationImpl(
    private val swgStorage: SwgStorage,
) : CreateNewShipmentOperation {
    override fun create(newShipment: NewShipment) = swgStorage.create(newShipment)
}
