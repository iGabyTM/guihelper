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

package me.gabytm.minecraft.guihelper

import me.gabytm.minecraft.guihelper.commands.*
import me.gabytm.minecraft.guihelper.functions.color
import me.gabytm.minecraft.guihelper.generators.GeneratorsManager
import me.gabytm.minecraft.guihelper.inventories.InventoryManager
import me.gabytm.minecraft.guihelper.items.ItemsManager
import me.gabytm.minecraft.guihelper.items.heads.HeadsIdHandler
import me.gabytm.minecraft.guihelper.listeners.InventoryCloseListener
import me.gabytm.minecraft.guihelper.utils.ServerVersion
import me.mattstudios.mf.base.CommandManager
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin

class GUIHelper : JavaPlugin() {

    lateinit var itemsManager: ItemsManager
        private set

    override fun onEnable() {
        sendLogo()

        this.itemsManager = ItemsManager()
        val generatorsManager = GeneratorsManager(this)
        val inventoryManager = InventoryManager()

        registerCommands(generatorsManager, inventoryManager)
        server.servicesManager.register(GeneratorsManager::class.java, generatorsManager, this, ServicePriority.Highest)
        server.pluginManager.registerEvents(InventoryCloseListener(generatorsManager), this)
    }

    private fun registerCommands(generatorsManager: GeneratorsManager, inventoryManager: InventoryManager) {
        with(CommandManager(this, true)) {
            completionHandler.register("#generators") { generatorsManager.registeredGeneratorsIds() }

            register(
                CreateCommand(generatorsManager, inventoryManager),
                EmptyCommand(inventoryManager),
                ReloadCommand(generatorsManager),
                OptionsCommand(generatorsManager)
            )
        }
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