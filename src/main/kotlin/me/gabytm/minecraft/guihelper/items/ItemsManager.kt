package me.gabytm.minecraft.guihelper.items

import me.gabytm.minecraft.guihelper.items.edit.ItemEditHandler
import me.gabytm.minecraft.guihelper.items.edit.editors.ItemEditor
import me.gabytm.minecraft.guihelper.items.heads.HeadsIdHandler
import me.gabytm.minecraft.guihelper.items.heads.providers.HeadIdProvider
import me.gabytm.minecraft.guihelper.items.serialization.ItemSerializationHandler
import me.gabytm.minecraft.guihelper.items.serialization.serializers.Serializer
import org.bukkit.inventory.ItemStack

class ItemsManager {

    private val headsIdHandler = HeadsIdHandler()
    private val itemEditHandler = ItemEditHandler()
    private val itemSerializerHandler = ItemSerializationHandler(this)

    fun getItemEditor(editor: ItemEditor.Editor): ItemEditor? {
        return itemEditHandler[editor]
    }

    fun getHeadId(item: ItemStack, provider: HeadIdProvider.Provider = HeadIdProvider.Provider.BASE_64): String {
        return headsIdHandler[item, provider]
    }

    fun serialize(item: ItemStack, serializer: Serializer = Serializer.VANILLA): String {
        return itemSerializerHandler.serialize(item, serializer)
    }

}