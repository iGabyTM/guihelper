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

import de.tr7zw.changeme.nbtapi.NBTItem
import de.tr7zw.changeme.nbtapi.NBTType
import me.gabytm.minecraft.guihelper.GUIHelper
import me.gabytm.minecraft.guihelper.config.Config
import me.gabytm.minecraft.guihelper.config.SettingsBase
import me.gabytm.minecraft.guihelper.functions.*
import me.gabytm.minecraft.guihelper.generator.base.ConfigGenerator
import me.gabytm.minecraft.guihelper.generator.base.GeneratorContext
import me.gabytm.minecraft.guihelper.item.custom.providers.itemsadder.ItemsAdderItem
import me.gabytm.minecraft.guihelper.item.heads.exceptions.HeadIdProviderNotSupportByPluginException
import me.gabytm.minecraft.guihelper.item.heads.providers.HeadIdProvider.Provider
import me.gabytm.minecraft.guihelper.util.Message
import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Comment
import me.mattstudios.config.annotations.Description
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property.create
import org.apache.commons.cli.CommandLine
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
    override val pluginVersion: String = "1.13.6",
    override val rgbFormat: (String) -> String = SPIGOT_RGB_FORMAT
) : ConfigGenerator() {

    private val settings = Settings(pluginName)
	private val ignoredNbtKeys = listOf(
		"CustomModelData"
	)

    init {
        options.addOption(createHeadsOption(Provider.BASE_64, Provider.HEAD_DATABASE, Provider.PLAYER_NAME))

        options.addOption('e') {
            longOpt("external")
            desc("Whether the menu is external ($pluginName/gui_menus/) or internal (config.yml)")
        }
    }

    override fun getMessage() = "  &2$pluginName &av$pluginVersion &8- &fExternal / local (config.yml) menus"

	override fun onReload() {
		settings.reload()
	}

    override fun generate(context: GeneratorContext, input: CommandLine): Boolean {
        val external = input.hasOption("external")

		val filePath = settings[if (external) Setting.EXTERNAL_MENUS_PATH else Setting.INTERNAL_MENUS_PATH]
        val config = Config("$filePath/${getConfigFileName(input)}.yml", plugin, true)

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

		checkForCustomItem(section, input, item)
        section.set("display_name", meta::hasDisplayName) { item.displayName(rgbFormat) }
        section.set("lore", meta::hasLore) { item.lore(rgbFormat) }
        section.set("model_data", item.customModelData) { it > 0 }
		section.set("unbreakable", item.isUnbreakable) { it }
        setItemFlags(section, meta.itemFlags)
        section.setList("enchantments", item.enchants { enchant, level -> "${enchant.name};${level}" })
        setMetaSpecificValues(section, input, item, meta)
		setNbt(section, item)
    }

	private fun checkForCustomItem(section: ConfigurationSection, input: CommandLine, item: ItemStack) {
		val manager = plugin.itemsManager

		val itemsAdderItem = manager.getCustomItem(ItemsAdderItem::class, item)

		if (itemsAdderItem != null) {
			section["material"] = "itemsadder-${itemsAdderItem.namespace}"
			return
		}

		if (item.isPlayerHead) {
			handlePlayerHeads(section, item, input.getHeadIdProvider(default = settings[Setting.SETTINGS__HEADS]))
		}
	}

    private fun setItemFlags(section: ConfigurationSection, flags: Set<ItemFlag>) {
        if (settings[Setting.SETTINGS__SET_ITEM_FLAGS_AS_LIST]) {
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

	private fun setNbt(section: ConfigurationSection, item: ItemStack) {
		val nbt = NBTItem(item)

		if (!nbt.hasNBTData()) {
			return
		}

		val strings = mutableListOf<String>()
		val ints = mutableListOf<String>()

		for (key in nbt.keys.filter { !ignoredNbtKeys.contains(it) }) {
			@Suppress("NON_EXHAUSTIVE_WHEN_STATEMENT")
			when (nbt.getType(key)) {
				NBTType.NBTTagString -> strings.add("$key:${nbt.getString(key)}")
				NBTType.NBTTagInt -> ints.add("$key:${nbt.getInteger(key)}")
			}
		}

		section.setList("nbt_strings", strings)
		section.setList("nbt_ints", ints)
	}

    private fun setMetaSpecificValues(section: ConfigurationSection, input: CommandLine, item: ItemStack, meta: ItemMeta) {
        when {
            item.isLeatherArmor -> {
                (meta as LeatherArmorMeta).color.ifNotDefault { section["rgb"] = it.asString() }
            }
            item.isShield || item.isBanner -> {
                handleBannersAndShields(section, item)
            }
            item.isPotion -> {
                handlePotions(section, meta as PotionMeta)
            }
            item.isFireworkStar -> {
                handFireworkStars(section, meta as FireworkEffectMeta)
            }
			item.isSpawnEgg -> {
				section["entity_type"] = item.spawnEggType.name
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
		meta.potionColor?.let { section["rgb"] = it.asString() }
    }

    private fun handlePlayerHeads(section: ConfigurationSection, item: ItemStack, provider: Provider) {
        try {
            val name = when (provider) {
                Provider.BASE_64 -> "basehead"
                Provider.HEAD_DATABASE -> "hdb"
                Provider.PLAYER_NAME -> "player"
                else -> throw HeadIdProviderNotSupportByPluginException(provider, pluginName)
            }

			plugin.itemsManager.getHeadId(item, provider)?.let { id -> section["material"] = "$name-$id}" }
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

    private class Settings(name: String) : SettingsBase(name, Setting::class.java)

    @Description(
        " ",
        "Settings that will be used in the config creation process",
        " ",
        "▪ DeluxeMenus 1.13.6 by clip (https://spigotmc.org/resources/11734/)",
        "▪ Wiki: https://wiki.helpch.at/clips-plugins/deluxemenus",
        " "
    )
    private object Setting : SettingsHolder {

		@Comment("The location where internal menus (from config.yml) will be saved")
		@Path("internalMenusLocation")
		val INTERNAL_MENUS_PATH = create("GUIHelper/generated-guis/DeluxeMenus/internal")

		@Comment("The location where external menus (from gui_menus) will be saved")
		@Path("externalMenusLocation")
		val EXTERNAL_MENUS_PATH = create("GUIHelper/generated-guis/DeluxeMenus/external")

        @Comment("Default format used for player heads, available options: BASE_64, HEAD_DATABASE, PLAYER_NAME")
        @Path("settings.heads")
        val SETTINGS__HEADS = create(Provider.BASE_64)

        @Comment("Starting with version 1.13.4, ItemFlags can be written as a list, 'item_flags'")
        @Path("settings.saveItemFlagsAsList")
        val SETTINGS__SET_ITEM_FLAGS_AS_LIST = create(false)

    }

}
