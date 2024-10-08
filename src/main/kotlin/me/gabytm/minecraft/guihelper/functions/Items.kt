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
import me.gabytm.minecraft.guihelper.util.Reflections
import me.gabytm.minecraft.guihelper.util.ServerVersion
import org.bukkit.*
import org.bukkit.block.Banner
import org.bukkit.block.banner.Pattern
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.*
import org.bukkit.material.SpawnEgg
import java.util.*

private val leatherArmor = EnumSet.copyOf(Material.values().filter { it.name.startsWith("LEATHER_") }.toSet())

private val potions = if (ServerVersion.IS_ANCIENT) {
    EnumSet.of(Material.POTION)
} else {
    EnumSet.of(Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION)
}

private val fireworkRocket = if (ServerVersion.IS_LEGACY) Material.valueOf("FIREWORK") else Material.FIREWORK_ROCKET
private val fireworkStar = if (ServerVersion.IS_LEGACY) Material.valueOf("FIREWORK_CHARGE") else Material.FIREWORK_STAR

private const val materialSpawnEggPrefix = "_SPAWN_EGG"
private val entityTypeByMaterial = if (ServerVersion.IS_LEGACY) {
	emptyMap<Material, EntityType>()
} else {
	Material.values()
		.filter(Material::isSpawnEgg)
		.associateWith { material ->
			@Suppress("DEPRECATION")
			EntityType.fromName(material.name.replace(materialSpawnEggPrefix, ""))
		}
}

private val dyeColorByMaterial: Map<Material, DyeColor> = if (ServerVersion.IS_LEGACY) {
	emptyMap()
} else {
	Material.values()
		.filter(Tag.ITEMS_BANNERS::isTagged)
		.associateWith { material ->
			DyeColor.valueOf(material.name.removeSuffix("_BANNER"))
		}
}

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
 * Whether the material is a spawn egg (1.13+)
 * @since 2.0.0
 */
val Material.isSpawnEgg: Boolean
	get() = name.endsWith(materialSpawnEggPrefix)

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
 * @see Material.LEATHER_HELMET, [Material.LEATHER_CHESTPLATE], [Material.LEATHER_LEGGINGS], [Material.LEATHER_BOOTS], [Material.LEATHER_HORSE_ARMOR]
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
    get() = if (ServerVersion.IS_LEGACY) type.name == "MONSTER_EGG" else type.isSpawnEgg

/**
 * Whether the item is a splash potion or not
 * @since 2.0.0
 */
val ItemStack.isSplashPotion: Boolean
    get() = if (ServerVersion.IS_ANCIENT) Reflections.isSplashPotion(this) else type == Material.SPLASH_POTION

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
		return when {
			!ServerVersion.IS_LEGACY -> entityTypeByMaterial[this.type]

			ServerVersion.HAS_SPAWN_EGG_META -> {
				if (!hasItemMeta()) {
					return EntityType.UNKNOWN
				}

				(this.itemMeta as SpawnEggMeta).spawnedType
			}

			else -> (this.data as SpawnEgg).spawnedType
		} ?: EntityType.UNKNOWN
    }

/**
 * The texture of a skull. Don't use this before checking if the item [isPlayerHead]
 * @since 2.0.0
 */
val ItemStack.skullTexture: String?
    get() {
		if (ServerVersion.HAS_PROFILE_API) {
			val profile = (meta as SkullMeta).ownerProfile ?: return null
			val textureUrl = profile.textures.skin ?: return null
			val json = "{\"textures\":{\"SKIN\":{\"url\":\"$textureUrl\"}}}"
			return Base64.getEncoder().encodeToString(json.encodeToByteArray())
		}

        val owner = NBTItem(this).getCompound("SkullOwner") ?: return null
        return owner.getCompound("Properties")?.getCompoundList("textures")?.get(0)?.getString("Value") ?: return null
    }

/**
 * Safe access to the Potion color that was introduced in 1.11
 * @since 2.0.0
 */
val PotionMeta.potionColor: Color?
	get() {
		return if (ServerVersion.POTION_META_HAS_COLOR && this.hasColor()) {
			this.color
		} else {
			null
		}
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
 * Check if an item is not null and its `type` it not [Material.AIR]
 * @return whether the item is not null
 * @since 2.0.0
 */
fun ItemStack?.isNotNull(): Boolean = this != null && type != Material.AIR

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

/**
 * Get item's enchantments formatted as you need
 * Example:
 * ```
 * // A list formatted as "ENCHANTMENT;level"
 * item.enchants { (e, level) -> "${e.name};$level" }
 * ```
 * @param format the format that will be applied to each <Enchantment, Level> pair
 * @return list of formatted enchantments
 * @since 2.0.0
 */
fun <T> ItemStack.enchants(format: (enchantment: Enchantment, level: Int) -> T): List<T> {
    // If the item has enchantments, it also has meta
    if (!hasItemMeta()) {
        return emptyList()
    }

    val meta = itemMeta ?: return emptyList()
    val list = enchantments.map { format(it.key, it.value) }.toMutableList()

    // Include the extra enchantments if the item is an enchanted book
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

@Throws(java.lang.IllegalArgumentException::class)
fun ItemStack.patternsAndBaseColor(check: Boolean): Pair<List<Pattern>, DyeColor?> {
    if (check && (!isShield && !isBanner)) {
        throw IllegalArgumentException("Item is not a SHIELD or BANNER but $type")
    }

    if (!hasItemMeta()) {
        return Pair(emptyList(), null)
    }

    val meta = itemMeta ?: return Pair(emptyList(), null)

    // https://github.com/EssentialsX/Essentials/pull/745#issuecomment-234843795
    if (isShield) {
        val state = (meta as BlockStateMeta).blockState as Banner
        return state.patterns to state.baseColor
    }

	meta as BannerMeta

	return if (ServerVersion.IS_LEGACY) {
		meta.patterns to Reflections.bannerMetaGetBaseColor(meta)
	} else {
		meta.patterns to dyeColorByMaterial[this.type]
	}
}
