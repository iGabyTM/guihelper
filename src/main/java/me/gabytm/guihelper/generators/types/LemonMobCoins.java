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

package me.gabytm.guihelper.generators.types;

import me.gabytm.guihelper.GUIHelper;
import me.gabytm.guihelper.data.Config;
import me.gabytm.guihelper.utils.ItemUtil;
import me.gabytm.guihelper.utils.Message;
import me.gabytm.guihelper.utils.NumberUtil;
import me.gabytm.guihelper.utils.StringUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LemonMobCoins {
    private GUIHelper plugin;

    public LemonMobCoins(GUIHelper plugin) { this.plugin = plugin; }

    /**
     * Generate a shop config
     * @param gui the gui from where the items are took
     * @param player the command sender
     */
    public void generate(Inventory gui, Player player) {
        try {
            long start = System.currentTimeMillis();
            Config config = new Config("LemonMobCoins");

            config.empty();
            config.get().createSection("gui.items");

            for (int slot = 0; slot < gui.getSize(); slot++) {
                if (ItemUtil.isItem(gui.getItem(slot))) {
                    String path = "gui.items.item-" + slot + ".";

                    addItem(config.get(), path, gui.getItem(slot), slot);
                }
            }

            config.save();
            Message.CREATION_DONE.format(System.currentTimeMillis() - start).send(player);
        } catch (Exception e) {
            e.printStackTrace();
            Message.CREATION_ERROR.send(player);
        }
    }

    /**
     * Create a shop config
     * @param path the path
     * @param item the item
     * @param slot the {@param item} slot
     */
    @SuppressWarnings("DuplicatedCode")
    private void addItem(FileConfiguration config, String path, ItemStack item, int slot) {
        FileConfiguration defaults = plugin.getConfig();
        ItemMeta meta = item.getItemMeta();
        StringBuilder rewardItem = new StringBuilder();
        StringBuilder rewardItemDisplayName = new StringBuilder();
        StringBuilder rewardItemLore = new StringBuilder();
        StringBuilder rewardItemEnchantments = new StringBuilder();
        List<String> rewardItemsList = new ArrayList<>();

        config.set(path + "material", item.getType().toString());
        config.set(path + "slot", slot);
        config.set(path + "amount", item.getAmount());
        config.set(path + "glowing", meta.hasEnchants());

        if (meta.hasDisplayName()) {
            config.set(path + "displayname", ItemUtil.getDisplayName(meta));
            rewardItemDisplayName.append(" name:").append(ItemUtil.getDisplayName(meta).replaceAll(" ", "_"));
        }

        if (meta.hasEnchants()) {
            List<String> enchantments = new ArrayList<>();

            for (Enchantment en : meta.getEnchants().keySet()) {
                enchantments.add(StringUtil.formatEnchantmentName(en) + " " + NumberUtil.toRoman(meta.getEnchantLevel(en)));
                rewardItemEnchantments.append(" ").append(en.getName()).append(":").append(meta.getEnchantLevel(en));
            }

            config.set(path + "lore", enchantments);
        }

        if (meta.hasLore()) {
            rewardItemLore.append(" lore:");
            meta.getLore().forEach(l -> rewardItemLore.append(l.replaceAll("§", "&").replaceAll(" ", "_")).append("|"));
            config.set(path + "lore", ItemUtil.getLore(meta));
        }

        config.set(path + "permission", defaults.getBoolean("LemonMobCoins.permission"));
        config.set(path + "price", defaults.getInt("LemonMobCoins.price", 100));
        rewardItem.append("give %player% ").append(item.getType().toString()).append(" ").append(item.getAmount());

        if (rewardItemDisplayName.length() > 0) rewardItem.append(rewardItemDisplayName.toString());
        if (rewardItemLore.length() > 0) rewardItem.append(rewardItemLore.toString(), 0, rewardItemLore.length() - 1);
        if (rewardItemEnchantments.length() > 0) rewardItem.append(rewardItemEnchantments.toString());

        rewardItemsList.add(rewardItem.toString());
        config.set(path + "commands", rewardItemsList);
    }
}
