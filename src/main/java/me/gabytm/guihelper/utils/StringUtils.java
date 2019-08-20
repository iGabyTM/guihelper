package me.gabytm.guihelper.utils;

import org.bukkit.ChatColor;

public class StringUtils {
    public static String colorize(String text) { return ChatColor.translateAlternateColorCodes('&', text); }
}
