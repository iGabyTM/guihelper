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

@file:JvmName("Colors")

package me.gabytm.minecraft.guihelper.functions

import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.DyeColor

private val defaultLeatherColor = Bukkit.getItemFactory().defaultLeatherColor
private val defaultFormat: (Color) -> String = { "${it.red},${it.green},${it.blue}" }

/**
 * Check if the color is the [defaultLeatherColor]
 * @since 2.0.0
 */
val Color.isDefaultLeatherColor: Boolean
    get() = this == defaultLeatherColor

/**
 * The name if it is a default color, otherwise null
 * @see [DyeColor.color]
 */
val Color.name: String?
    get() = DyeColor.getByColor(this)?.name

/**
 * Turn the color into a HEX string (RRGGBB)
 * @return HEX representation of a [Color], **without** #
 */
fun Color.asHex(): String {
    return Integer.toHexString(asRGB())
}

/**
 * Turn the color into a string that follow a certain format
 * @param format format to use (default: `red,green,blue`)
 * @return color as string
 */
fun Color.asString(format: ((Color) -> String) = defaultFormat): String {
    return format(this)
}

/**
 * Turn the name of a color, otherwise a string that follow the default format
 * @param format format to use (default: `red,green,blue`)
 * @return color as string
 */
fun Color.nameOrString(format: ((Color) -> String) = defaultFormat): String {
    return name ?: format(this)
}

/**
 * Run code if the color is not the [default leather color][org.bukkit.inventory.ItemFactory.getDefaultLeatherColor]
 * @param function function to run
 */
fun Color.ifNotDefault(function: (Color) -> Unit) {
    if (!isDefaultLeatherColor) {
        function(this)
    }
}