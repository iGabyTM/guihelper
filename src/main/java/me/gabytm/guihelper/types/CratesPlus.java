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
import me.gabytm.guihelper.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.ArrayList;
import java.util.List;

public class CratesPlus {
    private GUIHelper plugin;

    CratesPlus(GUIHelper plugin) { this.plugin = plugin; }

    /**
     * Generate a shop config
     * @param gui the gui from where the items are took
     * @param player the command sender
     */
    @SuppressWarnings("Duplicates")
    public void generate(Inventory gui, Player player) {
        try {
            long start = System.currentTimeMillis();

            for (String key : plugin.getConfig().getKeys(false)) {
                plugin.getConfig().set(key, null);
            }

            plugin.getConfig().createSection("Crates.GUIHelper.Winnings");

            for (int slot = 0; slot < gui.getSize(); slot++) {
                if (gui.getItem(slot) != null && gui.getItem(slot).getType() != Material.AIR) {
                    String path = "Crates.GUIHelper.Winnings." + (slot + 1);
                    ItemStack item = gui.getItem(slot);
                    ItemMeta meta = item.getItemMeta();

                    addItem(path, item, meta);
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
     */
    @SuppressWarnings("Duplicates")
    private void addItem(String path, ItemStack item, ItemMeta meta) {
        StringUtils.addToConfig(path + ".Type", "ITEM");
        StringUtils.addToConfig(path + ".Item Type", item.getType().toString());

        if (item.getDurability() > 0) StringUtils.addToConfig(path + ".Item Data", item.getDurability());

        if (item.getType().toString().contains("MONSTER_EGG")) StringUtils.addToConfig(path + ".Item Data", ((SpawnEggMeta) meta).getSpawnedType().getTypeId());

        StringUtils.addToConfig(path + ".Percentage", 10);

        if (meta.hasDisplayName()) StringUtils.addToConfig(path + ".Name", meta.getDisplayName().replaceAll("§", "&"));

        StringUtils.addToConfig(path + ".Amount", item.getAmount());

        if (meta.hasEnchants()) {
            List<String> enchantments = new ArrayList<>();

            for (Enchantment en : meta.getEnchants().keySet()) {
                enchantments.add(en.getName() + "-" + meta.getEnchantLevel(en));
            }

            StringUtils.addToConfig(path + ".Enchantments", enchantments);
        }

        if (meta.hasLore()) {
            List<String> lore = new ArrayList<>();

            for (String line : meta.getLore()) {
                lore.add(line.replaceAll("§", "&"));
            }

            StringUtils.addToConfig(path + ".Lore", lore);
        }

        if (meta.getItemFlags().size() > 0) {
            List<String> flags = new ArrayList<>();

            for (ItemFlag flag : meta.getItemFlags()) {
                flags.add(flag.toString());
            }

            StringUtils.addToConfig(path + ".Flags", flags);
        }
    }
}
