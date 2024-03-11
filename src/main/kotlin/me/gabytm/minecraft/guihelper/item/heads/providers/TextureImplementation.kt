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

package me.gabytm.minecraft.guihelper.item.heads.providers

import me.gabytm.minecraft.guihelper.functions.meta
import me.gabytm.minecraft.guihelper.functions.skullTexture
import me.gabytm.minecraft.guihelper.util.ServerVersion
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

class TextureImplementation(private val extractTextureId: Boolean) : HeadIdProvider() {

	//language=RegExp
    private val regex = Regex(if (extractTextureId) "texture/(\\w+)" else "((?:http|https)://textures\\.minecraft\\.net/texture/\\w+)")

    override fun getId(item: ItemStack): String? {
        checkItem(item)

		if (ServerVersion.HAS_PROFILE_API) {
			val profile = (item.meta as SkullMeta).ownerProfile ?: return null
			val textureUrl = profile.textures.skin?.toString() ?: return null

			return if (extractTextureId) {
				textureUrl.substring(textureUrl.lastIndexOf('/') + 1)
			} else {
				textureUrl
			}
		}

        val texture = item.skullTexture ?: return null
        val matcher = regex.find(String(Base64.getDecoder().decode(texture))) ?: return null
        return matcher.groupValues[1]
    }

}
