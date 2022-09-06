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

package me.gabytm.minecraft.guihelper.generator.base

import me.gabytm.minecraft.guihelper.functions.error
import me.gabytm.minecraft.guihelper.functions.isNotNull
import me.gabytm.minecraft.guihelper.functions.warning
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class GeneratorContext(val player: Player, val inventory: Inventory) {

    /**
     * Run the given function for all items in the gui that aren't null / [AIR][Material.AIR]
     * @param function function to run (item, slot)
     */
    fun forEach(function: (item: ItemStack, slot: Int) -> Unit) {
        inventory.contents.withIndex()
            .filter { it.value.isNotNull() }
            .forEach { function(it.value, it.index) }
    }

    /**
     * Same as above but only for items that are on the specified [rows]
     * @param rows the amount of rows from where items will be processed
     * @param pluginName the plugin name from where this method is called, used for logging
     * @param function function to run (item, slot)
     */
    fun forEach(rows: Int, pluginName: String, function: (item: ItemStack, slot: Int) -> Unit) {
        try {
            require(rows in 1..6) { "($pluginName) Invalid amount of rows $rows, must be between 1 and 6" }
            val max = rows * 9

            if (rows < 6) {
                warning("$pluginName supports only $rows rows, items from slot $max+ will be ignored.")
            }

            inventory.contents.withIndex()
                .filter { it.index < max && it.value.isNotNull() }
                .forEach { function(it.value, it.index) }
        } catch (e: IllegalArgumentException) {
            error("", e)
        }
    }

}
