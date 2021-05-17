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
import me.gabytm.minecraft.guihelper.config.defaults.implementations.ASkyBlockDefaultValues
import me.gabytm.minecraft.guihelper.config.defaults.implementations.ASkyBlockDefaultValues.DefaultValue
import me.gabytm.minecraft.guihelper.functions.isInvalid
import me.gabytm.minecraft.guihelper.functions.lore
import me.gabytm.minecraft.guihelper.functions.set
import me.gabytm.minecraft.guihelper.generators.base.ConfigGenerator
import me.gabytm.minecraft.guihelper.generators.base.GeneratorContext
import me.gabytm.minecraft.guihelper.utils.Message
import org.apache.commons.cli.CommandLine
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

class ASkyBlockGenerator(
    private val plugin: GUIHelper,
    override val pluginVersion: String = "3.0.9.4"
) : ConfigGenerator() {

    private val defaults = ASkyBlockDefaultValues()

    override fun getMessage() = "  &2ASkyBlock &8- &fIsland mini shop items"

    override fun generate(context: GeneratorContext, input: CommandLine): Boolean {
        val start = System.currentTimeMillis()
        val config = Config("ASkyBlock", plugin, true)

        for ((slot, item) in context.inventory.contents.withIndex()) {
            if (item.isInvalid) {
                continue
            }

            createItem(config.createSection("items.item${slot + 1}"), item, slot)
        }

        config.save()
        Message.GENERATION_DONE.send(context, config.path, (System.currentTimeMillis() - start))
        return true
    }

    override fun onReload() {
        defaults.reload()
    }

    override fun createItem(section: ConfigurationSection, item: ItemStack, slot: Int) {
        section["material"] = item.type.name
        section["quantity"] = item.amount
        section["price"] = defaults[DefaultValue.PRICE]
        section["sellprice"] = defaults[DefaultValue.SELL_PRICE]

        val meta = item.itemMeta ?: return

        section.set("lore", meta::hasLore) { item.lore.joinToString("|") }
    }

}