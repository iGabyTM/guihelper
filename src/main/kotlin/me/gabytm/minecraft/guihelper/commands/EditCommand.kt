package me.gabytm.minecraft.guihelper.commands

import me.gabytm.minecraft.guihelper.functions.hand
import me.gabytm.minecraft.guihelper.items.ItemsManager
import me.gabytm.minecraft.guihelper.items.edit.editors.ItemEditor
import me.gabytm.minecraft.guihelper.utils.Constants
import me.mattstudios.mf.annotations.Alias
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.CompleteFor
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.ParseException
import org.bukkit.entity.Player

@Command(Constants.COMMAND)
@Alias(Constants.ALIAS)
class EditCommand(private val itemsManager: ItemsManager) : CommandBase() {

    private val commandParser = DefaultParser()

    @SubCommand("edit")
    fun onCommand(sender: Player, args: Array<String>) {
        if (args.size == 1) {
            sender.sendMessage("wrong usage ...")
            return
        }

        val editor = itemsManager.getItemEditor(ItemEditor.Editor.getEditor(args[1]) ?: return) ?: return

        if (args.size == 2) {
            sender.sendMessage(editor.options.options.joinToString(", ") { it.opt })
            return
        }

        try {
            editor.edit(sender.hand, sender, commandParser.parse(editor.options, args.copyOfRange(2, args.size)))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    @CompleteFor("edit")
    fun tabCompletion(args: List<String>, sender: Player): List<String> {
        if (args.size == 1) { // /gh edit <tab>
            return ItemEditor.Editor.VALUES
        }

        return emptyList()
    }

}