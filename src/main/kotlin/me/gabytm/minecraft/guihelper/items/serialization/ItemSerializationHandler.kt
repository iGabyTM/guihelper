package me.gabytm.minecraft.guihelper.items.serialization

import me.gabytm.minecraft.guihelper.items.ItemsManager
import me.gabytm.minecraft.guihelper.items.serialization.serializers.*
import org.bukkit.inventory.ItemStack

class ItemSerializationHandler(itemsManager: ItemsManager) {

    private val serializers = mutableMapOf(
        Serializer.CRATE_RELOADED to CrateReloadedImplementation(),
        Serializer.ESSENTIALSX to EssentialsXImplementation(itemsManager),
        Serializer.VANILLA to VanillaImplementation()
    )

    fun serialize(item: ItemStack, serializer: Serializer = Serializer.VANILLA): String {
        return serializers[serializer]?.serialize(item) ?: ItemSerializer.DEFAULT
    }

}