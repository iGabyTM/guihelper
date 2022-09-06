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

import me.gabytm.minecraft.guihelper.functions.*
import net.kyori.adventure.text.Component.text
import org.apache.commons.cli.CommandLine
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class EnchantmentsEditor : ItemEditor() {

    private val enchantments = text("hover")
        .hoverEvent(Enchantment.values().sortedBy { it.name }.joinToString("&7, &f") { it.name.lowercase() }.component())

    init {
        options.addOption('c') {
            longOpt("clear")
        }

        options.addOption('r') {
            longOpt("remove")
            arg(String::class, 1, "enchantment")
        }
    }

    override fun edit(item: ItemStack, executor: Player, input: CommandLine) {
        checkItem(item)

        val meta = item.meta ?: return

        when {
            input.hasOption('a') -> {
                val enchantment = input.getArg('a') { Enchantment.getByName(it.uppercase()) } ?: kotlin.run {
                    enchantments.send(executor)
                    return
                }
                val level = if (input.args.isEmpty()) 1 else input.args[0].toIntOrNull() ?: 1

                meta.addEnchant(enchantment, level, true)
                item.itemMeta = meta
            }

            input.hasOption('c') -> {
                meta.enchants.keys.forEach { meta.removeEnchant(it) }
                item.itemMeta = meta
            }

            input.hasOption('r') -> {
                val enchantment = input.getArg('r') { Enchantment.getByName(it.uppercase()) } ?: kotlin.run {
                    enchantments.send(executor)
                    return
                }
                meta.removeEnchant(enchantment)
                item.itemMeta = meta
            }
        }
    }

}
