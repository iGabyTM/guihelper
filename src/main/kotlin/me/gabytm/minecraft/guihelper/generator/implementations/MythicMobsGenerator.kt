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
import me.gabytm.minecraft.guihelper.functions.*
import me.gabytm.minecraft.guihelper.generator.base.ConfigGenerator
import me.gabytm.minecraft.guihelper.generator.base.GeneratorContext
import me.gabytm.minecraft.guihelper.item.heads.exceptions.HeadIdProviderNotSupportByPluginException
import me.gabytm.minecraft.guihelper.item.heads.providers.HeadIdProvider.Provider
import me.gabytm.minecraft.guihelper.util.Message
import me.gabytm.minecraft.guihelper.util.ServerVersion
import org.apache.commons.cli.CommandLine
import org.bukkit.attribute.Attribute
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta
import kotlin.system.measureTimeMillis

class MythicMobsGenerator(
    private val plugin: GUIHelper,
    override val pluginName: String = "MythicMobs",
    override val pluginVersion: String = "5.0.0",
	override val configPath: String = "GUIHelper/generated-guis/$pluginName",
    override val rgbFormat: (String) -> String = { "<#$it>" }
) : ConfigGenerator() {

    init {
        options.addOption(createHeadsOption(Provider.BASE_64, Provider.PLAYER_NAME))
    }

    override fun getMessage(): String = "  &2$pluginName &av$pluginVersion &8- &fItems"

    override fun generate(context: GeneratorContext, input: CommandLine): Boolean {
		val config = Config("$configPath/${getConfigFileName(input)}.yml", plugin, true)
        val duration = measureTimeMillis {
            context.forEach { item, slot -> createItem(config.createSection("item_$slot"), input, item, slot) }
        }

        config.save()
        Message.GENERATION_DONE.send(context, config.path, duration)
        return true
    }

    override fun createItem(section: ConfigurationSection, input: CommandLine, item: ItemStack, slot: Int) {
        section["Id"] = item.type.name.lowercase()
        section.set("Data", item.durability) { it > 0 }
        section["Amount"] = item.amount

        if (!item.hasItemMeta()) {
            return
        }

        val meta = item.meta ?: return

        section.set("Model", item.customModelData) { it > 0 }
        section.set("Name", meta::hasDisplayName) { item.displayName(rgbFormat) }
        section.set("Lore", meta::hasLore) { item.lore(rgbFormat) }
        section.setList("Enchantments", item.enchants { enchantment, level -> "${enchantment.name}:$level" })
        section.setList("Hide", meta.itemFlags.map { it.name.replace("HIDE_", "") })
        section.set("Options.Unbreakable", item.isUnbreakable) { it }

        if (ServerVersion.HAS_ATTRIBUTE && meta.hasAttributeModifiers()) {
            setAttributes(section.createSection("Attributes"), meta)
        }

        setMetaSpecificValues(section, input, item, meta)
    }

    private fun setAttributes(section: ConfigurationSection, meta: ItemMeta) {
        val attributes = meta.attributeModifiers ?: return

        for ((attribute, modifier) in attributes.entries()) {
            val attributeName = attribute.name() ?: continue
            val slot = if (modifier.slot == null) "All" else modifier.slot?.name() ?: continue

            section["$slot.$attributeName"] = modifier.amount
        }
    }

    private fun setMetaSpecificValues(section: ConfigurationSection, input: CommandLine, item: ItemStack, meta: ItemMeta) {
        when {
            meta is LeatherArmorMeta -> {
                meta.color.ifNotDefault { section.set("Options.Color", it.nameOrString()) }
            }

            item.isBanner || item.isShield -> {
                val (patterns) = item.patternsAndBaseColor(false)
                section.setList("BannerPatterns", patterns.map { "${it.pattern} ${it.color}" })
            }

            item.isFirework -> {
                handleFireworks(section.createSection("Firework"), meta as FireworkMeta)
            }

            item.isPlayerHead -> {
                handlePlayerHeads(section.createSection("Options"), item, input.getHeadIdProvider())
            }

            !ServerVersion.IS_ANCIENT && item.isPotion -> {
                handlePotions(section, meta as PotionMeta)
            }
        }
    }

    private fun handleFireworks(section: ConfigurationSection, meta: FireworkMeta) {
        if (!meta.hasEffects()) {
            return
        }

        val effects = meta.effects

        section.setList("Colors", effects.flatMap { it.colors }.map { it.asString() })
        section.setList("FadeColors", effects.flatMap { it.fadeColors }.map { it.asString() })
        section.set("Flicker", effects.any { it.hasFlicker() }) { it }
        section.set("Trail", effects.any { it.hasTrail() }) { it }
    }

    private fun handlePlayerHeads(section: ConfigurationSection, item: ItemStack, provider: Provider) {
        val configKey = when (provider) {
            Provider.BASE_64 -> "SkinTexture"
            Provider.PLAYER_NAME -> "Player"
            else -> throw HeadIdProviderNotSupportByPluginException(provider, pluginName)
        }

		plugin.itemsManager.getHeadId(item, provider)?.let { id -> section[configKey] = id }
    }

    private fun handlePotions(section: ConfigurationSection, meta: PotionMeta) {
		meta.potionColor?.let { section["Options.Color"] = it.asString() }

        val effects = mutableListOf<String>()

		// TODO update potion api
        /*with (meta.basePotionData) {
            if (type.effectType != null) {
                effects.add("${type.effectType} -1 ${if (isUpgraded) 1 else 0}")
            }
        }*/

        if (meta.hasCustomEffects()) {
            effects.addAll(meta.customEffects.map { "${it.type.name} ${it.duration} ${it.amplifier + 1}" })
        }

        section.setList("PotionEffects", effects)
    }

}

private fun EquipmentSlot.name(): String? {
    return when (this) {
        EquipmentSlot.HEAD -> "Head"
        EquipmentSlot.CHEST -> "Chest"
        EquipmentSlot.LEGS -> "Legs"
        EquipmentSlot.FEET -> "Feet"
        EquipmentSlot.HAND -> "MainHand"
        EquipmentSlot.OFF_HAND -> "OffHand"
        else -> null
    }
}
