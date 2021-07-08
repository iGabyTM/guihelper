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

package me.gabytm.minecraft.guihelper.generators.implementations

import me.gabytm.minecraft.guihelper.GUIHelper
import me.gabytm.minecraft.guihelper.config.Config
import me.gabytm.minecraft.guihelper.functions.*
import me.gabytm.minecraft.guihelper.generators.base.ConfigGenerator
import me.gabytm.minecraft.guihelper.generators.base.GeneratorContext
import me.gabytm.minecraft.guihelper.items.serialization.serializers.Serializer
import me.gabytm.minecraft.guihelper.utils.Message
import org.apache.commons.cli.CommandLine
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack
import kotlin.system.measureTimeMillis

class LemonMobCoinsGenerators(
    private val plugin: GUIHelper,
    override val pluginName: String = "LemonMobCoins",
    override val pluginVersion: String = "1.4"
) : ConfigGenerator() {

    override fun getMessage(): String = "  &2$pluginName &av$pluginVersion &8- &fShop items"

    override fun generate(context: GeneratorContext, input: CommandLine): Boolean {
        val config = Config(pluginName, plugin, true)

        val duration = measureTimeMillis {
            context.forEach { item, slot -> createItem(config.createSection("item-$slot"), input, item, slot) }
        }

        config.save()
        Message.GENERATION_DONE.send(context, config.path, duration)
        return true
    }

    override fun createItem(section: ConfigurationSection, input: CommandLine, item: ItemStack, slot: Int) {
        section["material"] = item.type.name
        section["slot"] = slot
        section["amount"] = item.amount
        section["permission"] = false
        section["price"] = 100

        val meta = item.meta ?: return

        section.set("displayname", meta::hasDisplayName, item::displayName)
        section.set("lore", meta::hasLore, item::lore)
        section["commands"] = listOf("give %player% ${plugin.itemsManager.serialize(item, Serializer.ESSENTIALSX)}")
    }

}