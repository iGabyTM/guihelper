package me.gabytm.minecraft.guihelper.items.edit.editors

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import org.bukkit.Material
import org.bukkit.command.CommandSender
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