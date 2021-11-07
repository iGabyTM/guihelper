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
import me.gabytm.minecraft.guihelper.config.DefaultValues
import me.gabytm.minecraft.guihelper.functions.*
import me.gabytm.minecraft.guihelper.generators.base.ConfigGenerator
import me.gabytm.minecraft.guihelper.generators.base.GeneratorContext
import me.gabytm.minecraft.guihelper.utils.Message
import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Comment
import me.mattstudios.config.annotations.Description
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property
import org.apache.commons.cli.CommandLine
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import kotlin.system.measureTimeMillis

class BossShopProGenerator(
    private val plugin: GUIHelper,
    override val pluginName: String = "BossShopPro",
    override val pluginVersion: String = "2.0.9",
) : ConfigGenerator() {

    private val defaults = Defaults(pluginName)

    init {
        options.addOption(createPageOption("The page where items will be set"))
    }

    override fun getMessage(): String = "  &2$pluginName &av$pluginVersion &8- &fShop items"

    override fun generate(context: GeneratorContext, input: CommandLine): Boolean {
        val page = input.getOrDefault('p', 1) { it.toIntOrNull() }
        val config = Config("$pluginName/shops", plugin, true)

        val duration = measureTimeMillis {
            context.forEach { item, slot ->
                val finalSlot = if (page == 1) slot + 1 else (54 * (page - 1)) + (slot + 1)
                createItem(config.createSection("shop.$finalSlot"), item, finalSlot)
            }
        }

        config.save()
        Message.GENERATION_DONE.send(context, config.path, duration)
        return true
    }

    override fun createItem(section: ConfigurationSection, item: ItemStack, slot: Int) {
        val itemProperties = getItemProperties(item)

        section["MenuItem"] = itemProperties

        if (defaults[Value.SETTINGS__GIVE_ITEM]) {
            section["RewardType"] = "ITEM"
            // I need to use #toList() to avoid YAML anchors since the same list is used on two places
            section["Reward"] = listOf(itemProperties.toList())
        }

        section["PriceType"] = defaults[Value.PRICE_TYPE]
        section["Price"] = defaults[Value.PRICE]
        section.set("Message", defaults[Value.MESSAGE]) { it.isNotEmpty() }
        section.set("ExtraPermission", defaults[Value.EXTRA_PERMISSION]) { it.isNotEmpty() }
        section["InventoryLocation"] = slot
        section.set("Condition", defaults[Value.CONDITION]) { it.isNotEmpty() }
    }

    @Suppress("SpellCheckingInspection")
    private fun getItemProperties(item: ItemStack): List<String> {
        val properties = mutableListOf(
            "type:${item.type}",
            "amount:${item.amount}"
        )

        item.durability.ifNotZero { properties.add("durability:$it") }

        if (!item.hasItemMeta()) {
            return properties
        }

        val meta = item.meta ?: return properties

        item.customModelData.ifNotZero { properties.add("custommodeldata:$it") }
        item.isUnbreakable.takeIf { it }.let { properties.add("unbreakable:true") }

        item.displayName(rgbFormat).ifNotEmpty { properties.add("name:$it") }
        properties.addAll(item.lore(rgbFormat).map { "lore:$it" })
        properties.addAll(item.enchants { enchantment, level -> "enchantment:${enchantment.name}#$level" })

        meta.itemFlags.ifNotEmpty { properties.add("hideflags:${meta.itemFlags.joinToString("#")}") }

        when {
            meta is LeatherArmorMeta -> {
                meta.color.ifNotDefault { properties.add("color:${it.red}#${it.green}#${it.blue}") }
            }

            item.type == Material.WRITTEN_BOOK -> {
                meta as BookMeta

                val title = if (meta.hasTitle()) meta.title?.fixColors(rgbFormat) ?: "" else ""
                val author = if (meta.hasAuthor()) meta.author ?: "" else ""

                properties.add("book:$title#$author")

                if (meta.hasPages()) {
                    for ((pageNumber, page) in meta.pages.withIndex()) {
                        for (line in page.split("\n")) {
                            properties.add("bookpage:${pageNumber + 1}#${line.fixColors(rgbFormat)}")
                        }
                    }
                }
            }

            item.isSpawnEgg -> {
                properties.add("monsteregg:${item.spawnEggType}")
            }

            // TODO: 11/6/2021 add support for potions
            item.isPotion -> {

            }

            item.isPlayerHead -> {
                properties.add("customskull:${plugin.itemsManager.getHeadId(item)}")
            }
        }

        return properties
    }

    private class Defaults(name: String) : DefaultValues(name, Value::class.java)

    @Description(
        " ",
        "Default values that will be used in the config creation process",
        " ",
        "▪ BossShopPro 2.0.9 by Blackixx (https://spigotmc.org/resources/222/)",
        "▪ Wiki: https://www.spigotmc.org/wiki/bossshoppro-configuration/",
        " "
    )
    private object Value : SettingsHolder {

        @Comment("Whether the MenuItem should be set as Reward as well")
        @Path("settings.giveItem")
        val SETTINGS__GIVE_ITEM = Property.create(true)

        @Path("priceType")
        val PRICE_TYPE = Property.create("MONEY")

        @Path("price")
        val PRICE = Property.create(1000)

        @Path("extraPermission")
        val EXTRA_PERMISSION = Property.create("")

        @Path("message")
        val MESSAGE = Property.create("&aYou've purchased &e%reward%&a for &e$%price%")

        @Path("condition")
        val CONDITION = Property.create(emptyList<String>())

    }

}
