package me.gabytm.minecraft.guihelper.items.heads.providers

import me.gabytm.minecraft.guihelper.items.heads.HeadIdProvider
import me.gabytm.minecraft.guihelper.utils.ServerVersion
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class PlayerNameImplementation : HeadIdProvider() {

    override fun getId(item: ItemStack): String {
        checkItem(item)
        val meta = item.itemMeta as? SkullMeta ?: return ""

        if (!meta.hasOwner()) {
            return DEFAULT
        }

        if (ServerVersion.isOlderThan(ServerVersion.V1_12)) {
            return meta.owner ?: DEFAULT
        }

        return meta.owningPlayer?.name ?: DEFAULT
    }

}