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

package me.gabytm.minecraft.guihelper.item.edit.editors

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

abstract class ItemEditor {

    val options = Options()

    internal fun checkItem(item: ItemStack) {
        if (item.type == Material.AIR) {
            throw IllegalArgumentException()
        }
    }

    abstract fun edit(item: ItemStack, executor: Player, input: CommandLine)

    enum class Editor {

        ENCHANTMENTS,
        NAME;

        companion object {

            private val editors = EnumSet.allOf(Editor::class.java)

            val VALUES = values().map { it.name.lowercase() }

            fun getEditor(input: String): Editor? {
                return editors.firstOrNull { it.name.equals(input, true) }
            }

        }

    }

}
