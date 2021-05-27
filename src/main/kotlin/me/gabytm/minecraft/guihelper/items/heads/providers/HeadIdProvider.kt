package me.gabytm.minecraft.guihelper.items.heads.providers

import me.gabytm.minecraft.guihelper.functions.isPlayerHead
import me.gabytm.minecraft.guihelper.items.heads.exceptions.ItemIsNotPlayerHeadException
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.jvm.Throws

abstract class HeadIdProvider {

    /**
     * Check if the item [isPlayerHead]
     * @throws ItemIsNotPlayerHeadException if the item is not a player head
     */
    @Throws(ItemIsNotPlayerHeadException::class)
    internal fun checkItem(item: ItemStack) {
        if (!item.isPlayerHead) {
            throw ItemIsNotPlayerHeadException(item)
        }
    }

    abstract fun getId(item: ItemStack): String

    companion object {

        const val DEFAULT = "none"

    }

    enum class Provider(val alias: String) {

        BASE_64("base64"),
        HEAD_DATABASE("hdb"),
        PLAYER_NAME("player"),
        TEXTURE_ID("textureid"),
        TEXTURE_URL("textureurl");

        companion object {

            private val providers = EnumSet.allOf(Provider::class.java)

            fun getProvider(string: String, default: Provider = BASE_64): Provider {
                return providers.firstOrNull {
                    it.name.equals(string, true) ||
                    it.alias.equals(string, true) ||
                    it.name.replace("_", "").equals(string, true)
                } ?: default
            }

        }

    }

}