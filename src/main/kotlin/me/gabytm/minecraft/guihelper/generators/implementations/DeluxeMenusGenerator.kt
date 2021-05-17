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
import me.gabytm.minecraft.guihelper.items.heads.HeadIdProvider.Provider
import me.gabytm.minecraft.guihelper.utils.Message
import me.gabytm.minecraft.guihelper.utils.ServerVersion
import org.apache.commons.cli.CommandLine
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta

class DeluxeMenusGenerator(
    private val plugin: GUIHelper,
    override val pluginVersion: String = "1.13.3"
) : ConfigGenerator() {

    init {
        options.addOption(createHeadsOption(Provider.BASE_64, Provider.HEAD_DATABASE, Provider.PLAYER_NAME))

        options.addOption(
            "e",
            "external",
            false,
            "Whether the menu is external (DeluxeMenus/gui_menus/) or internal (config.yml)"
        )
    }

    override fun getMessage() = "  &2DeluxeMenus &av$pluginVersion &8- &fExternal / local (config.yml) menus"

    override fun generate(context: GeneratorContext, input: CommandLine): Boolean {
        val startTime = System.currentTimeMillis()

        val external = input.hasOption("external")
        val config = Config(if (external) "DeluxeMenus/gui_menus" else "DeluxeMenus", plugin, true)

        for ((slot, item) in context.inventory.contents.withIndex()) {
            if (item.isInvalid) {
                continue
            }

            val path = if (external) "items.$slot" else "gui_menus.GUIHelper.items.$slot"
            createItem(config.createSection(path), item, slot)
        }

        config.save()
        Message.GENERATION_DONE.send(context, config.path, (System.currentTimeMillis() - startTime))
        return true
    }

    override fun createItem(section: ConfigurationSection, input: CommandLine, item: ItemStack, slot: Int) {
        val meta = item.itemMeta ?: Bukkit.getItemFactory().getItemMeta(item.type)

        section["material"] = item.type.name
        section.set("data", item.durability) { it > 0 }
        section.set("amount", item.amount) { it > 1 }
        section["slot"] = slot

        if (meta == null) {
            return
        }

        section.set("display_name", meta::hasDisplayName, item::displayName)
        section.set("lore", meta::hasLore, item::lore)
        section.set("nbt_int", item.customModelData, { it > 0}) { "CustomModelData:$it" }
        section.set("unbreakable", item.isUnbreakable) { it }
        setItemFlags(section, meta.itemFlags)
        section.setList("enchantments", item.enchants { enchant, level -> "${enchant.name};${level}" })
        setMetaSpecificValues(section, input, item, meta)
    }

    private fun setItemFlags(section: ConfigurationSection, flags: Set<ItemFlag>) {
        flags.forEach { flag ->
            when (flag) {
                ItemFlag.HIDE_ATTRIBUTES -> "hide_attributes"
                ItemFlag.HIDE_ENCHANTS -> "hide_enchantments"
                ItemFlag.HIDE_POTION_EFFECTS -> "hide_effects"
                ItemFlag.HIDE_UNBREAKABLE -> "hide_unbreakable"
                else -> null
            }?.let { section[it] = true }
        }
    }

    private fun setMetaSpecificValues(section: ConfigurationSection, input: CommandLine, item: ItemStack, meta: ItemMeta) {
        if (item.isLeatherArmor) {
            with((meta as LeatherArmorMeta).color) {
                section.set("rgb", asString()) { !isDefaultLeatherColor }
            }
            return
        }

        if (item.isSpawnEgg && ServerVersion.isLegacy) {
            section["data"] = item.spawnEggType.typeId
            return
        }

        if (item.isBanner || item.type.name == "SHIELD") {
            section.setList("banner_meta", (meta as BannerMeta).patterns.map { "${it.color};${it.pattern}" })
            return
        }

        if (item.isPotion) {
            handlePotions(section, meta as PotionMeta)
            return
        }

        if (item.isPlayerHead) {
            handlePlayerHeads(section, item, Provider.getFromInput(input))
        }
    }

    private fun handlePotions(section: ConfigurationSection, meta: PotionMeta) {
        val base = meta.basePotionData
        val effects = mutableListOf("${base.type.name};1;${if (base.isUpgraded) 1 else 0}") // TODO: Find a way to add the base effect

        if (meta.hasCustomEffects()) {
            effects.addAll(meta.customEffects.map { "${it.type.name};${it.duration};${it.amplifier}" })
        }

        section["potion_effects"] = effects
        section["rgb"] = (meta.color ?: Color.GREEN).asString()
    }

    private fun handlePlayerHeads(section: ConfigurationSection, item: ItemStack, provider: Provider) {
        val id = plugin.headsIdHandler[item, provider]

        when (provider) {
            Provider.BASE_64 -> "basehead-$id"
            Provider.HEAD_DATABASE -> "hdb-$id"
            Provider.PLAYER_NAME -> "player-$id"
            else -> throw IllegalArgumentException("$provider is not supported by DeluxeMenus")
        }.let { section["material"] = it }
    }

}

/**
 * Turn a [Color] into a string and follow the format used by DeluxeMenus
 */
private fun Color.asString() = "$red,$green,$blue"