/*
 * Copyright 2020 GabyTM
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

package me.gabytm.guihelper.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;

import java.util.stream.Stream;

public final class StringUtil {
    public static String color(final String line) {
        return ChatColor.translateAlternateColorCodes('&', line);
    }

    public static void consoleText(final ConsoleCommandSender ccs, final String version) {
        Stream.of(
                "&2 _____   _____   ",
                "&2|   __| |  |  |  &fGUIHelper &av" + version + " &fby &aGabyTM",
                "&2|  |  | |     |  &7The ultimate solution for creating professional GUIs",
                "&2|_____| |__|__|  "
        ).forEach(line -> ccs.sendMessage(color(line)));
    }

    public static String formatEnchantmentName(final Enchantment enchantment) {
        switch (enchantment.getName()) {
            case "ARROW_DAMAGE":
                return "&7Power";
            case "ARROW_FIRE":
                return "&7Flame";
            case "ARROW_INFINITE":
                return "&7Infinity";
            case "ARROW_KNOCKBACK":
                return "&7Punch";
            case "BINDING_CURSE":
                return "&cCurse of Binding";
            case "CHANNELING":
                return "&7Channeling";
            case "DAMAGE_ALL":
                return "&7Sharpness";
            case "DAMAGE_ARTHROPODS":
                return "&7Bane of Arthropods";
            case "DAMAGE_UNDEAD":
                return "&7Smite";
            case "DEPTH_STRIDER":
                return "&7Depth Strider";
            case "DIG_SPEED":
                return "&7Efficiency";
            case "DURABILITY":
                return "&7Unbreaking";
            case "FIRE_ASPECT":
                return "&7Fire Aspect";
            case "FROST_WALKER":
                return "&7Frost Walker";
            case "IMPALING":
                return "&7Impaling";
            case "KNOCKBACK":
                return "&7Knockback";
            case "LOOT_BONUS_BLOCKS":
                return "&7Fortune";
            case "LOOT_BONUS_MOBS":
                return "&7Looting";
            case "LOYALTY":
                return "&7Loyality";
            case "LUCK":
                return "&7Luck of the Sea";
            case "LURE":
                return "&7Lure";
            case "MENDING":
                return "&7Mending";
            case "MULTISHOT":
                return "&7Multishot";
            case "OXYGEN":
                return "&7Respiration";
            case "PIERCING":
                return "&7Piercing";
            case "PROTECTION_ENVIRONMENTAL":
                return "&7Protection";
            case "PROTECTION_EXPLOSIONS":
                return "&7Blast Protection";
            case "PROTECTION_FALL":
                return "&7Feather Falling";
            case "PROTECTION_FIRE":
                return "&7Fire Protection";
            case "PROTECTION_PROJECTILE":
                return "&7Projectile Protection";
            case "QUICK_CHARGE":
                return "&7Quick Charge";
            case "RIPTIDE":
                return "&7Riptide";
            case "SILK_TOUCH":
                return "&7Silk Touch";
            case "SWEEPING_EDGE":
                return "&7Sweeping Edge";
            case "THORNS":
                return "&7Thorns";
            case "VANISHING_CURSE":
                return "&cCurse of Vanishing";
            case "WATER_WORKER":
                return "&7Aqua Affinity";
            default:
                return "&7Unknown";
        }
    }
}
