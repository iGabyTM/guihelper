package me.gabytm.minecraft.guihelper.inventories

import me.gabytm.minecraft.guihelper.functions.isNotEmpty
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Chest
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.util.*

class InventoryManager : InventoryHolder {

    private val inventories: MutableMap<UUID, Inventory> = mutableMapOf()

    operator fun get(player: Player): Inventory {
        var existentInventory = inventories[player.uniqueId]

        if (existentInventory != null && existentInventory.isNotEmpty) { // Inventory exist and is not empty
            return existentInventory
        }

        val target = player.getTargetBlock(null, 5)

        if (target.type == Material.CHEST) { // Player is looking at a chest
            val chestInventory = (target.state as Chest).inventory

            if (!chestInventory.isEmpty) { // Chest inventory is not empty
                return chestInventory
            }
        }

        if (existentInventory == null) { // Creating an inventory
            existentInventory = Bukkit.createInventory(this, 54, "GUIHelper")
            inventories[player.uniqueId] = existentInventory
        }

        return existentInventory
    }

    fun removeInventory(uuid: UUID): Inventory? = inventories.remove(uuid)

    override fun getInventory(): Inventory {
        TODO("Not yet implemented")
    }

}