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
import me.gabytm.minecraft.guihelper.config.defaults.implementations.ShopGuiPlusDefaultValues
import me.gabytm.minecraft.guihelper.config.defaults.implementations.ShopGuiPlusDefaultValues.DefaultValue
import me.gabytm.minecraft.guihelper.functions.*
import me.gabytm.minecraft.guihelper.generators.base.ConfigGenerator
import me.gabytm.minecraft.guihelper.generators.base.GeneratorContext
import me.gabytm.minecraft.guihelper.items.heads.providers.HeadIdProvider.Provider
import me.gabytm.minecraft.guihelper.items.heads.exceptions.HeadIdProviderNotSupportByPluginException
import me.gabytm.minecraft.guihelper.utils.Message
import me.gabytm.minecraft.guihelper.utils.ServerVersion
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Option
import org.apache.commons.lang.WordUtils
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.block.Banner
import org.bukkit.block.banner.Pattern
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.*

class ShopGuiPlusGenerator(
    private val plugin: GUIHelper,
    override val pluginVersion: String = "1.59.2",
    override val rgbFormat: (String) -> String = { "#$it" }
) : ConfigGenerator() {

    private val defaults = ShopGuiPlusDefaultValues()

    init {
        options.addOption(createHeadsOption(Provider.BASE_64, Provider.HEAD_DATABASE, Provider.PLAYER_NAME))

        options.addOption(
            Option.builder("p")
                .longOpt("page")
                .argName("page")
                .type(Int::class.java)
                .desc("The page where items will be set")
                .build()
        )
    }

    override fun getMessage() = "  &2ShopGUIPlus &av$pluginVersion &8- &fIsland mini shop items"

    override fun generate(context: GeneratorContext, input: CommandLine): Boolean {
        val startTime = System.currentTimeMillis()

        val page = input.getOrDefault('p', 0) { it.toIntOrNull() }
        val config = Config("ShopGUIPlus/shops", plugin, true)

        for ((slot, item) in context.inventory.contents.withIndex()) {
            if (item.isInvalid) {
                continue
            }

            createItem(config.createSection("shops.GUIHelper.items.P$page-$slot"), input, item, slot, page)
        }

        config.save()
        Message.GENERATION_DONE.send(context, config.path, (System.currentTimeMillis() - startTime))
        return true
    }

    private fun createItem(section: ConfigurationSection, input: CommandLine, item: ItemStack, slot: Int, page: Int) {
        val meta = item.itemMeta ?: Bukkit.getItemFactory().getItemMeta(item.type)
        val itemSection = section.createSection("item")

        section["type"] = "item"

        itemSection["material"] = item.type.name
        itemSection.set("damage", item.durability) { it > 0 }
        itemSection["amount"] = item.amount

        section["slot"] = slot
        section["page"] = page
        section.set("unstack", defaults[DefaultValue.UNSTACK]) { it }
        section.set("stacked", defaults[DefaultValue.STACKED]) { !it }
        section["buyPrice"] = defaults[DefaultValue.BUY_PRICE]
        section["sellPrice"] = defaults[DefaultValue.SELL_PRICE]

        if (meta == null) {
            return
        }

        itemSection.set("model", item.customModelData) { it > 0 }
        itemSection.set("unbreakable", item.isUnbreakable) { it }
        itemSection.set("name", meta::hasDisplayName) { item.displayName(rgbFormat) }
        itemSection.set("lore", meta::hasLore) { item.lore(rgbFormat) }
        itemSection.setList("flags", meta.itemFlags.map { it.name })
        itemSection.setList("enchantments", item.enchants { enchant, level -> "$enchant:$level" })
        setMetaSpecificValues(itemSection, input, item, meta)
    }

    private fun setMetaSpecificValues(section: ConfigurationSection, input: CommandLine, item: ItemStack, meta: ItemMeta) {
        if (item.type.name == "SHIELD" || item.isBanner) {
            handleBannersAndShields(section, item)
            return
        }

        if (item.type == Material.FIREWORK_ROCKET) {
            handleFireworks(section, meta as FireworkMeta)
            return
        }

        if (item.isFireworkStar) {
            handleFireworkStars(section, meta as FireworkEffectMeta)
            return
        }

        if (item.isLeatherArmor) {
            with ((meta as LeatherArmorMeta).color) {
                section.set("color", asString()) { !isDefaultLeatherColor }
            }
            return
        }

        if (item.isPlayerHead) {
            handlePlayerHeads(section, item, Provider.getFromInput(input))
            return
        }

        // See https://docs.brcdev.net/#/item-meta?id=_19-116
        if (item.isPotion && ServerVersion.isNewerThan(ServerVersion.V1_8_8)) {
            handlePotions(section.createSection("potion"), meta as PotionMeta)
            return
        }

        // See https://docs.brcdev.net/#/item-meta?id=spawn-eggs
        if (item.isSpawnEgg) {
            handleSpawnEggs(section, item)
            return
        }
    }

    private fun handleBannersAndShields(section: ConfigurationSection, item: ItemStack) {
        val (patterns, color) = item.patternsAndBaseColor(false)

        if (ServerVersion.isLegacy) {
            section["damage"] = null
            section["color"] = color?.name
        }

        for ((index, pattern) in patterns.withIndex()) {
            val patternSection = section.createSection("patterns.${index + 1}")

            patternSection["type"] = pattern.pattern.name
            patternSection["color"] = pattern.color.name
        }
    }

    private fun handleFireworks(section: ConfigurationSection, meta: FireworkMeta) {
        section["fireworkPower"] = meta.power

        for ((index, effect) in meta.effects.withIndex()) {
            val effectSection = section.createSection("fireworkEffects.${index + 1}")

            effectSection["type"] = effect.type.name
            effectSection["colors"] = effect.colors.mapNotNull { it.name }.toList()
            effectSection["fadeColors"] = effect.fadeColors.mapNotNull { it.name }.toList()
            effectSection["flicker"] = effect.hasFlicker()
            effectSection["trail"] = effect.hasTrail()
        }
    }

    private fun handleFireworkStars(section: ConfigurationSection, meta: FireworkEffectMeta) {
        if (!meta.hasEffect()) {
            return
        }

        with (meta.effect!!) {
            section["fireworkColor"] = colors[0].name
            section["fireworkFadeColor"] = fadeColors[0].name
        }
    }

    private fun handlePlayerHeads(section: ConfigurationSection, item: ItemStack, provider: Provider) {
        try {
            when (provider) {
                Provider.BASE_64 -> "skin"
                Provider.HEAD_DATABASE -> "headDatabase"
                Provider.PLAYER_NAME -> "skullOwner"
                else -> throw HeadIdProviderNotSupportByPluginException(provider, "ShopGUIPlus")
            }.let { section[it] = plugin.itemsManager.getHeadId(item, provider) }
        } catch (e: IllegalArgumentException) {
            plugin.logger.warning(e.message)
        }
    }

    private fun handlePotions(section: ConfigurationSection, meta: PotionMeta) {
        with (meta.basePotionData) {
            section["type"] = type.name
            section["extended"] = isExtended
            section["level"] = if (isUpgraded) 2 else 1
        }

        if (meta.hasColor()) {
            section["color"] = meta.color?.asString()
        }
    }

    private fun handleSpawnEggs(section: ConfigurationSection, item: ItemStack) {
        if (ServerVersion.isOlderThan(ServerVersion.V1_11)) {
            section["mob"] = item.spawnEggType.asString()
        } else {
            section["mob"] = item.spawnEggType.name
        }
    }

}

/**
 * Turn a [Color] into a string and follow the format used by ShopGUIPlus
 */
private fun Color.asString(): String = name ?: "$red,$green$blue"

/**
 * See the 'Minecraft 1.7-1.10' tab on [docs.brcdev.net/#/entity-types](https://docs.brcdev.net/#/entity-types)
 */
private fun EntityType.asString(): String {
    return WordUtils.capitalizeFully(name.replace('_', ' ')).replace(" ", "")
}