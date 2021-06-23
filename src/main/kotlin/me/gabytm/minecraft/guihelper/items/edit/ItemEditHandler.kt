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