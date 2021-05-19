package me.gabytm.minecraft.guihelper.items.serialization.serializers

import de.tr7zw.changeme.nbtapi.NBTItem
import org.bukkit.inventory.ItemStack

class VanillaImplementation : ItemSerializer() {

    override fun serialize(item: ItemStack): String {
        checkItem(item)
        return NBTItem(item).toString()
    }

}