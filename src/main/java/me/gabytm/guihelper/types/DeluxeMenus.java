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

package me.gabytm.guihelper.types;

import com.sun.xml.internal.bind.v2.TODO;
import me.gabytm.guihelper.GUIHelper;
import me.gabytm.guihelper.utils.Messages;
import me.gabytm.guihelper.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class DeluxeMenus {
    private GUIHelper plugin;

    DeluxeMenus(GUIHelper plugin) {
        this.plugin = plugin;
    }

    /**
     * Generate an external menu config
     * @param gui the gui from where the items are took
     * @param player the command sender
     */
    public void generateExternal(Inventory gui, Player player) {
        try {
            long start = System.currentTimeMillis();

            for (String key : plugin.getConfig().getKeys(false)) {
                plugin.getConfig().set(key, null);
            }

            plugin.getConfig().createSection("items");

            for (int slot = 0; slot < gui.getSize(); slot++) {
                if (gui.getItem(slot) != null && gui.getItem(slot).getType() != Material.AIR) {
                    ItemStack item = gui.getItem(slot);
                    ItemMeta meta = item.getItemMeta();
                    String path = "items." + slot;

                    addItem(path, item, meta, slot);
                }
            }

            plugin.saveConfig();
            player.sendMessage(Messages.CREATION_DONE.format(null, (System.currentTimeMillis() - start), null));
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(Messages.CREATION_ERROR.format(null, null, null));
        }
    }

    /**
     * Generate items for a DeluxeMenus local menu (config.yml)
     * @param gui the gui from where the items are took
     * @param player the command sender
     */
    public void generateLocal(Inventory gui, Player player) {
        try {
            long start = System.currentTimeMillis();

            for (String key : plugin.getConfig().getKeys(false)) {
                plugin.getConfig().set(key, null);
            }

            plugin.getConfig().createSection("gui_menus.GUIHelper.items");

            for (int slot = 0; slot < gui.getSize(); slot++) {
                if (gui.getItem(slot) != null && gui.getItem(slot).getType() != Material.AIR) {
                    String path = "gui_menus.GUIHelper.items." + slot;
                    ItemStack item = gui.getItem(slot);
                    ItemMeta meta = item.getItemMeta();

                    addItem(path, item, meta, slot);
                }
            }

            plugin.saveConfig();
            player.sendMessage(Messages.CREATION_DONE.format(null, (System.currentTimeMillis() - start), null));
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(Messages.CREATION_ERROR.format(null, null, null));
        }
    }

    /**
     * Add an item to the config
     * @param path the config path
     * @param item the item
     * @param meta the item meta
     * @param slot the item slot
     */
    @SuppressWarnings("Duplicates")
    private void addItem(String path, ItemStack item, ItemMeta meta, int slot) {
        StringUtils.addToConfig(path + ".material", item.getType().toString());

        if (item.getDurability() > 0) StringUtils.addToConfig(path + ".data", item.getDurability());

        if (item.getAmount() > 1) StringUtils.addToConfig(path + ".amount", item.getAmount());

        if (item.getType().toString().contains("LEATHER_")) {
            LeatherArmorMeta armorMeta = (LeatherArmorMeta) item.getItemMeta();

            StringUtils.addToConfig(path + ".color", armorMeta.getColor().getRed() + ", " + armorMeta.getColor().getGreen() + ", " + armorMeta.getColor().getBlue());
        }

        //TODO
        // Add support for banners

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

        StringUtils.addToConfig(path + ".slot", slot);

        if (meta.hasDisplayName()) StringUtils.addToConfig(path + ".display_name", meta.getDisplayName().replaceAll("§", "&"));

        if (meta.hasLore()) {
            List<String> lore = new ArrayList<>();

            for (String line : meta.getLore()) {
                lore.add(line.replaceAll("§", "&"));
            }

            StringUtils.addToConfig(path + ".lore", lore);
        }

        if (meta.hasEnchants()) {
            List<String> enchantments = new ArrayList<>();

            for (Enchantment en : meta.getEnchants().keySet()) {
                enchantments.add(en.getName() + ";" + meta.getEnchantLevel(en));
            }

            StringUtils.addToConfig(path + ".enchantments", enchantments);
        }
    }
}