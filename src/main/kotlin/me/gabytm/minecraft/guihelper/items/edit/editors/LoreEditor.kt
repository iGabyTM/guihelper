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

package me.gabytm.minecraft.guihelper.items.edit.editors

import me.gabytm.minecraft.guihelper.functions.addOption
import me.gabytm.minecraft.guihelper.functions.arg
import me.gabytm.minecraft.guihelper.functions.getOrDefault
import me.gabytm.minecraft.guihelper.functions.meta
import org.apache.commons.cli.CommandLine
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class LoreEditor : ItemEditor() {

    init {
        options.addOption('a') { longOpt("add") }
        options.addOption('A') { longOpt("Append") }

        options.addOption('b') { longOpt("before") }

        options.addOption('c') { longOpt("clear") }

        options.addOption('i') {
            longOpt("insert")
            arg(Int::class, 1, "index")
        }

        options.addOption('r') {
            longOpt("replace")
            arg(Int::class, 1, "index")
        }

        options.addOption('s') { longOpt("set") }
    }

    override fun edit(item: ItemStack, player: Player, input: CommandLine) {
        checkItem(item)
        input.argList

        val meta = item.meta ?: return

        val itemLore = if (meta.hasLore()) meta.lore ?: mutableListOf() else mutableListOf()
        val inputLore = input.args.joinToString(" ").split(';')

        when {
            input.hasOption('b') -> {
                item.setLore(meta, inputLore + itemLore)
            }

            input.hasOption('c') -> {
                item.setLore(meta, emptyList())
            }

            input.hasOption('i') -> {
                val index = input.getOrDefault('i', 0) { it.toIntOrNull() } - 1

                if (index < 0 || index + 1 >= itemLore.size) {
                    itemLore.addAll(inputLore)
                } else {
                    itemLore.addAll(index, inputLore)
                }

                item.setLore(meta, itemLore)
            }

            input.hasOption('r') -> {
                val index = input.getOrDefault('r', 0) { it.toIntOrNull() } - 1
            }

            input.hasOption('s') -> {
                item.setLore(meta, inputLore)
            }

            else -> {
                item.setLore(meta, itemLore + inputLore)
            }
        }
    }

}

private fun ItemStack.setLore(meta: ItemMeta, lore: List<String>) {
    if (meta.hasLore() && meta.lore == lore) {
        return
    }

    meta.lore = lore
    itemMeta = meta
}