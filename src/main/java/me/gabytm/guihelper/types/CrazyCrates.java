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

import java.util.ArrayList;
import java.util.List;

import static me.gabytm.guihelper.utils.StringUtils.colorize;

public class CrazyCrates {
    private GUIHelper plugin;

    CrazyCrates(GUIHelper plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("Duplicates")
    public void generate(Inventory gui, Player player) {
        try {
            long start = System.currentTimeMillis();

            for (String key : plugin.getConfig().getKeys(false)) {
                plugin.getConfig().set(key, null);
            }

            plugin.getConfig().createSection("Crate.Prizes");

            for (int slot = 0; slot < gui.getSize(); slot++) {
                if (gui.getItem(slot) != null && gui.getItem(slot).getType() != Material.AIR) {
                    String path = "Crate.Prizes." + slot;
                    ItemStack item = gui.getItem(slot);
                    ItemMeta meta = item.getItemMeta();

                    addPrize(path, item, meta);
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
    private void addPrize(String path, ItemStack item, ItemMeta meta) {
        StringBuilder rewardItem = new StringBuilder();
        StringBuilder rewardItemMaterial = new StringBuilder();
        StringBuilder rewardItemAmount = new StringBuilder();
        StringBuilder rewardItemDisplayName = new StringBuilder();
        StringBuilder rewardItemLore = new StringBuilder();
        StringBuilder rewardItemEnchantments = new StringBuilder();
        List<String> rewardItemsList = new ArrayList<>();

        if (meta.hasDisplayName()) {
            plugin.getConfig().set(path + ".DisplayName", meta.getDisplayName().replaceAll("§", "&"));
            rewardItemDisplayName.append(", Name:").append(meta.getDisplayName().replaceAll("§", "&"));
        }

        plugin.getConfig().set(path + ".DisplayItem", item.getType().toString());
        rewardItemMaterial.append("Item:").append(item.getType().toString());

        if (item.getDurability() > 0) {
            plugin.getConfig().set(path + ".DisplayItem", item.getType().toString() + ":" + item.getDurability());
            rewardItemMaterial.append(":").append(item.getDurability());
        }

        plugin.getConfig().set(path + ".DisplayAmount", item.getAmount());
        rewardItemAmount.append(", Amount:").append(item.getAmount());

        if (meta.hasLore()) {
            List<String> lore = new ArrayList<>();

            rewardItemLore.append(", Lore:");

            for (String line : meta.getLore()) {
                lore.add(line.replaceAll("§", "&"));
                rewardItemLore.append(line.replaceAll("§", "&")).append(",");
            }

            plugin.getConfig().set(path + ".Lore", lore);
        }

        if (meta.getEnchants().size() > 0) {
            List<String> enchantments = new ArrayList<>();

            for (Enchantment en : meta.getEnchants().keySet()) {
                enchantments.add(en.getName() + ":" + meta.getEnchantLevel(en));
                rewardItemEnchantments.append(", ").append(en.getName()).append(":").append(meta.getEnchantLevel(en));
            }

            plugin.getConfig().set(path + ".DisplayEnchantments", enchantments);
        }

        plugin.getConfig().set(path + ".MaxRange", 100);
        plugin.getConfig().set(path + ".Chance", 10);
        plugin.getConfig().set(path + ".Firework", false);
        rewardItem.append(rewardItemMaterial.toString()).append(rewardItemAmount.toString());

        if (!rewardItemDisplayName.toString().equals("")) rewardItem.append(rewardItemDisplayName.toString());
        if (!rewardItemLore.toString().equals("")) rewardItem.append(rewardItemLore.toString(), 0, rewardItemLore.length() - 1);
        if (!rewardItemEnchantments.toString().equals("")) rewardItem.append(rewardItemEnchantments.toString());

        rewardItemsList.add(rewardItem.toString());
        plugin.getConfig().set(path + ".Items", rewardItemsList);
    }
}