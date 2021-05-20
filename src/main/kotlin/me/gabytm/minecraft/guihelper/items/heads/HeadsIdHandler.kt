package me.gabytm.minecraft.guihelper.items.heads

import me.gabytm.minecraft.guihelper.items.heads.providers.*
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack

class HeadsIdHandler {

    private val providers = mutableMapOf(
        HeadIdProvider.Provider.BASE_64 to Base64Implementation(),
        HeadIdProvider.Provider.PLAYER_NAME to PlayerNameImplementation(),
        HeadIdProvider.Provider.TEXTURE_ID to TextureImplementation("texture/(\\w+)"),
        HeadIdProvider.Provider.TEXTURE_URL to TextureImplementation("((?:http|https)://textures\\.minecraft\\.net/texture/\\w+)")
    )

    init {
        if ("HeadDatabase".isEnabled()) {
            providers[HeadIdProvider.Provider.HEAD_DATABASE] = HeadDatabaseImplementation()
        }
    }

    operator fun get(item: ItemStack, idProvider: HeadIdProvider.Provider): String {
        return providers[idProvider]?.getId(item) ?: HeadIdProvider.DEFAULT
    }

}

/**
 * Whether a [plugin][org.bukkit.plugin.Plugin] is enabled or not
 */
private fun String.isEnabled(): Boolean = Bukkit.getPluginManager().isPluginEnabled(this)
