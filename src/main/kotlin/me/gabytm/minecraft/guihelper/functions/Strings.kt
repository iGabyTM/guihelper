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

@file:JvmName("Strings")

package me.gabytm.minecraft.guihelper.functions

import me.gabytm.minecraft.guihelper.utils.ServerVersion
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.ChatColor

private val vanillaRgbRegex = Regex("&x((?:&[a-fA-F0-9]){6})")

val NO_RGB_SUPPORT: (String) -> String = { "" }
val SPIGOT_RGB_FORMAT: (String) -> String = { "&#$it" }

fun String.color(): String = ChatColor.translateAlternateColorCodes('&', this)

fun String.fixColors(format: ((String) -> String) = SPIGOT_RGB_FORMAT): String {
    val replaced = replace(ChatColor.COLOR_CHAR, '&')

    return if (ServerVersion.HAS_HEX && format != NO_RGB_SUPPORT) {
        vanillaRgbRegex.findAll(replaced).fold(replaced) { str, matcher ->
            str.replace(matcher.groupValues[0], format(matcher.groupValues[1].replace("&", "")))
        }
    } else {
        replaced
    }
}

fun String.component(): Component = LegacyComponentSerializer.legacyAmpersand().deserialize(color())

fun String.ifNotEmpty(function: (String) -> Any) {
    if (this.isNotEmpty()) {
        function(this)
    }
}