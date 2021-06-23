/*
 * Copyright 2021 GabyTM
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
