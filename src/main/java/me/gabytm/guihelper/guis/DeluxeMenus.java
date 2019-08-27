/*
 * Copyright 2019 GabyTM
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

package me.gabytm.guihelper.guis;

import me.gabytm.guihelper.GUIHelper;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

import static me.gabytm.guihelper.utils.StringUtils.*;

public class DeluxeMenus {
    private GUIHelper plugin;

    DeluxeMenus(GUIHelper plugin) {
        this.plugin = plugin;
    }

    /**
     * Generate a DeluxeMenus external menu
     * @param gui the gui from where the items are took
     * @param player the command sender
     */
    public void externalMenu(Inventory gui, Player player) {
        try {
            long start = System.currentTimeMillis();

            for (String key : plugin.getConfig().getKeys(false)) {
                plugin.getConfig().set(key, null);
            }

            plugin.getConfig().createSection("items");

            for (int s = 0; s < gui.getSize(); s++) {
                if (gui.getItem(s) != null && gui.getItem(s).getType() != Material.AIR) {
                    ItemStack item = gui.getItem(s);
                    ItemMeta meta = item.getItemMeta();

                    plugin.getConfig().set("items." + s + ".material", item.getType().toString());

                    if (item.getDurability() > 0) plugin.getConfig().set("items." + s + ".data", item.getDurability());

                    if (item.getAmount() > 1) plugin.getConfig().set("items." + s + ".amount", item.getAmount());

                    if (item.getType().equals(Material.LEATHER_HELMET) || item.getType().equals(Material.LEATHER_CHESTPLATE) || item.getType().equals(Material.LEATHER_LEGGINGS) || item.getType().equals(Material.LEATHER_BOOTS)) {
                        LeatherArmorMeta armorMeta = (LeatherArmorMeta) item.getItemMeta();

                        plugin.getConfig().set("items." + s + ".color", armorMeta.getColor().getRed() + ", " + armorMeta.getColor().getGreen() + ", " + armorMeta.getColor().getBlue());
                    }

                    /*if (item.getType().equals(Material.BANNER)) {
                        BannerMeta bannerMeta = (BannerMeta) item.getItemMeta();
                        List<String> banner_meta = new ArrayList<>();

                        if (bannerMeta.getPatterns().size() > 0) {
                            for (Pattern pattern : bannerMeta.getPatterns()) {
                                banner_meta.add(pattern.getColor() + ";" + pattern.getPattern());
                            }

                            plugin.getConfig().set("items." + s + ".banner_meta", banner_meta);
                        }
                    } */

                    plugin.getConfig().set("items." + s + ".slot", s);

                    if (meta.hasDisplayName()) plugin.getConfig().set("items." + s + ".display_name", meta.getDisplayName().replaceAll("§", "&"));

                    if (meta.hasLore()) {
                        List<String> lore = new ArrayList<>();

                        for (String line : meta.getLore()) {
                            lore.add(line.replaceAll("§", "&"));
                        }

                        plugin.getConfig().set("items." + s + ".lore", lore);
                    }

                    if (meta.getEnchants().size() > 0) {
                        List<String> enchantments = new ArrayList<>();

                        for (Enchantment en : meta.getEnchants().keySet()) {
                            enchantments.add(en.getName() + ";" + meta.getEnchantLevel(en));
                        }

                        plugin.getConfig().set("items." + s + ".enchantments", enchantments);
                    }
                }
            }

            plugin.saveConfig();
            player.sendMessage(colorize("&a[GH] Done! &7(" + (System.currentTimeMillis() - start) + "ms)"));
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(colorize("&c[GH] Something went wrong, please check the console."));
        }
    }
    /**
     * Generate a DeluxeMenus local (config.yml) menu
     * @param gui the gui from where the items are took
     * @param player the command sender
     */
    public void localMenu(Inventory gui, Player player) {
        try {
            long start = System.currentTimeMillis();

            for (String key : plugin.getConfig().getKeys(false)) {
                plugin.getConfig().set(key, null);
            }

            plugin.getConfig().createSection("gui_menus.GUIHelper.items");

            for (int s = 0; s < gui.getSize(); s++) {
                if (gui.getItem(s) != null && gui.getItem(s).getType() != Material.AIR) {
                    ItemStack item = gui.getItem(s);
                    ItemMeta meta = item.getItemMeta();

                    plugin.getConfig().set("gui_menus.GUIHelper.items." + s + ".material", item.getType().toString());

                    if (item.getDurability() > 0) plugin.getConfig().set("gui_menus.GUIHelper.items." + s + ".data", item.getDurability());

                    if (item.getAmount() > 1) plugin.getConfig().set("gui_menus.GUIHelper.items." + s + ".amount", item.getAmount());

                    if (item.getType().equals(Material.LEATHER_HELMET) || item.getType().equals(Material.LEATHER_CHESTPLATE) || item.getType().equals(Material.LEATHER_LEGGINGS) || item.getType().equals(Material.LEATHER_BOOTS)) {
                        LeatherArmorMeta armorMeta = (LeatherArmorMeta) item.getItemMeta();

                        plugin.getConfig().set("gui_menus.GUIHelper.items." + s + ".color", armorMeta.getColor().getRed() + ", " + armorMeta.getColor().getGreen() + ", " + armorMeta.getColor().getBlue());
                    }

                    /*if (item.getType().equals(Material.BANNER)) {
                        BannerMeta bannerMeta = (BannerMeta) item.getItemMeta();
                        List<String> banner_meta = new ArrayList<>();

                        if (bannerMeta.getPatterns().size() > 0) {
                            for (Pattern pattern : bannerMeta.getPatterns()) {
                                banner_meta.add(pattern.getColor() + ";" + pattern.getPattern());
                            }

                            plugin.getConfig().set("items." + s + ".banner_meta", banner_meta);
                        }
                    } */

                    plugin.getConfig().set("gui_menus.GUIHelper.items." + s + ".slot", s);

                    if (meta.hasDisplayName()) plugin.getConfig().set("gui_menus.GUIHelper.items." + s + ".display_name", meta.getDisplayName().replaceAll("§", "&"));

                    if (meta.hasLore()) {
                        List<String> lore = new ArrayList<>();

                        for (String line : meta.getLore()) {
                            lore.add(line.replaceAll("§", "&"));
                        }

                        plugin.getConfig().set("gui_menus.GUIHelper.items." + s + ".lore", lore);
                    }

                    if (meta.getEnchants().size() > 0) {
                        List<String> enchantments = new ArrayList<>();

                        for (Enchantment en : meta.getEnchants().keySet()) {
                            enchantments.add(en.getName() + ";" + meta.getEnchantLevel(en));
                        }

                        plugin.getConfig().set("gui_menus.GUIHelper.items." + s + ".enchantments", enchantments);
                    }
                }
            }

            plugin.saveConfig();
            player.sendMessage(colorize("&a[GH] Done! &7(" + (System.currentTimeMillis() - start) + "ms)"));
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(colorize("&c[GH] Something went wrong, please check the console."));
        }
    }
}
