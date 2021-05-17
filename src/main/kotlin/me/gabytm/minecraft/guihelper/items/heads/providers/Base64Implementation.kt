package me.gabytm.minecraft.guihelper.items.heads.providers

import me.gabytm.minecraft.guihelper.functions.skullTexture
import me.gabytm.minecraft.guihelper.items.heads.HeadIdProvider
import org.bukkit.inventory.ItemStack

class Base64Implementation : HeadIdProvider() {

    override fun getId(item: ItemStack): String {
        checkItem(item)
        return item.skullTexture
    }

}