package me.gabytm.minecraft.guihelper.items.heads

import me.gabytm.minecraft.guihelper.functions.getOrDefault
import me.gabytm.minecraft.guihelper.functions.isPlayerHead
import org.apache.commons.cli.CommandLine
import org.bukkit.inventory.ItemStack
import java.util.*

abstract class HeadIdProvider {

    abstract fun getId(item: ItemStack): String

    internal fun checkItem(item: ItemStack) {
        if (!item.isPlayerHead) {
            throw ItemIsNotPlayerHeadException(item)
        }
    }

    companion object {

        const val DEFAULT = "none"

    }

    enum class Type(val alias: String) {

        BASE_64("base64"),
        HEAD_DATABASE("hdb"),
        PLAYER_NAME("player"),
        TEXTURE_ID("textureid"),
        TEXTURE_URL("textureurl");

        companion object {

            private val types = EnumSet.allOf(Type::class.java)

            fun getProvider(string: String, default: Type = BASE_64): Type {
                return types.firstOrNull {
                    it.name.equals(string, true) ||
                            it.name.replace("_", "").equals(string, true) ||
                            it.alias.equals(string, true)
                } ?: default
            }

            fun getFromInput(input: CommandLine, option: String = "heads", default: Type = BASE_64): Type {
                return input.getOrDefault(option, default) { getProvider(it, default) }
            }

        }

    }

}