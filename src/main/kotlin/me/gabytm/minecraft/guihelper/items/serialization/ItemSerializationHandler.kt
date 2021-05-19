package me.gabytm.minecraft.guihelper.items.serialization

import me.gabytm.minecraft.guihelper.items.ItemsManager
import me.gabytm.minecraft.guihelper.items.serialization.serializers.EssentialsXImplementation
import me.gabytm.minecraft.guihelper.items.serialization.serializers.ItemSerializer
import me.gabytm.minecraft.guihelper.items.serialization.serializers.VanillaImplementation
import org.bukkit.inventory.ItemStack

class ItemSerializationHandler(itemsManager: ItemsManager) {

    private val serializers = mutableMapOf(
        ItemSerializer.Serializer.ESSENTIALSX to EssentialsXImplementation(itemsManager),
        ItemSerializer.Serializer.VANILLA to VanillaImplementation()
    )

    fun serialize(item: ItemStack, serializer: ItemSerializer.Serializer = ItemSerializer.Serializer.VANILLA): String {
        return serializers[serializer]?.serialize(item) ?: ItemSerializer.DEFAULT
    }

}