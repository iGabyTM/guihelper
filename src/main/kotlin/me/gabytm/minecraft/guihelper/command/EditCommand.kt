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

package me.gabytm.minecraft.guihelper.command

import me.gabytm.minecraft.guihelper.functions.hand
import me.gabytm.minecraft.guihelper.item.ItemsManager
import me.gabytm.minecraft.guihelper.item.edit.editors.ItemEditor
import me.gabytm.minecraft.guihelper.util.Constants
import me.mattstudios.mf.annotations.*
import me.mattstudios.mf.base.CommandBase
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.ParseException
import org.bukkit.entity.Player

@Command(Constants.COMMAND)
@Alias(Constants.ALIAS)
class EditCommand(private val itemsManager: ItemsManager) : CommandBase() {

    private val commandParser = DefaultParser()

    @Permission(Constants.PERMISSION)
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
