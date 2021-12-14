package me.gabytm.minecraft.guihelper.generators.implementations

import me.gabytm.minecraft.guihelper.GUIHelper
import me.gabytm.minecraft.guihelper.config.Config
import me.gabytm.minecraft.guihelper.functions.*
import me.gabytm.minecraft.guihelper.generators.base.ConfigGenerator
import me.gabytm.minecraft.guihelper.generators.base.GeneratorContext
import me.gabytm.minecraft.guihelper.items.heads.exceptions.HeadIdProviderNotSupportByPluginException
import me.gabytm.minecraft.guihelper.items.heads.providers.HeadIdProvider.Provider
import me.gabytm.minecraft.guihelper.utils.Message
import me.gabytm.minecraft.guihelper.utils.VersionHelper
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
    override val rgbFormat: (String) -> String = { "<#$it>" }
) : ConfigGenerator() {

    init {
        options.addOption(createHeadsOption(Provider.BASE_64, Provider.PLAYER_NAME))
    }

    override fun getMessage(): String = "TODO"

    override fun generate(context: GeneratorContext, input: CommandLine): Boolean {
        val config = Config(pluginName, plugin, true)
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

        if (VersionHelper.HAS_ATTRIBUTE && meta.hasAttributeModifiers()) {
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

            !VersionHelper.IS_ANCIENT && item.isPotion -> {
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
        when (provider) {
            Provider.BASE_64 -> "SkinTexture"
            Provider.PLAYER_NAME -> "Player"
            else -> throw HeadIdProviderNotSupportByPluginException(provider, pluginName)
        }.let { section[it] = plugin.itemsManager.getHeadId(item, provider) }
    }

    private fun handlePotions(section: ConfigurationSection, meta: PotionMeta) {
        if (meta.hasColor()) {
            meta.color?.let { section.set("Options.Color", it.asString()) }
        }

        val effects = mutableListOf<String>()

        with (meta.basePotionData) {
            if (type.effectType != null) {
                effects.add("${type.effectType} -1 ${if (isUpgraded) 1 else 0}")
            }
        }

        if (meta.hasCustomEffects()) {
            effects.addAll(meta.customEffects.map { "${it.type.name} ${it.duration} ${it.amplifier + 1}" })
        }

        section.setList("PotionEffects", effects)
    }

}

private fun Attribute.name(): String? {
    return when (this) {
        Attribute.GENERIC_ATTACK_SPEED -> "AttackSpeed"
        Attribute.GENERIC_ARMOR_TOUGHNESS -> "ArmorToughness"
        Attribute.GENERIC_ATTACK_DAMAGE -> "Damage"
        Attribute.GENERIC_MAX_HEALTH -> "Health"
        Attribute.GENERIC_KNOCKBACK_RESISTANCE -> "KnockbackResistance"
        Attribute.GENERIC_LUCK -> "Luck"
        Attribute.GENERIC_MOVEMENT_SPEED -> "MovementSpeed"
        else -> null
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