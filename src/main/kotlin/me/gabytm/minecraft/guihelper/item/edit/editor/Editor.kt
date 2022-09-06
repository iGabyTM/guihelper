package me.gabytm.minecraft.guihelper.item.edit.editor

import dev.triumphteam.gui.guis.Gui
import dev.triumphteam.gui.guis.GuiItem
import org.bukkit.inventory.ItemStack

abstract class Editor {

	abstract fun canEdit(item: ItemStack): Boolean

	abstract fun getIcon(itemToEdit: ItemStack, gui: Gui): GuiItem

}
