package me.gabytm.minecraft.guihelper.functions

import de.tr7zw.changeme.nbtapi.NBTItem
import me.gabytm.minecraft.guihelper.utils.ServerVersion
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SpawnEggMeta
import org.bukkit.material.SpawnEgg
import java.util.*

private val leatherArmor =
    EnumSet.of(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS)

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
 * Whether the item is a piece of leather armor or not
 * @see Material.LEATHER_HELMET, [Material.LEATHER_CHESTPLATE], [Material.LEATHER_LEGGINGS], [Material.LEATHER_BOOTS]
 * @since 1.1.0
 */
val ItemStack.isLeatherArmor: Boolean
    get() = leatherArmor.contains(type)

/**
 * Whether the item is a spawn egg or not
 * @since 1.1.0
 */
val ItemStack.isSpawnEgg: Boolean
    get() = if (ServerVersion.isLegacy) type.name == "MONSTER_EGG" else type.name.endsWith("_SPAWN_EGG")

/**
 * Whether the item is a player skull or not
 * @since 1.1.0
 */
@Suppress("DEPRECATION")
val ItemStack.isPlayerSkull: Boolean
    get() {
        return if (ServerVersion.isLegacy) {
            type.name == "SKULL_ITEM" && durability == 3.toShort()
        } else {
            type == Material.PLAYER_HEAD
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
 * The texture of a skull. Don't use this before checking if the item [isPlayerSkull]
 * @since 1.1.0
 */
val ItemStack.skullTexture: String
    get() {
        return NBTItem(this)
            .getCompound("SkullOwner")
            .getCompound("Properties")
            .getCompoundList("textures")[0]
            .getString("Value")
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

        return NBTItem(this).getInteger("CustomModelData")
    }

val ItemStack?.isInvalid: Boolean
    get() = this == null || type == Material.AIR