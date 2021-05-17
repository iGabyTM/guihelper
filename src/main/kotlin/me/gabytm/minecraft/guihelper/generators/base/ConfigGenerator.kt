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

package me.gabytm.minecraft.guihelper.generators.base

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Base class for all config generators
 * @since 1.1.0
 */
abstract class ConfigGenerator {

    val options = Options()

    abstract val pluginVersion: String

    /**
     * Main method called when the [me.gabytm.minecraft.guihelper.commands.CreateCommand] is used
     * @param context access to the [Player] who used the command or the [org.bukkit.inventory.Inventory]
     * @param input user's input
     * @return if the action was successful or not
     */
    abstract fun generate(context: GeneratorContext, input: CommandLine): Boolean

    /*
    /**
     * Method used for handling tab completion for the [me.gabytm.minecraft.guihelper.commands.CreateCommand]
     * @param sender commands sender
     * @param args list of arguments typed by the [Player]
     * @return tab completion
     */
    open fun tabCompletion(sender: Player, args: List<String>): List<String> = emptyList()
     */

    /**
     * The message that is displayed for this generated on the [me.gabytm.minecraft.guihelper.commands.ListCommand]
     * or when a GUI is closed ([me.gabytm.minecraft.guihelper.listeners.InventoryCloseListener])
     * @see [me.gabytm.minecraft.guihelper.generators.GeneratorsManager.getMessage]
     */
    abstract fun getMessage(): String

    /**
     * The method that is called to set an item in config
     * @param section where the item is saved
     * @param item item to process
     * @param slot item's slot in the GUI
     */
    open fun createItem(section: ConfigurationSection, item: ItemStack, slot: Int) {}

    open fun createItem(section: ConfigurationSection, input: CommandLine, item: ItemStack, slot: Int) {}

    open fun onReload() {}

}