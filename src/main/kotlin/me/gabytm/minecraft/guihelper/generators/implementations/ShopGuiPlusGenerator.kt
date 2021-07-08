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
import me.gabytm.minecraft.guihelper.items.heads.exceptions.HeadIdProviderNotSupportByPluginException
import me.gabytm.minecraft.guihelper.items.heads.providers.HeadIdProvider.Provider
import me.gabytm.minecraft.guihelper.utils.Message
import me.gabytm.minecraft.guihelper.utils.VersionHelper
import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Comment
import me.mattstudios.config.annotations.Description
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property.create
import org.apache.commons.cli.CommandLine
import org.apache.commons.lang.WordUtils
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.*
import kotlin.system.measureTimeMillis

class ShopGuiPlusGenerator(
    private val plugin: GUIHelper,
    override val pluginName: String = "ShopGUIPlus",
    override val pluginVersion: String = "1.59.2",
    override val rgbFormat: (String) -> String = { "#$it" }
) : ConfigGenerator() {

    private val defaults = Defaults(pluginName)
    // TODO: add nbt support
    /*
    private val ignoredNbtTags = setOf(
        "Damage", // durability
        "Enchantments",
        "display", // lore, display name and color
        "SkullOwner", // texture
        //"Patterns", // banner and shield patterns
        "CustomPotionEffects"
    )*/

    init {
        options.addOption(createHeadsOption(Provider.BASE_64, Provider.HEAD_DATABASE, Provider.PLAYER_NAME))
        options.addOption(createPageOption("The page where items will be set"))
    }

    override fun getMessage() = "  &2$pluginName &av$pluginVersion &8- &fIsland mini shop items"

    override fun generate(context: GeneratorContext, input: CommandLine): Boolean {
        val page = input.getOrDefault('p', 1) { it.toIntOrNull() }
        val config = Config(pluginName, plugin, true)

        val duration = measureTimeMillis {
            context.forEach { item, slot ->
                createItem(config.createSection("GUIHelper.items.P$page-$slot"), input, item, slot, page)
            }
        }

        config.save()
        Message.GENERATION_DONE.send(context, config.path, duration)
        return true
    }

    @Suppress("DEPRECATION")
    private fun createItem(section: ConfigurationSection, input: CommandLine, item: ItemStack, slot: Int, page: Int) {
        val itemSection = section.createSection("item")

        section["type"] = "item"

        itemSection["material"] = item.type.name
        itemSection.set("damage", item.durability) { it > 0 }
        itemSection["amount"] = item.amount

        section["slot"] = slot
        section["page"] = page
        section.set("unstack", defaults[Value.UNSTACK]) { it }
        section.set("stacked", defaults[Value.STACKED]) { !it }
        section["buyPrice"] = defaults[Value.BUY_PRICE]
        section["sellPrice"] = defaults[Value.SELL_PRICE]

        val meta = item.meta ?: return

        itemSection.set("model", item.customModelData) { it > 0 }
        itemSection.set("unbreakable", item.isUnbreakable) { it }
        itemSection.set("name", meta::hasDisplayName) { item.displayName(rgbFormat) }
        itemSection.set("lore", meta::hasLore) { item.lore(rgbFormat) }
        itemSection.setList("flags", meta.itemFlags.map { it.name })
        itemSection.setList("enchantments", item.enchants { enchant, level -> "${enchant.name}:$level" })
        setMetaSpecificValues(itemSection, input, item, meta)

        // TODO: add nbt support
        /*
        if (defaults[Value.NBT]) {
            handleNbt(itemSection, item)
        }*/
    }

    private fun setMetaSpecificValues(section: ConfigurationSection, input: CommandLine, item: ItemStack, meta: ItemMeta) {
        when {
            item.isShield || item.isBanner -> {
                handleBannersAndShields(section, item)
            }
            item.isFirework -> {
                handleFireworks(section, meta as FireworkMeta)
            }
            item.isFireworkStar -> {
                handleFireworkStars(section, meta as FireworkEffectMeta)
            }
            item.isLeatherArmor -> {
                (meta as LeatherArmorMeta).color.ifNotDefault { section["color"] = it.nameOrString() }
            }
            item.isPlayerHead -> {
                handlePlayerHeads(section, item, input.getHeadIdProvider(default = defaults[Value.SETTINGS__HEADS]))
            }
            !VersionHelper.IS_ANCIENT && item.isPotion -> {
                handlePotions(section.createSection("potion"), meta as PotionMeta)
            }
            item.isSpawnEgg -> {
                handleSpawnEggs(section, item)
            }
        }
    }

    private fun handleBannersAndShields(section: ConfigurationSection, item: ItemStack) {
        val (patterns, color) = item.patternsAndBaseColor(false)

        if (VersionHelper.IS_LEGACY || item.isShield) {
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
                else -> throw HeadIdProviderNotSupportByPluginException(provider, pluginName)
            }.let { section[it] = plugin.itemsManager.getHeadId(item, provider) }
        } catch (e: IllegalArgumentException) {
            plugin.logger.warning(e.message)
        }
    }

    // See https://docs.brcdev.net/#/item-meta?id=_19-116
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

    // See https://docs.brcdev.net/#/item-meta?id=spawn-eggs
    private fun handleSpawnEggs(section: ConfigurationSection, item: ItemStack) {
        if (VersionHelper.HAS_SPAWN_EGG_META) {
            section["mob"] = item.spawnEggType.name
        } else {
            section["mob"] = item.spawnEggType.asString()
        }
    }

    /*
    // See https://docs.brcdev.net/#/item-meta?id=nbt-tags
    private fun handleNbt(section: ConfigurationSection, item: ItemStack) {
        val nbt = NBTItem(item)

        if (!nbt.hasNBTData()) {
            return
        }

        val nbtSection = section.createSection("nbt")

        for (key in nbt.keys) {
            if (ignoredNbtTags.contains(key)) {
                println("[-] $key is ignored")
                continue
            }

            val compoundSection = nbtSection.createSection(key)
            val type = nbt.getType(key) ?: continue

            println("[?] Processing $key...")

            when (type) {
                NBTType.NBTTagCompound -> setNbtCompound(compoundSection, nbt.getCompound(key))
                else -> {
                    val (actualType, value) = nbt[type, key]
                    println("[+] $key type is ${actualType.asString()}")
                    setNbtTag(compoundSection, actualType, key, value)
                }
            }
        }
    }

    @Beta
    private fun setNbtTag(section: ConfigurationSection, nbtType: NBTType, key: String, value: Any?) {
        if (value == null) {
            return
        }

        // TODO: Come back to this when support for compound list is added
        if (value is NBTCompoundList) {
            return
        }

        section["type"] = nbtType.asString()
        section["key"] = key
        section["value"] = value
    }

    // TODO: Come back to this when support for compound list is added
    private fun handleNBTCompoundList(section: ConfigurationSection, list: NBTCompoundList) {
        for ((index, entry) in list.withIndex()) {
            val childrenSection = section.createSection("$index.children")

            for (key in entry.keys) {
                val type = entry.getType(key)
                val keySection = childrenSection.createSection(key)

                when (type) {
                    NBTType.NBTTagCompound -> setNbtCompound(keySection, entry.getCompound(key))
                    else -> {
                        val (realType, value) = entry[type, key]
                        setNbtTag(keySection, realType, key, value)
                    }
                }
            }
        }
    }

    @Beta
    private fun setNbtCompound(section: ConfigurationSection, compound: NBTCompound) {
        for (key in compound.keys) {
            val keySection = section.createSection(key)

            when (val type = compound.getType(key) ?: continue) {
                NBTType.NBTTagCompound -> setNbtCompound(keySection, compound.getCompound(key))
                else -> {
                    val (realType, value) = compound[type, key]
                    setNbtTag(keySection, realType, key, value)
                }
            }
        }
    }
     */
    private class Defaults(name: String) : DefaultValues(name, Value::class.java)

    @Description(
        " ",
        "Default values that will be used in the config creation process",
        " ",
        "▪ ShopGUIPlus 1.59.2 by brc (https://spigotmc.org/resources/6515/)",
        "▪ Wiki: https://docs.brcdev.net/#/shopgui/faq",
        " "
    )
    private object Value : SettingsHolder {

        @Comment("Default format used for player heads, available options: BASE_64, HEAD_DATABASE, PLAYER_NAME")
        @Path("settings.heads")
        val SETTINGS__HEADS = create(Provider.BASE_64)

        @Path("buyPrice")
        val BUY_PRICE = create(10.0)

        @Path("sellPrice")
        val SELL_PRICE = create(10.0)

        @Comment("https://docs.brcdev.net/#/shopgui/stack-size?id=unstack")
        @Path("unstack")
        val UNSTACK = create(false)

        @Comment("https://docs.brcdev.net/#/shopgui/stack-size?id=stacked")
        @Path("stacked")
        val STACKED = create(true)

        // TODO: add nbt support
        /*
        @Comment("https://docs.brcdev.net/#/item-meta?id=nbt-tags")
        @Path("nbt")
        val NBT = create(false)
         */
    }

}

/**
 * See the 'Minecraft 1.7-1.10' tab on [docs.brcdev.net/#/entity-types](https://docs.brcdev.net/#/entity-types)
 */
private fun EntityType.asString(): String {
    return WordUtils.capitalizeFully(name.replace('_', ' ')).replace(" ", "")
}

// TODO: add nbt support
/*
private operator fun NBTCompound.get(type: NBTType, key: String): Pair<NBTType, Any?> {
    if (type == NBTType.NBTTagList) {
        val listType = getListType(key)

        return listType to when (listType) {
            NBTType.NBTTagCompound -> null // getCompoundList(key)

            NBTType.NBTTagByteArray -> getByteArray(key)
            NBTType.NBTTagDouble -> getDoubleList(key)
            NBTType.NBTTagFloat -> getFloatList(key)
            NBTType.NBTTagInt -> getIntegerList(key)
            NBTType.NBTTagIntArray -> getIntArray(key)
            NBTType.NBTTagLong -> getLongList(key)

            else -> getStringList(key)
        }
    }

    return type to when (type) {
        NBTType.NBTTagCompound -> getCompound(key)

        NBTType.NBTTagByte -> getByte(key)
        NBTType.NBTTagByteArray -> getByteArray(key)
        NBTType.NBTTagDouble -> getDouble(key)
        NBTType.NBTTagFloat -> getFloat(key)
        NBTType.NBTTagInt -> getInteger(key)
        NBTType.NBTTagIntArray -> getIntArray(key)
        NBTType.NBTTagLong -> getLong(key)
        NBTType.NBTTagShort -> getShort(key)
        NBTType.NBTTagString -> getString(key)

        NBTType.NBTTagEnd -> getString(key)
        else -> getCompound(key)
    }
}

private fun NBTType.asString(): String {
    return when (this) {
        NBTType.NBTTagByte -> "BYTE"
        NBTType.NBTTagByteArray -> "BYTE_ARRAY"
        NBTType.NBTTagCompound -> "COMPOUND"
        NBTType.NBTTagFloat -> "FLOAT"
        NBTType.NBTTagDouble -> "DOUBLE"
        NBTType.NBTTagInt -> "INT"
        NBTType.NBTTagIntArray -> "INT_ARRAY"
        NBTType.NBTTagLong -> "LONG"
        NBTType.NBTTagString -> "STRING"
        else -> name
    }
}
*/