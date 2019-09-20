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
            case "BINDING_CURSE": return "&cCurse of Binding";
            case "CHANNELING": return "&7Channeling";
            case "DAMAGE_ALL": return "&7Sharpness";
            case "DAMAGE_ARTHROPODS": return "&7Bane of Arthropods";
            case "DAMAGE_UNDEAD": return "&7Smite";
            case "DEPTH_STRIDER": return "&7Depth Strider";
            case "DIG_SPEED": return "&7Efficiency";
            case "DURABILITY": return "&7Unbreaking";
            case "FIRE_ASPECT": return "&7Fire Aspect";
            case "FROST_WALKER": return "&7Frost Walker";
            case "IMPALING": return "&7Impaling";
            case "KNOCKBACK": return "&7Knockback";
            case "LOOT_BONUS_BLOCKS": return "&7Fortune";
            case "LOOT_BONUS_MOBS": return "&7Looting";
            case "LOYALTY": return "&7Loyality";
            case "LUCK": return "&7Luck of the Sea";
            case "LURE": return "&7Lure";
            case "MENDING": return "&7Mending";
            case "MULTISHOT": return "&7Multishot";
            case "OXYGEN": return "&7Respiration";
            case "PIERCING": return "&7Piercing";
            case "PROTECTION_ENVIRONMENTAL": return "&7Protection";
            case "PROTECTION_EXPLOSIONS": return "&7Blast Protection";
            case "PROTECTION_FALL": return "&7Feather Falling";
            case "PROTECTION_FIRE": return "&7Fire Protection";
            case "PROTECTION_PROJECTILE": return "&7Projectile Protection";
            case "QUICK_CHARGE": return "&7Quick Charge";
            case "RIPTIDE": return "&7Riptide";
            case "SILK_TOUCH": return "&7Silk Touch";
            case "SWEEPING_EDGE": return "&7Sweeping Edge";
            case "THORNS": return "&7Thorns";
            case "VANISHING_CURSE": return "&cCurse of Vanishing";
            case "WATER_WORKER": return "&7Aqua Affinity";
            default: return "&7Unknown";
        }
    }
}
