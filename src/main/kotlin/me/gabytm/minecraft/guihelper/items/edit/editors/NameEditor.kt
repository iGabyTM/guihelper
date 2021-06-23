package me.gabytm.minecraft.guihelper.items.edit.editors

import me.gabytm.minecraft.guihelper.functions.addOption
import me.gabytm.minecraft.guihelper.functions.color
import me.gabytm.minecraft.guihelper.functions.displayName
import me.gabytm.minecraft.guihelper.functions.meta
import me.gabytm.minecraft.guihelper.utils.Message
import me.rayzr522.jsonmessage.JSONMessage
import org.apache.commons.cli.CommandLine
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
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
                    JSONMessage.create(Message.EDIT__NAME__COPY[this]).copyText(this).send(executor)
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
