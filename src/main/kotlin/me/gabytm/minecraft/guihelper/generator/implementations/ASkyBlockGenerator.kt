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

package me.gabytm.minecraft.guihelper.generator.implementations

import me.gabytm.minecraft.guihelper.GUIHelper
import me.gabytm.minecraft.guihelper.config.Config
import me.gabytm.minecraft.guihelper.config.SettingsBase
import me.gabytm.minecraft.guihelper.functions.*
import me.gabytm.minecraft.guihelper.generator.base.ConfigGenerator
import me.gabytm.minecraft.guihelper.generator.base.GeneratorContext
import me.gabytm.minecraft.guihelper.util.Message
import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Description
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property
import org.apache.commons.cli.CommandLine
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack
import kotlin.system.measureTimeMillis

class ASkyBlockGenerator(
    private val plugin: GUIHelper,
    override val pluginName: String = "ASkyBlock",
    override val pluginVersion: String = "3.0.9.4",
    override val rgbFormat: (String) -> String = NO_RGB_SUPPORT
) : ConfigGenerator() {

    private val settings = Settings(pluginName)

    override fun getMessage() = "  &2$pluginName &av$pluginVersion &8- &fIsland mini shop items"

	override fun onReload() {
		settings.reload()
	}

    override fun generate(context: GeneratorContext, input: CommandLine): Boolean {
        val config = Config(pluginName, plugin, true)

        val duration = measureTimeMillis {
            context.forEach { item, slot -> createItem(config.createSection("items.item${slot + 1}"), item, slot) }
        }

        config.save()
        Message.GENERATION_DONE.send(context, config.path, duration)
        return true
    }

    override fun onReload() {
        settings.reload()
    }

    override fun createItem(section: ConfigurationSection, item: ItemStack, slot: Int) {
        section["material"] = item.type.name
        section["quantity"] = item.amount
        section["price"] = settings[Setting.PRICE]
        section["sellprice"] = settings[Setting.SELL_PRICE]

        val meta = item.meta ?: return

        section.set("lore", meta::hasLore) { item.lore().joinToString("|") }
    }

    private class Settings(name: String) : SettingsBase(name, Setting::class.java)

    @Description(
        " ",
        "Settings that will be used in the config creation process",
        " ",
        "▪ ASkyBlock 3.0.9.4 by Tastybento (https://spigotmc.org/resources/1220/)",
        "▪ Wiki: https://github.com/tastybento/askyblock/wiki",
        " "
    )
    private object Setting : SettingsHolder {

        @Path("price")
        val PRICE = Property.create(100)

        @Path("sellprice")
        val SELL_PRICE = Property.create(10)

    }

}
