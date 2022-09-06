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
import me.gabytm.minecraft.guihelper.util.Message
import net.kyori.adventure.text.event.ClickEvent
import org.apache.commons.cli.CommandLine
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class NameEditor : ItemEditor() {

    init {
        options.addOption('a') {
            longOpt("append")
            desc("Append extra text after item's current name")
        }
        options.addOption('b') {
            longOpt("append-before")
            desc("Append extra text before item's current name")
        }
        options.addOption('c') {
            longOpt("clear")
            desc("Clear item's name")
        }
        options.addOption('g') {
            longOpt("get")
            desc("Receive item's name in chat")
        }
    }

    override fun edit(item: ItemStack, executor: Player, input: CommandLine) {
        checkItem(item)

        val meta = item.meta ?: return

        val itemName = if (meta.hasDisplayName()) meta.displayName else ""
        val inputName = input.args.joinToString(" ").color()

        when {
            input.hasOption('a') -> {
                item.setName(meta, "$itemName$inputName")
            }

            input.hasOption('b') -> {
                item.setName(meta, "$inputName$itemName")
            }

            input.hasOption('c') -> {
                item.setName(meta, null)
            }

            input.hasOption('g') -> {
                with (item.displayName()) {
                    Message.EDIT__NAME__COPY.component(this)
                        .clickEvent(ClickEvent.copyToClipboard(this))
                        .send(executor)
                }
            }

            else -> {
                item.setName(meta, inputName)
            }
        }
    }

}

private fun ItemStack.setName(meta: ItemMeta, name: String?) {
    if (meta.hasDisplayName() && meta.displayName == name) {
        return
    }

    meta.setDisplayName(name)
    itemMeta = meta
}
