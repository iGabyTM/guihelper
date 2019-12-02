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

import me.gabytm.guihelper.GUIHelper;
import me.gabytm.guihelper.utils.ItemUtil;
import me.gabytm.guihelper.utils.Messages;
import org.bukkit.configuration.file.FileConfiguration;
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
     *
     * @param gui    the gui from where the items are took
     * @param player the command sender
     */
    public void generateExternal(Inventory gui, Player player) {
        try {
            long start = System.currentTimeMillis();

            plugin.emptyConfig();
            plugin.getConfig().createSection("items");

            for (int slot = 0; slot < gui.getSize(); slot++) {
                if (!ItemUtil.slotIsEmpty(gui.getItem(slot))) {
                    addItem("items." + slot + ".", gui.getItem(slot), slot);
                }
            }

            plugin.saveConfig();
            player.sendMessage(Messages.CREATION_DONE.format(System.currentTimeMillis() - start));
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(Messages.CREATION_ERROR.value());
        }
    }

    /**
     * Generate items for a DeluxeMenus local menu (config.yml)
     *
     * @param gui    the gui from where the items are took
     * @param player the command sender
     */
    public void generateLocal(Inventory gui, Player player) {
        try {
            long start = System.currentTimeMillis();

            plugin.emptyConfig();
            plugin.getConfig().createSection("gui_menus.GUIHelper.items");

            for (int slot = 0; slot < gui.getSize(); slot++) {
                if (!ItemUtil.slotIsEmpty(gui.getItem(slot))) {
                    addItem("gui_menus.GUIHelper.items." + slot + ".", gui.getItem(slot), slot);
                }
            }

            plugin.saveConfig();
            player.sendMessage(Messages.CREATION_DONE.format(System.currentTimeMillis() - start));
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(Messages.CREATION_ERROR.value());
        }
    }

    /**
     * Add an item to the config
     *
     * @param path the config path
     * @param item the item
     * @param slot the item slot
     */
    private void addItem(String path, ItemStack item, int slot) {
        FileConfiguration config = plugin.getConfig();
        ItemMeta meta = item.getItemMeta();

        config.set(path + "material", item.getType().toString());

        if (item.getDurability() > 0) config.set(path + "data", item.getDurability());

        if (item.getAmount() > 1) config.set(path + "amount", item.getAmount());

        if (item.getType().toString().contains("LEATHER_")) {
            LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();

            config.set(path + "color", lam.getColor().getRed() + ", " + lam.getColor().getGreen() + ", " + lam.getColor().getBlue());
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

        config.set(path + "slot", slot);

        if (meta.hasDisplayName()) config.set(path + "display_name", meta.getDisplayName().replaceAll("§", "&"));

        if (meta.hasLore()) config.set(path + "lore", ItemUtil.getLore(meta));

        if (meta.hasEnchants()) {
            List<String> enchantments = new ArrayList<>();

            meta.getEnchants().keySet().forEach(en -> enchantments.add(en.getName() + ";" + meta.getEnchantLevel(en)));
            config.set(path + "enchantments", enchantments);
        }
    }
}