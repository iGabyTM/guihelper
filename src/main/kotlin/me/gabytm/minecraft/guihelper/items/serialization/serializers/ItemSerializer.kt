package me.gabytm.minecraft.guihelper.items.serialization.serializers

import me.gabytm.minecraft.guihelper.items.serialization.exceptions.ItemCanNotBeSerializedException
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*

abstract class ItemSerializer {

    /**
     * Check if the item can be serialized
     * @throws ItemCanNotBeSerializedException
     */
    internal fun checkItem(item: ItemStack) {
        if (item.type == Material.AIR) {
            throw ItemCanNotBeSerializedException(item)
        }
    }

    abstract fun serialize(item: ItemStack): String

    companion object {

        const val DEFAULT = "none"

    }

    enum class Serializer(val alias: String) {

        CRATE_RELOADED("cratereloaded"),
        ESSENTIALSX("essentials"),
        VANILLA("");

        companion object {

            private val types = EnumSet.allOf(Serializer::class.java)

            fun getSerializer(string: String, default: Serializer = VANILLA): Serializer {
                return types.firstOrNull {
                    it.name.equals(string, true) ||
                            it.alias.equals(string, true) ||
                            it.name.replace("_", "").equals(string, true)
                } ?: default
            }

        }

    }

}