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
import me.gabytm.minecraft.guihelper.item.heads.exceptions.HeadIdProviderNotSupportByPluginException
import me.gabytm.minecraft.guihelper.item.heads.providers.HeadIdProvider.Provider
import me.gabytm.minecraft.guihelper.util.Message
import me.gabytm.minecraft.guihelper.util.ServerVersion
import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Comment
import me.mattstudios.config.annotations.Description
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property.create
import org.apache.commons.cli.CommandLine
import org.bukkit.Color
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkEffectMeta
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta
import kotlin.system.measureTimeMillis

class DeluxeMenusGenerator(
    private val plugin: GUIHelper,
    override val pluginName: String = "DeluxeMenus",
    override val pluginVersion: String = "1.13.5",
    override val rgbFormat: (String) -> String = SPIGOT_RGB_FORMAT
) : ConfigGenerator() {

    private val defaults = Defaults(pluginName)

    init {
        options.addOption(createHeadsOption(Provider.BASE_64, Provider.HEAD_DATABASE, Provider.PLAYER_NAME))

        options.addOption('e') {
            longOpt("external")
            desc("Whether the menu is external ($pluginName/gui_menus/) or internal (config.yml)")
        }
    }

    override fun getMessage() = "  &2$pluginName &av$pluginVersion &8- &fExternal / local (config.yml) menus"

    override fun generate(context: GeneratorContext, input: CommandLine): Boolean {
        val external = input.hasOption("external")
        val config = Config(if (external) "$pluginName/gui_menus" else pluginName, plugin, true)

        val duration = measureTimeMillis {
            context.forEach { item, slot ->
                val path = if (external) "items.$slot" else "gui_menus.GUIHelper.items.$slot"
                createItem(config.createSection(path), input, item, slot)
            }
        }

        config.save()
        Message.GENERATION_DONE.send(context, config.path, duration)
        return true
    }

    override fun createItem(section: ConfigurationSection, input: CommandLine, item: ItemStack, slot: Int) {
        section["material"] = item.type.name
        section.set("data", item.durability) { it > 0 }
        section.set("amount", item.amount) { it > 1 }
        section["slot"] = slot

        val meta = item.meta ?: return

        section.set("display_name", meta::hasDisplayName) { item.displayName(rgbFormat) }
        section.set("lore", meta::hasLore) { item.lore(rgbFormat) }
        section.set("nbt_int", item.customModelData, { it > 0}) { "CustomModelData:$it" }
        section.set("unbreakable", item.isUnbreakable) { it }
        setItemFlags(section, meta.itemFlags)
        section.setList("enchantments", item.enchants { enchant, level -> "${enchant.name};${level}" })
        setMetaSpecificValues(section, input, item, meta)
    }

    private fun setItemFlags(section: ConfigurationSection, flags: Set<ItemFlag>) {
        if (defaults[Value.SETTINGS__SET_ITEM_FLAGS_AS_LIST]) {
            section.setList("item_flags", flags.map { it.name })
            return
        }

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
        when {
            item.isLeatherArmor -> {
                (meta as LeatherArmorMeta).color.ifNotDefault { section["rgb"] = it.asString() }
            }
            ServerVersion.IS_LEGACY && item.isSpawnEgg -> {
                section["data"] = item.spawnEggType.typeId
            }
            item.isShield || item.isBanner -> {
                handleBannersAndShields(section, item)
            }
            item.isPotion -> {
                handlePotions(section, meta as PotionMeta)
            }
            item.isPlayerHead -> {
                handlePlayerHeads(section, item, input.getHeadIdProvider(default = defaults[Value.SETTINGS__HEADS]))
            }
            item.isFireworkStar -> {
                handFireworkStars(section, meta as FireworkEffectMeta)
            }
        }
    }

    private fun handleBannersAndShields(section: ConfigurationSection, item: ItemStack) {
        section.setList(
            "banner_meta",
                item.patternsAndBaseColor(false).first.map { "${it.color.name};${it.pattern.name}" }
        )
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
        try {
            when (provider) {
                Provider.BASE_64 -> "basehead"
                Provider.HEAD_DATABASE -> "hdb"
                Provider.PLAYER_NAME -> "player"
                else -> throw HeadIdProviderNotSupportByPluginException(provider, pluginName)
            }.let { section["material"] = "$it-${plugin.itemsManager.getHeadId(item, provider)}" }
        } catch (e: IllegalArgumentException) {
            plugin.logger.warning(e.message)
        }
    }

    private fun handFireworkStars(section: ConfigurationSection, meta: FireworkEffectMeta) {
        if (!meta.hasEffect()) {
            return
        }

        section["rgb"] = meta.effect!!.colors[0].asString()
    }

    private class Defaults(name: String) : DefaultValues(name, Value::class.java)

    @Description(
        " ",
        "Default values that will be used in the config creation process",
        " ",
        "▪ DeluxeMenus 1.13.5 by clip (https://spigotmc.org/resources/11734/)",
        "▪ Wiki: https://wiki.helpch.at/clips-plugins/deluxemenus",
        " "
    )
    private object Value : SettingsHolder {

        @Comment("Default format used for player heads, available options: BASE_64, HEAD_DATABASE, PLAYER_NAME")
        @Path("settings.heads")
        val SETTINGS__HEADS = create(Provider.BASE_64)

        @Comment("Starting with version 1.13.4, ItemFlags can be written as a list, 'item_flags'")
        @Path("settings.saveItemFlagsAsList")
        val SETTINGS__SET_ITEM_FLAGS_AS_LIST = create(false)

    }

}
