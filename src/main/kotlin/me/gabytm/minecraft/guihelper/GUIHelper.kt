package me.gabytm.minecraft.guihelper

import me.gabytm.minecraft.guihelper.commands.CreateCommand
import me.gabytm.minecraft.guihelper.commands.EmptyCommand
import me.gabytm.minecraft.guihelper.functions.color
import me.gabytm.minecraft.guihelper.generators.GeneratorsManager
import me.gabytm.minecraft.guihelper.inventories.InventoryManager
import me.gabytm.minecraft.guihelper.listeners.InventoryCloseListener
import me.gabytm.minecraft.guihelper.utils.Constants
import me.gabytm.minecraft.guihelper.utils.ServerVersion
import me.mattstudios.mf.base.CommandManager
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin

class GUIHelper : JavaPlugin() {

    override fun onEnable() {
        sendLogo()

        val generatorsManager = GeneratorsManager(this)
        val inventoryManager = InventoryManager()

        registerCommands(generatorsManager, inventoryManager)
        server.servicesManager.register(GeneratorsManager::class.java, generatorsManager, this, ServicePriority.Highest)
        server.pluginManager.registerEvents(InventoryCloseListener(generatorsManager), this)
    }

    private fun registerCommands(generatorsManager: GeneratorsManager, inventoryManager: InventoryManager) {
        val commandManager = CommandManager(this, true)

        commandManager.completionHandler.register(Constants.Completion.GENERATORS) { generatorsManager.getRegisteredGeneratorsIds() }

        commandManager.register(
            CreateCommand(generatorsManager, inventoryManager),
            EmptyCommand(inventoryManager)
        )
    }

    private fun sendLogo() {
        sequenceOf(
            "&2 _____   _____   ",
            "&2|   __| |  |  |  &fGUIHelper &av${description.version} &fby &a${description.authors.joinToString()} &7(${ServerVersion.CURRENT_VERSION.bukkitName})",
            "&2|  |  | |     |  &7${description.description}",
            "&2|_____| |__|__|  "
        ).forEach { server.consoleSender.sendMessage(it.color()) }
    }

}