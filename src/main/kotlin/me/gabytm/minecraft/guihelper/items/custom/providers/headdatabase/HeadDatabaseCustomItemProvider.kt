package me.gabytm.minecraft.guihelper.items.custom.providers.headdatabase

import me.arcaniax.hdb.api.HeadDatabaseAPI
import me.gabytm.minecraft.guihelper.items.custom.providers.CustomItemProvider
import org.bukkit.inventory.ItemStack

class HeadDatabaseCustomItemProvider : CustomItemProvider<HeadDatabaseItem>() {

    private val api = HeadDatabaseAPI()

    override fun get(item: ItemStack): HeadDatabaseItem? {
        val id = api.getItemID(item) ?: return null
        return HeadDatabaseItem(id)
    }

}