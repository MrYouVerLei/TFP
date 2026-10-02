package ru.yarsu.web.routes.v2

import ru.yarsu.Swg
import ru.yarsu.SwgStorage
import java.util.UUID

interface PutShipmentOperation {
    fun edit(
        id: UUID,
        shipment: Swg,
        editShipment: EditShipment,
    )
}

class PutShipmentOperationImpl(
    private val swgStorage: SwgStorage,
) : PutShipmentOperation {
    override fun edit(
        id: UUID,
        shipment: Swg,
        editShipment: EditShipment,
    ) = swgStorage.edit(id, shipment, editShipment)
}
