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
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.ArrayList;
import java.util.List;

public class CrazyEnvoy {
    private GUIHelper plugin;

    CrazyEnvoy(GUIHelper plugin) { this.plugin = plugin; }

    /**
     * Generate an envoy config
     * @param gui the gui from where the items are took
     * @param player the command sender
     * @param page the page
     */
    @SuppressWarnings("Duplicates")
    public void generate(Inventory gui, Player player, int page) {
        try {
            long start = System.currentTimeMillis();

            for (String key : plugin.getConfig().getKeys(false)) {
                plugin.getConfig().set(key, null);
            }

            plugin.getConfig().createSection("Prizes");

            for (int slot = 0; slot < gui.getSize(); slot++) {
                if (gui.getItem(slot) != null && gui.getItem(slot).getType() != Material.AIR) {
                    String path = "Prizes." + (page > 1 ? slot + 1 + (53 * (page - 1)) : slot);
                    ItemStack item = gui.getItem(slot);
                    ItemMeta meta = item.getItemMeta();

                    addPrize(path, item, meta);
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
     * Crate an envoy prize
     * @param path the path
     * @param item the item
     * @param meta the {@param item} meta
     */
    @SuppressWarnings("Duplicates")
    private void addPrize(String path, ItemStack item, ItemMeta meta) {
        StringBuilder rewardItem = new StringBuilder();
        StringBuilder rewardItemMaterial = new StringBuilder();
        StringBuilder rewardItemAmount = new StringBuilder();
        StringBuilder rewardItemDisplayName = new StringBuilder();
        StringBuilder rewardItemLore = new StringBuilder();
        StringBuilder rewardItemEnchantments = new StringBuilder();
        List<String> rewardItemsList = new ArrayList<>();

        if (meta.hasDisplayName()) rewardItemDisplayName.append(", Name:").append(meta.getDisplayName().replaceAll("§", "&"));

        rewardItemMaterial.append("Item:").append(item.getType().toString());

        if (item.getDurability() > 0) rewardItemMaterial.append(":").append(item.getDurability());

        if (item.getType().toString().contains("MONSTER_EGG")) rewardItemMaterial.append(":").append(((SpawnEggMeta) meta).getSpawnedType().getTypeId());

        rewardItemAmount.append(", Amount:").append(item.getAmount());

        if (meta.hasLore()) {
            rewardItemLore.append(", Lore:");

            for (String line : meta.getLore()) {
                rewardItemLore.append(line.replaceAll("§", "&")).append(",");
            }
        }

        if (meta.hasEnchants()) {
            for (Enchantment en : meta.getEnchants().keySet()) {
                rewardItemEnchantments.append(", ").append(en.getName()).append(":").append(meta.getEnchantLevel(en));
            }
        }

        plugin.getConfig().set(path + ".Chance", 10);
        plugin.getConfig().set(path + ".Drop-Items", false);
        rewardItem.append(rewardItemMaterial.toString()).append(rewardItemAmount.toString());

        if (rewardItemDisplayName.length() > 0) rewardItem.append(rewardItemDisplayName.toString());
        if (rewardItemLore.length() > 0) rewardItem.append(rewardItemLore.toString(), 0, rewardItemLore.length() - 1);
        if (rewardItemEnchantments.length() > 0) rewardItem.append(rewardItemEnchantments.toString());

        rewardItemsList.add(rewardItem.toString());
        plugin.getConfig().set(path + ".Items", rewardItemsList);
    }
}