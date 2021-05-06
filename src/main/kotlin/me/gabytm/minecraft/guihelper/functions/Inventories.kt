package me.gabytm.minecraft.guihelper.functions

import org.bukkit.Material
import org.bukkit.inventory.Inventory

/**
 * Whether the inventory is empty or not
 * @since 1.1.0
 */
val Inventory.isNotEmpty: Boolean
    get() = contents.asSequence().any { it != null && it.type != Material.AIR }