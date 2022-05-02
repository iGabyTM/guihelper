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

@file:JvmName("Items")

package me.gabytm.minecraft.guihelper.functions

import de.tr7zw.changeme.nbtapi.NBTItem
import me.gabytm.minecraft.guihelper.utils.ServerVersion
import org.bukkit.Bukkit
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.block.Banner
import org.bukkit.block.banner.Pattern
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.*
import org.bukkit.material.SpawnEgg
import org.bukkit.potion.Potion
import java.util.*
import kotlin.jvm.Throws

private val leatherArmor =
    EnumSet.of(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS)

private val potions = if (ServerVersion.IS_ANCIENT) {
    EnumSet.of(Material.POTION)
} else {
    EnumSet.of(Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION)
}

private val fireworkRocket = if (ServerVersion.IS_LEGACY) Material.valueOf("FIREWORK") else Material.FIREWORK_ROCKET
private val fireworkStar = if (ServerVersion.IS_LEGACY) Material.valueOf("FIREWORK_CHARGE") else Material.FIREWORK_STAR

/**
 * Gets a copy of the item the player is currently holding using the right method for each version
 * @since 2.0.0
 */
@Suppress("DEPRECATION")
val Player.hand: ItemStack
    get() {
        return if (ServerVersion.IS_ANCIENT) {
            inventory.itemInHand
        } else {
            inventory.itemInMainHand
        }
    }

/**
 * Whether the item is a banner or not
 * @since 2.0.0
 */
val ItemStack.isBanner: Boolean
    get() = if (ServerVersion.IS_LEGACY) type.name == "BANNER" else Tag.ITEMS_BANNERS.isTagged(type)

/**
 * Whether the item is a firework or not
 * @since 2.0.0
 */
val ItemStack.isFirework: Boolean
    get() = type == fireworkRocket

/**
 * Whether the item is a firework star or not
 * @since 2.0.0
 */
val ItemStack.isFireworkStar: Boolean
    get() = type == fireworkStar

/**
 * Whether the item is a piece of leather armor or not
 * @see Material.LEATHER_HELMET, [Material.LEATHER_CHESTPLATE], [Material.LEATHER_LEGGINGS], [Material.LEATHER_BOOTS]
 * @since 2.0.0
 */
val ItemStack.isLeatherArmor: Boolean
    get() = leatherArmor.contains(type)

/**
 * Whether the item is a player head or not
 * @since 2.0.0
 */
@Suppress("DEPRECATION")
val ItemStack.isPlayerHead: Boolean
    get() {
        return if (ServerVersion.IS_LEGACY) {
            type.name == "SKULL_ITEM" && durability == 3.toShort()
        } else {
            type == Material.PLAYER_HEAD
        }
    }

/**
 * Whether the item is a potion or not
 * @since 2.0.0
 */
val ItemStack.isPotion: Boolean
    get() = potions.contains(type)

/**
 * Whether the item is a shield or not
 * @since 2.0.0
 */
val ItemStack.isShield: Boolean
    get() = !ServerVersion.IS_ANCIENT && type == Material.SHIELD

/**
 * Whether the item is a spawn egg or not
 * @since 2.0.0
 */
val ItemStack.isSpawnEgg: Boolean
    get() = if (ServerVersion.IS_LEGACY) type.name == "MONSTER_EGG" else type.name.endsWith("_SPAWN_EGG")

/**
 * Whether the item is a splash potion or not
 * @since 2.0.0
 */
@Suppress("DEPRECATION")
val ItemStack.isSplashPotion: Boolean
    get() = if (ServerVersion.IS_ANCIENT) Potion.fromItemStack(this).isSplash else type == Material.SPLASH_POTION

/**
 * Whether the item is unbreakable or not
 * @since 2.0.0
 */
val ItemStack.isUnbreakable: Boolean
    get() {
        return if (ServerVersion.ITEM_META_HAS_UNBREAKABLE) {
            itemMeta?.isUnbreakable ?: false
        } else {
            NBTItem(this).getBoolean("Unbreakable")
        }
    }

val ItemStack.meta: ItemMeta?
    get() = itemMeta ?: Bukkit.getItemFactory().getItemMeta(type)

/**
 * The [EntityType] associated with a spawn egg. Don't use this before checking if the item IS a spawn egg
 * @see [SpawnEgg.getSpawnedType], [SpawnEggMeta.getSpawnedType]
 * @see [isSpawnEgg]
 * @since 2.0.0
 */
@Suppress("DEPRECATION")
val ItemStack.spawnEggType: EntityType
    get() {
        return if (ServerVersion.HAS_SPAWN_EGG_META) {
            if (!hasItemMeta()) {
                return EntityType.UNKNOWN
            }

            (itemMeta as SpawnEggMeta).spawnedType ?: EntityType.UNKNOWN
        } else {
            (data as SpawnEgg).spawnedType
        }
    }

/**
 * The texture of a skull. Don't use this before checking if the item [isPlayerHead]
 * @since 2.0.0
 */
val ItemStack.skullTexture: String
    get() {
        val owner = NBTItem(this).getCompound("SkullOwner") ?: return ""
        return owner.getCompound("Properties").getCompoundList("textures")[0].getString("Value")
    }

/**
 * Item's custom model data, if it has any and the feature is supposed by the server version
 * @since 2.0.0
 */
val ItemStack.customModelData: Int
    get() {
        if (ServerVersion.HAS_CUSTOM_MODEL_DATA) {
            val meta = itemMeta ?: return 0
            return if (meta.hasCustomModelData()) meta.customModelData else 0
        }

        return 0
    }

/**
 * Item's displayName (if it has any) with [org.bukkit.ChatColor.COLOR_CHAR] replaced by &
 * @param format the format for RGB (only used on 1.14+)
 * @return displayName or empty string
 *
 * @see [org.bukkit.inventory.meta.ItemMeta.getDisplayName]
 * @see [fixColors]
 * @since 2.0.0
 */
fun ItemStack.displayName(format: ((rgb: String) -> String) = SPIGOT_RGB_FORMAT): String {
    return itemMeta?.displayName?.fixColors(format) ?: ""
}

fun <T> ItemStack.enchants(format: (enchantment: Enchantment, level: Int) -> T): List<T> {
    if (!hasItemMeta()) {
        return emptyList()
    }

    val meta = itemMeta ?: return emptyList()
    val list = enchantments.map { format(it.key, it.value) }.toMutableList()

    if (meta is EnchantmentStorageMeta) {
        list.addAll(meta.storedEnchants.map { format(it.key, it.value) })
    }

    return list
}

/**
 * Item's lore (if it has any) with [org.bukkit.ChatColor.COLOR_CHAR] replaced by &
 * @param format the format for RGB (only used on 1.14+)
 * @return lore or empty list
 *
 * @see [org.bukkit.inventory.meta.ItemMeta.getLore]
 * @see [fixColors]
 * @since 2.0.0
 */
fun ItemStack.lore(format: ((rgb: String) -> String) = SPIGOT_RGB_FORMAT): List<String> {
    return itemMeta?.lore?.map { it.fixColors(format) }?.toList() ?: emptyList()
}

@Suppress("DEPRECATION")
@Throws(java.lang.IllegalArgumentException::class)
fun ItemStack.patternsAndBaseColor(check: Boolean): Pair<List<Pattern>, DyeColor?> {
    if (check && (!isShield && !isBanner)) {
        throw IllegalArgumentException("Item is not a SHIELD or BANNER but $type")
    }

    val meta = itemMeta ?: return Pair(emptyList(), null)

    // https://github.com/EssentialsX/Essentials/pull/745#issuecomment-234843795
    return if (isShield) {
        val state = (meta as BlockStateMeta).blockState as Banner
        state.patterns to state.baseColor
    } else {
        meta as BannerMeta
        meta.patterns to meta.baseColor
    }
}