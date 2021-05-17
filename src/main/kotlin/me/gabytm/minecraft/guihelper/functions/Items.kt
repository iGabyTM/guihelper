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
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.SpawnEggMeta
import org.bukkit.material.SpawnEgg
import java.util.*

private val leatherArmor =
    EnumSet.of(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS)

private val potions = if (ServerVersion.isOlderThan(ServerVersion.V1_9)) {
    EnumSet.of(Material.POTION)
} else {
    EnumSet.of(Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION)
}

private val fireworkStar = if (ServerVersion.isLegacy) Material.valueOf("FIREWORK_CHARGE") else Material.FIREWORK_STAR

/**
 * Gets a copy of the item the player is currently holding using the right method for each version
 * @since 1.1.0
 */
@Suppress("DEPRECATION")
val Player.hand: ItemStack
    get() {
        return if (ServerVersion.isOlderThan(ServerVersion.V1_9)) {
            inventory.itemInHand
        } else {
            inventory.itemInMainHand
        }
    }

/**
 * Whether the item is a banner or not
 * @since 1.1.0
 */
val ItemStack.isBanner: Boolean
    get() = if (ServerVersion.isLegacy) type.name == "BANNER" else Tag.ITEMS_BANNERS.isTagged(type)

/**
 * Whether the item is a firework star or not
 * @since 1.1.0
 */
val ItemStack.isFireworkStar: Boolean
    get() = type == fireworkStar

/**
 * Whether the item is a piece of leather armor or not
 * @see Material.LEATHER_HELMET, [Material.LEATHER_CHESTPLATE], [Material.LEATHER_LEGGINGS], [Material.LEATHER_BOOTS]
 * @since 1.1.0
 */
val ItemStack.isLeatherArmor: Boolean
    get() = leatherArmor.contains(type)

/**
 * Whether the item is a player head or not
 * @since 1.1.0
 */
@Suppress("DEPRECATION")
val ItemStack.isPlayerHead: Boolean
    get() {
        return if (ServerVersion.isLegacy) {
            type.name == "SKULL_ITEM" && durability == 3.toShort()
        } else {
            type == Material.PLAYER_HEAD
        }
    }

/**
 * Whether the item is a potion or not
 * @since 1.1.0
 */
val ItemStack.isPotion: Boolean
    get() = potions.contains(type)

/**
 * Whether the item is a spawn egg or not
 * @since 1.1.0
 */
val ItemStack.isSpawnEgg: Boolean
    get() = if (ServerVersion.isLegacy) type.name == "MONSTER_EGG" else type.name.endsWith("_SPAWN_EGG")

/**
 * Whether the item is unbreakable or not
 * @since 1.1.0
 */
val ItemStack.isUnbreakable: Boolean
    get() {
        return if (ServerVersion.isOlderThan(ServerVersion.V1_11)) {
            NBTItem(this).getBoolean("Unbreakable")
        } else {
            itemMeta?.isUnbreakable ?: false
        }
    }

/**
 * Item's displayName (if it has any) with [org.bukkit.ChatColor.COLOR_CHAR] replaced by &
 * @see [org.bukkit.inventory.meta.ItemMeta.getDisplayName]
 * @see [fixColors]
 * @since 1.1.0
 */
val ItemStack.displayName: String
    get() = itemMeta?.displayName?.fixColors() ?: ""

/**
 * Item's lore (if it has any) with [org.bukkit.ChatColor.COLOR_CHAR] replaced by &
 * @see [org.bukkit.inventory.meta.ItemMeta.getLore]
 * @see [fixColors]
 * @since 1.1.0
 */
val ItemStack.lore: List<String>
    get() = itemMeta?.lore?.map { it.fixColors() }?.toList() ?: emptyList()

/**
 * The [EntityType] associated with a spawn egg. Don't use this before checking if the item IS a spawn egg
 * @see [SpawnEgg.getSpawnedType], [SpawnEggMeta.getSpawnedType]
 * @see [isSpawnEgg]
 * @since 1.1.0
 */
@Suppress("DEPRECATION")
val ItemStack.spawnEggType: EntityType
    get() {
        return if (ServerVersion.isOlderThan(ServerVersion.V1_11)) {
            (data as SpawnEgg).spawnedType
        } else {
            (itemMeta as SpawnEggMeta).spawnedType
        }
    }

/**
 * The texture of a skull. Don't use this before checking if the item [isPlayerHead]
 * @since 1.1.0
 */
val ItemStack.skullTexture: String
    get() {
        val owner = NBTItem(this).getCompound("SkullOwner") ?: return ""
        return owner.getCompound("Properties").getCompoundList("textures")[0].getString("Value")
    }

/**
 * Item's custom model data, if it has any and the feature is supposed by the server version
 * @since 1.1.0
 */
val ItemStack.customModelData: Int
    get() {
        if (ServerVersion.isOlderThan(ServerVersion.V1_14)) {
            return 0
        }

        val meta = itemMeta ?: return 0
        return if (meta.hasCustomModelData()) meta.customModelData else 0
    }

val ItemStack?.isInvalid: Boolean
    get() = this == null || type == Material.AIR

fun <T> ItemStack.enchants(format: (Enchantment, Int) -> T): List<T> {
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