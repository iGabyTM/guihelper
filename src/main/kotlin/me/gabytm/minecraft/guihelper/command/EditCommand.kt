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

import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.components.util.GuiFiller
import dev.triumphteam.gui.guis.Gui
import dev.triumphteam.gui.guis.GuiItem
import me.gabytm.minecraft.guihelper.item.ItemsManager
import me.gabytm.minecraft.guihelper.item.edit.editor.general.NameEditor
import me.gabytm.minecraft.guihelper.util.Constants
import me.mattstudios.mf.annotations.*
import me.mattstudios.mf.base.CommandBase
import net.kyori.adventure.text.Component
import org.apache.commons.cli.DefaultParser
import org.bukkit.Material
import org.bukkit.entity.Player

@Command(Constants.COMMAND)
@Alias(Constants.ALIAS)
class EditCommand(private val itemsManager: ItemsManager) : CommandBase() {

    private val commandParser = DefaultParser()

    @Permission(Constants.PERMISSION)
    @SubCommand("edit")
    fun onCommand(sender: Player) {
		val item = sender.inventory.itemInMainHand
		val gui = Gui.gui()
			.rows(3)
			.title(Component.text("GH Editor"))
			.disableAllInteractions()
			.create()

		gui.addItem(NameEditor().getIcon(item, gui))
		gui.open(sender)
    }

    @CompleteFor("edit")
    fun tabCompletion(args: List<String>, sender: Player): List<String> {
        return emptyList()
    }

}
