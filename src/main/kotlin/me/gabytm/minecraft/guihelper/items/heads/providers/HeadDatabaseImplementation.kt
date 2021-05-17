package me.gabytm.minecraft.guihelper.items.heads.providers

import me.arcaniax.hdb.api.HeadDatabaseAPI
import me.gabytm.minecraft.guihelper.items.heads.HeadIdProvider
import org.bukkit.inventory.ItemStack

class HeadDatabaseImplementation : HeadIdProvider() {

    private val api = HeadDatabaseAPI()

    override fun getId(item: ItemStack): String {
        checkItem(item)
        return api.getItemID(item) ?: DEFAULT
    }

}