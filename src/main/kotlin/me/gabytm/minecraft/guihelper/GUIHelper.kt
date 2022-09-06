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

import me.gabytm.minecraft.guihelper.command.*
import me.gabytm.minecraft.guihelper.config.SettingsBase
import me.gabytm.minecraft.guihelper.functions.color
import me.gabytm.minecraft.guihelper.generator.GeneratorsManager
import me.gabytm.minecraft.guihelper.inventory.InventoryManager
import me.gabytm.minecraft.guihelper.item.ItemsManager
import me.gabytm.minecraft.guihelper.listener.InventoryCloseListener
import me.gabytm.minecraft.guihelper.util.BStats
import me.gabytm.minecraft.guihelper.util.ServerVersion
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import java.util.regex.Pattern

class GUIHelper : JavaPlugin() {

    lateinit var audiences: BukkitAudiences private set
    lateinit var itemsManager: ItemsManager private set
    lateinit var generatorsManager: GeneratorsManager private set
    lateinit var inventoryManager: InventoryManager private set

    override fun onEnable() {
        if (ServerVersion.IS_EXTREMELY_OLD) {
            logger.severe("Versions older than 1.8 aren't supported.")
            logger.warning("If you are running a newer version and see this, report it to https://github.com/iGabyTM/GUIHelper/issues")

            server.pluginManager.disablePlugin(this)
            return
        }

        sendLogo()
		SettingsBase.move(dataFolder.toPath())

        this.audiences = BukkitAudiences.create(this)
        this.itemsManager = ItemsManager()
        this.generatorsManager = GeneratorsManager(this)
        this.inventoryManager = InventoryManager()

        CommandManager(this)
        BStats(this)

        server.servicesManager.register(GeneratorsManager::class.java, generatorsManager, this, ServicePriority.Highest)
        server.pluginManager.registerEvents(InventoryCloseListener(generatorsManager, this.audiences), this)
    }

    private fun sendLogo() {
        val matcher = Pattern.compile("\\d+\\.\\d+(?:\\.\\d+)?").matcher(Bukkit.getBukkitVersion())
        val serverVersion = if (matcher.find()) matcher.group() else "unknown"

        with (description) {
            sequenceOf(
                "&2 _____   _____   ",
                "&2|   __| |  |  |  &fGUIHelper &av$version &fby &a${authors.joinToString()} &7($serverVersion)",
                "&2|  |  | |     |  &7$description",
                "&2|_____| |__|__|  "
            ).forEach { server.consoleSender.sendMessage(it.color()) }
        }
    }

}
