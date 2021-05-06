package me.gabytm.minecraft.guihelper.listeners

import me.gabytm.minecraft.guihelper.functions.isNotEmpty
import me.gabytm.minecraft.guihelper.generators.GeneratorsManager
import me.gabytm.minecraft.guihelper.inventories.InventoryManager
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent

class InventoryCloseListener(private val manager: GeneratorsManager) : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun InventoryCloseEvent.onEvent() {
        if (inventory.holder !is InventoryManager) {
            return
        }

        if (inventory.isNotEmpty) {
            player.sendMessage(manager.getMessage())
        }
    }

}