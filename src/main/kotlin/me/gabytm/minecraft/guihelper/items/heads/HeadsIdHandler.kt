package me.gabytm.minecraft.guihelper.items.heads

import me.gabytm.minecraft.guihelper.items.heads.providers.Base64Implementation
import me.gabytm.minecraft.guihelper.items.heads.providers.HeadDatabaseImplementation
import me.gabytm.minecraft.guihelper.items.heads.providers.PlayerNameImplementation
import me.gabytm.minecraft.guihelper.items.heads.providers.TextureImplementation
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack

class HeadsIdHandler {

    private val providers = mutableMapOf<HeadIdProvider.Type, HeadIdProvider>()

    init {
        providers[HeadIdProvider.Type.BASE_64] = Base64Implementation()
        providers[HeadIdProvider.Type.PLAYER_NAME] = PlayerNameImplementation()
        providers[HeadIdProvider.Type.TEXTURE_ID] = TextureImplementation("texture/(\\w+)")
        providers[HeadIdProvider.Type.TEXTURE_URL] = TextureImplementation("((?:http|https)://textures\\.minecraft\\.net/texture/\\w+)")

        if ("HeadDatabase".isEnabled()) {
            providers[HeadIdProvider.Type.HEAD_DATABASE] = HeadDatabaseImplementation()
        }
    }

    private fun String.isEnabled(): Boolean = Bukkit.getPluginManager().isPluginEnabled(this)

    operator fun get(item: ItemStack, idProvider: HeadIdProvider.Type): String {
        return providers[idProvider]?.getId(item) ?: HeadIdProvider.DEFAULT
    }

}