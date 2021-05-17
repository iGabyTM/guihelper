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

            if (chestInventory.isNotEmpty) { // Chest inventory is not empty
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