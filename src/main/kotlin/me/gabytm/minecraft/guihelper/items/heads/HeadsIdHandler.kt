package me.gabytm.minecraft.guihelper.items.heads

import me.gabytm.minecraft.guihelper.items.heads.providers.Base64Implementation
import me.gabytm.minecraft.guihelper.items.heads.providers.HeadDatabaseImplementation
import me.gabytm.minecraft.guihelper.items.heads.providers.PlayerNameImplementation
import me.gabytm.minecraft.guihelper.items.heads.providers.TextureImplementation
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack

class HeadsIdHandler {

    private val providers = mutableMapOf<HeadIdProvider.Provider, HeadIdProvider>()

    init {
        providers[HeadIdProvider.Provider.BASE_64] = Base64Implementation()
        providers[HeadIdProvider.Provider.PLAYER_NAME] = PlayerNameImplementation()
        providers[HeadIdProvider.Provider.TEXTURE_ID] = TextureImplementation("texture/(\\w+)")
        providers[HeadIdProvider.Provider.TEXTURE_URL] = TextureImplementation("((?:http|https)://textures\\.minecraft\\.net/texture/\\w+)")

        if ("HeadDatabase".isEnabled()) {
            providers[HeadIdProvider.Provider.HEAD_DATABASE] = HeadDatabaseImplementation()
        }
    }

    private fun String.isEnabled(): Boolean = Bukkit.getPluginManager().isPluginEnabled(this)

    operator fun get(item: ItemStack, idProvider: HeadIdProvider.Provider): String {
        return providers[idProvider]?.getId(item) ?: HeadIdProvider.DEFAULT
    }

}