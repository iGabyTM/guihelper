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
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

import static me.gabytm.guihelper.utils.StringUtils.colorize;

public class ChestCommands {
    private GUIHelper plugin;

    ChestCommands(GUIHelper plugin) { this.plugin = plugin; }

    @SuppressWarnings("Duplicates")
    public void generate(Inventory gui, Player player) {
        try {
            long start = System.currentTimeMillis();

            for (String key : plugin.getConfig().getKeys(false)) {
                plugin.getConfig().set(key, null);
            }

            for (int slot = 0; slot < gui.getSize(); slot++) {
                if (gui.getItem(slot) != null && gui.getItem(slot).getType() != Material.AIR) {
                    plugin.getConfig().createSection("item-" + slot);

                    String path = "item-" + slot;
                    ItemStack item = gui.getItem(slot);
                    ItemMeta meta = item.getItemMeta();

                    addItem(path, item, meta, slot);
                }
            }

            plugin.saveConfig();
            player.sendMessage(colorize("&aDone! &7(" + (System.currentTimeMillis() - start) + "ms)"));
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(colorize("&cSomething went wrong, please check the console."));
        }
    }

    @SuppressWarnings("Duplicates")
    private void addItem(String path, ItemStack item, ItemMeta meta, int slot) {
        //plugin.getConfig().set(path + ".ID", item.getType().toString() + ":" + item.getDurability() + "," + item.getAmount());
        plugin.getConfig().set(path + ".ID", item.getType().toString());

        if (item.getDurability() > 0) plugin.getConfig().set(path + ".DATA-VALUE", item.getDurability());

        if (item.getAmount() > 1) plugin.getConfig().set(path + ".AMOUNT", item.getAmount());

        setItemPosition(path, slot);

        if (meta.hasDisplayName()) plugin.getConfig().set(path + ".NAME", meta.getDisplayName().replaceAll("§", "&"));

        if (meta.hasLore()) {
            List<String> lore = new ArrayList<>();

            for (String line : meta.getLore()) {
                lore.add(line.replaceAll("§", "&"));
            }

            plugin.getConfig().set(path + ".LORE", lore);
        }

        if (meta.getEnchants().size() > 0) {
            StringBuilder enchantments = new StringBuilder();

            for (Enchantment en : meta.getEnchants().keySet()) {
                if (enchantments.length() > 0) {
                    enchantments.append(";").append(en.getName()).append(",").append(meta.getEnchantLevel(en));
                } else {
                    enchantments.append(en.getName()).append(",").append(meta.getEnchantLevel(en));
                }
            }

            plugin.getConfig().set(path + ".ENCHANTMENT", enchantments.toString());
        }

        if (item.getType().toString().contains("LEATHER_")) {
            LeatherArmorMeta armorMeta = (LeatherArmorMeta) item.getItemMeta();

            plugin.getConfig().set(path + ".COLOR", armorMeta.getColor().getRed() + ", " + armorMeta.getColor().getGreen() + ", " + armorMeta.getColor().getBlue());
        }
    }

    /**
     * Transform a slot into x and y position
     * @param path the path where the position will be saved
     * @param slot the item slot
     */
    private void setItemPosition(String path, int slot) {
        int y = Math.toIntExact(slot / 9 + 1);
        int x = slot + 1 - (9 * (y - 1));

        plugin.getConfig().set(path + ".POSITION-X", x);
        plugin.getConfig().set(path + ".POSITION-Y", y);
    }
}
