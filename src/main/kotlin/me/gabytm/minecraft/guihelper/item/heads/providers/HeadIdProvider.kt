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

import me.gabytm.minecraft.guihelper.functions.isPlayerHead
import me.gabytm.minecraft.guihelper.item.heads.exceptions.ItemIsNotPlayerHeadException
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
