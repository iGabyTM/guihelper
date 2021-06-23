package me.gabytm.minecraft.guihelper.items.serialization.serializers

import me.gabytm.minecraft.guihelper.items.serialization.exceptions.ItemCanNotBeSerializedException
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.jvm.Throws

abstract class ItemSerializer {

    /**
     * Check if the item can be serialized
     * @throws ItemCanNotBeSerializedException
     */
    @Throws(ItemCanNotBeSerializedException::class)
    internal fun checkItem(item: ItemStack) {
        if (item.type == Material.AIR) {
            throw ItemCanNotBeSerializedException(item)
        }
    }

    abstract fun serialize(item: ItemStack): String

    companion object {

        const val DEFAULT = "none"

    }

}