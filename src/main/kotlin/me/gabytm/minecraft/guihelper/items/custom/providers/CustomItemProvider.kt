package me.gabytm.minecraft.guihelper.items.custom.providers

import org.bukkit.inventory.ItemStack

abstract class CustomItemProvider<T> {

    abstract fun get(item: ItemStack): T?

}