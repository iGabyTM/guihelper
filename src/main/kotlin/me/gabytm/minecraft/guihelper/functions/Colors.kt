package me.gabytm.minecraft.guihelper.functions

import org.bukkit.Bukkit
import org.bukkit.Color

private val defaultLeatherColor = Bukkit.getItemFactory().defaultLeatherColor

/**
 * Check if the color is the [defaultLeatherColor]
 * @since 1.1.0
 */
val Color.isDefaultLeatherColor: Boolean
    get() = this == defaultLeatherColor