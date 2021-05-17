package me.gabytm.minecraft.guihelper.items.heads

import org.bukkit.inventory.ItemStack

class ItemIsNotPlayerHeadException(private val item: ItemStack) : Exception() {

    override val message: String
        get() = "Item ${item.type} is not a player head"

}