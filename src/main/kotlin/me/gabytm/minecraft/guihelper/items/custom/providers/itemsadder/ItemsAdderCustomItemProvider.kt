package me.gabytm.minecraft.guihelper.items.custom.providers.itemsadder

import dev.lone.itemsadder.api.CustomStack
import me.gabytm.minecraft.guihelper.items.custom.providers.CustomItemProvider
import org.bukkit.inventory.ItemStack

class ItemsAdderCustomItemProvider : CustomItemProvider<ItemsAdderItem>() {

    override fun get(item: ItemStack): ItemsAdderItem? {
        val customStack = CustomStack.byItemStack(item) ?: return null
        return ItemsAdderItem(customStack.id)
    }

}