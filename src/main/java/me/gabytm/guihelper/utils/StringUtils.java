package me.gabytm.guihelper.utils;

import me.gabytm.guihelper.GUIHelper;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

public class StringUtils {
    private final static JavaPlugin PLUGIN = JavaPlugin.getProvidingPlugin(GUIHelper.class);

    public static void addToConfig(String path, Object value) { PLUGIN.getConfig().set(path, value); }

    public static String colorize(String text) { return ChatColor.translateAlternateColorCodes('&', text); }

    public static String formatEnchantmentName(Enchantment enchantment) {
        switch (enchantment.getName()) {
            case "ARROW_DAMAGE": return "&7Power";
            case "ARROW_FIRE": return "&7Flame";
            case "ARROW_INFINITE": return "&7Infinity";
            case "ARROW_KNOCKBACK": return "&7Punch";
            case "DAMAGE_ALL": return "&7Sharpness";
            case "DAMAGE_ARTHROPODS": return "&7Bane of Arthropods";
            case "DAMAGE_UNDEAD": return "&7Smite";
            case "DIG_SPEED": return "&7Efficiency";
            case "DURABILITY": return "&7Unbreaking";
            case "FIRE_ASPECT": return "&7Fire Aspect";
            case "KNOCKBACK": return "&7Knockback";
            case "LOOT_BONUS_BLOCKS": return "&7Fortune";
            case "LOOT_BONUS_MOBS": return "&7Looting";
            case "LUCK": return "&7Luck of the Sea";
            case "LURE": return "&7Lure";
            case "OXYGEN": return "&7Respiration";
            case "PROTECTION_ENVIRONMENTAL": return "&7Protection";
            case "PROTECTION_EXPLOSIONS": return "&7Blast Protection";
            case "PROTECTION_FALL": return "&7Feather Falling";
            case "PROTECTION_FIRE": return "&7Fire Protection";
            case "PROTECTION_PROJECTILE": return "&7Projectile Protection";
            case "SILK_TOUCH": return "&7Silk Touch";
            case "THORNS": return "&7Thorns";
            case "WATER_WORKER": return "&7Aqua Affinity";
            default: return "&7Unknown";
        }
    }
}
