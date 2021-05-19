package me.gabytm.minecraft.guihelper.items

import me.gabytm.minecraft.guihelper.items.heads.providers.HeadIdProvider
import me.gabytm.minecraft.guihelper.items.heads.HeadsIdHandler
import me.gabytm.minecraft.guihelper.items.serialization.ItemSerializationHandler
import me.gabytm.minecraft.guihelper.items.serialization.serializers.ItemSerializer
import org.bukkit.inventory.ItemStack

class ItemsManager {

    private val headsIdHandler = HeadsIdHandler()
    private val itemSerializerHandler = ItemSerializationHandler(this)

    fun getHeadId(item: ItemStack, provider: HeadIdProvider.Provider = HeadIdProvider.Provider.BASE_64): String {
        return headsIdHandler[item, provider]
    }

    fun serialize(
        item: ItemStack,
        serializer: ItemSerializer.Serializer = ItemSerializer.Serializer.VANILLA
    ): String {
        return itemSerializerHandler.serialize(item, serializer)
    }

}