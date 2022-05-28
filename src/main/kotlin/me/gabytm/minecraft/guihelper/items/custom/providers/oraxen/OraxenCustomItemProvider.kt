package me.gabytm.minecraft.guihelper.items.custom.providers.oraxen

import io.th0rgal.oraxen.items.OraxenItems
import me.gabytm.minecraft.guihelper.items.custom.providers.CustomItemProvider
import org.bukkit.inventory.ItemStack

class OraxenCustomItemProvider : CustomItemProvider<OraxenItem>() {

    override fun get(item: ItemStack): OraxenItem? {
        return OraxenItem(OraxenItems.getIdByItem(item) ?: return null)
    }

}