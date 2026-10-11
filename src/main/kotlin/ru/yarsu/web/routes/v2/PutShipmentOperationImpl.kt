package ru.yarsu.web.routes.v2

import ru.yarsu.Swg
import ru.yarsu.SwgStorage
import java.util.UUID

interface PutShipmentOperation {
    fun edit(
        shipment: Swg,
        editShipment: EditShipment,
    )
}

class PutShipmentOperationImpl(
    private val swgStorage: SwgStorage,
) : PutShipmentOperation {
    override fun edit(
        shipment: Swg,
        editShipment: EditShipment,
    ) = swgStorage.edit(shipment, editShipment)
}
