package me.gabytm.minecraft.guihelper.generators.base

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Base class for all config generators
 * @since 1.1.0
 */
abstract class ConfigGenerator {

    /**
     * Main method called when the [me.gabytm.minecraft.guihelper.commands.CreateCommand] is used
     * @param context access to the [Player] who used the command or the [org.bukkit.inventory.Inventory]
     * @param args the arguments from command
     * @return if the action was successful or not
     */
    abstract fun generate(context: GeneratorContext, args: Array<String>): Boolean

    /**
     * Method used for handling tab completion for the [me.gabytm.minecraft.guihelper.commands.CreateCommand]
     * @param sender commands sender
     * @param args list of arguments typed by the [Player]
     * @return tab completion
     */
    abstract fun tabCompletion(sender: Player, args: List<String>): List<String>

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
    protected abstract fun createItem(section: ConfigurationSection, item: ItemStack, slot: Int)

}