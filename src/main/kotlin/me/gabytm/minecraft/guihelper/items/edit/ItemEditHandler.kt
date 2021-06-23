package me.gabytm.minecraft.guihelper.items.edit

import me.gabytm.minecraft.guihelper.items.edit.editors.EnchantmentsEditor
import me.gabytm.minecraft.guihelper.items.edit.editors.ItemEditor
import me.gabytm.minecraft.guihelper.items.edit.editors.ItemEditor.Editor
import me.gabytm.minecraft.guihelper.items.edit.editors.NameEditor
import org.apache.commons.cli.CommandLine
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ItemEditHandler {

    private val editors = mutableMapOf(
        Editor.ENCHANTMENTS to EnchantmentsEditor(),
        Editor.NAME to NameEditor()
    )

    fun edit(item: ItemStack, editor: Editor, executor: Player, input: CommandLine) {
        editors[editor]?.edit(item, executor, input)
    }

    operator fun get(editor: Editor): ItemEditor? = editors[editor]

}