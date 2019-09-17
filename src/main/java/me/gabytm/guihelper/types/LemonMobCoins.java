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
import me.gabytm.guihelper.utils.Messages;
import me.gabytm.guihelper.utils.RomanNumber;
import me.gabytm.guihelper.utils.StringUtils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LemonMobCoins {
    private GUIHelper plugin;

    LemonMobCoins(GUIHelper plugin) { this.plugin = plugin; }

    /**
     * Generate a shop config
     * @param gui the gui from where the items are took
     * @param player the command sender
     */
    public void generate(Inventory gui, Player player) {
        try {
            long start = System.currentTimeMillis();

            for (String key : plugin.getConfig().getKeys(false)) {
                plugin.getConfig().set(key, null);
            }

            plugin.getConfig().createSection("gui.items");

            for (int slot = 0; slot < gui.getSize(); slot++) {
                if (gui.getItem(slot) != null && gui.getItem(slot).getType() != Material.AIR) {
                    String path = "gui.items.item-" + slot;
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
     * Create a shop config
     * @param path the path
     * @param item the item
     * @param meta the {@param item} meta
     * @param slot the {@param item} slot
     */
    @SuppressWarnings("Duplicates")
    private void addItem(String path, ItemStack item, ItemMeta meta, int slot) {
        StringBuilder rewardItem = new StringBuilder();
        StringBuilder rewardItemDisplayName = new StringBuilder();
        StringBuilder rewardItemLore = new StringBuilder();
        StringBuilder rewardItemEnchantments = new StringBuilder();
        List<String> rewardItemsList = new ArrayList<>();

        StringUtils.addToConfig(path + ".material", item.getType().toString());
        StringUtils.addToConfig(path + ".slot", slot);
        StringUtils.addToConfig(path + ".amount", item.getAmount());
        StringUtils.addToConfig(path + ".glowing", meta.hasEnchants());

        if (meta.hasDisplayName()) {
            StringUtils.addToConfig(path + ".displayname", meta.getDisplayName().replaceAll("§", "&"));
            rewardItemDisplayName.append(" name:").append(meta.getDisplayName().replaceAll("§", "&").replaceAll(" ", "_"));
        }

        if (meta.hasEnchants()) {
            List<String> enchantments = new ArrayList<>();

            for (Enchantment en : meta.getEnchants().keySet()) {
                enchantments.add(StringUtils.formatEnchantmentName(en) + " " + RomanNumber.toRoman(meta.getEnchantLevel(en)));
                rewardItemEnchantments.append(" ").append(en.getName()).append(":").append(meta.getEnchantLevel(en));
            }

            StringUtils.addToConfig(path + ".lore", enchantments);
        }

        if (meta.hasLore()) {
            List<String> lore = plugin.getConfig().getStringList(path + ".lore");

            rewardItemLore.append(" lore:");

            for (String line : meta.getLore()) {
                lore.add(line.replaceAll("§", "&"));
                rewardItemLore.append(line.replaceAll("§", "&").replaceAll(" ", "_")).append("|");
            }

            StringUtils.addToConfig(path + ".lore", lore);
        }

        StringUtils.addToConfig(path + ".permission", false);
        StringUtils.addToConfig(path + ".price", 100);
        rewardItem.append("give %player% ").append(item.getType().toString()).append(" ").append(item.getAmount());

        if (rewardItemDisplayName.length() > 0) rewardItem.append(rewardItemDisplayName.toString());
        if (rewardItemLore.length() > 0) rewardItem.append(rewardItemLore.toString(), 0, rewardItemLore.length() - 1);
        if (rewardItemEnchantments.length() > 0) rewardItem.append(rewardItemEnchantments.toString());

        rewardItemsList.add(rewardItem.toString());
        StringUtils.addToConfig(path + ".commands", rewardItemsList);
    }
}
