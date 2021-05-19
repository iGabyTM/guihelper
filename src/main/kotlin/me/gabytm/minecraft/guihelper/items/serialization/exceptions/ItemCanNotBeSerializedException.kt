package me.gabytm.minecraft.guihelper.items.serialization.exceptions

import org.bukkit.inventory.ItemStack

class ItemCanNotBeSerializedException(item: ItemStack) : Exception() {

    override val message: String = "${item.type} can not be serialized"

}