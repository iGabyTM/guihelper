package me.gabytm.minecraft.guihelper.items.heads.exceptions

import org.bukkit.inventory.ItemStack

class ItemIsNotPlayerHeadException(item: ItemStack) : Exception() {

    override val message: String = "Item ${item.type} is not a player head"

}