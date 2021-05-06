package me.gabytm.minecraft.guihelper.functions

import org.apache.commons.lang.StringUtils
import org.bukkit.ChatColor

fun String.color(): String = ChatColor.translateAlternateColorCodes('&', this)

fun String.fixColors(): String = StringUtils.replaceChars(this, ChatColor.COLOR_CHAR, '&')