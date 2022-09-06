package me.gabytm.minecraft.guihelper.item.edit.editor.general

import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.guis.Gui
import dev.triumphteam.gui.guis.GuiItem
import me.gabytm.minecraft.guihelper.functions.meta
import me.gabytm.minecraft.guihelper.functions.send
import me.gabytm.minecraft.guihelper.item.edit.editor.Editor
import me.gabytm.minecraft.guihelper.item.edit.input.BasicTextInput
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class NameEditor : Editor() {

	override fun canEdit(item: ItemStack): Boolean = true

	override fun getIcon(itemToEdit: ItemStack, gui: Gui): GuiItem {
		return ItemBuilder.from(Material.NAME_TAG)
			.name(Component.text("Name"))
			.lore(
				Component.text("Left click to clear"),
				Component.text("Right click to edit")
			)
			.asGuiItem { event ->
				val meta = itemToEdit.meta ?: return@asGuiItem

				if (event.isLeftClick) {
					meta.setDisplayName(null)
					itemToEdit.itemMeta = meta
					return@asGuiItem
				}

				Component.text("Input a new name. Current name '")
					.append(
						Component.text(meta.displayName)
							.clickEvent(ClickEvent.copyToClipboard(meta.displayName.replace(ChatColor.COLOR_CHAR, '&')))
							.hoverEvent(Component.text("Click to copy"))
					)
					.append(Component.text("'"))
					.send(event.whoClicked)
				gui.close(event.whoClicked)
				BasicTextInput(event.whoClicked as Player) {
					meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', it))
					itemToEdit.itemMeta = meta
					gui.open(event.whoClicked)
				}
			}
	}

}
