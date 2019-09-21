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
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.ArrayList;
import java.util.List;

public class BossShopPro {
    private GUIHelper plugin;

    BossShopPro(GUIHelper plugin) { this.plugin = plugin; }

    /**
     * Generate a menu config
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

            plugin.getConfig().createSection("shop");

            for (int slot = 0; slot < gui.getSize(); slot++) {
                if (gui.getItem(slot) != null && gui.getItem(slot).getType() != Material.AIR) {
                    String path = "shop.item-" + (slot + 1);
                    ItemStack item = gui.getItem(slot);
                    ItemMeta meta = item.getItemMeta();

                    addItem(path, item, meta, (slot + 1));
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
     * Create a menu item
     * @param path the path
     * @param item the item
     * @param meta the {@param item} meta
     * @param slot the {@param item} slot
     */
    @SuppressWarnings("Duplicates")
    private void addItem(String path, ItemStack item, ItemMeta meta, int slot) {
        List<String> rewardItem = new ArrayList<>();
        List<String> menuItem = new ArrayList<>();

        StringUtils.addToConfig(path + ".RewardType", "ITEM");
        StringUtils.addToConfig(path + ".PriceType", "MONEY");
        StringUtils.addToConfig(path + ".Price", 10);

        if (item.getType().toString().contains("MONSTER_EGG")) {
            String monsterEgg = "type:" + item.getType().toString() + ":" + ((SpawnEggMeta) meta).getSpawnedType().getTypeId();

            rewardItem.add(monsterEgg);
            menuItem.add(monsterEgg);
        } else if (item.getType().toString().contains("LEATHER_")) {
            LeatherArmorMeta armorMeta = (LeatherArmorMeta) item.getItemMeta();
            String color = "color:" + armorMeta.getColor().getRed() + "#" + armorMeta.getColor().getGreen() + "#" + armorMeta.getColor().getBlue();

            rewardItem.add("type:" + item.getType().toString());
            menuItem.add("type:" + item.getType().toString());
            rewardItem.add(color);
            menuItem.add(color);
        } else {
            String type = "type:" + (item.getDurability() > 0 ? item.getType().toString() + ":" + item.getDurability() : item.getType().toString());

            rewardItem.add(type);
            menuItem.add(type);
        }

        rewardItem.add("amount:" + item.getAmount());
        menuItem.add("amount:" + item.getAmount());

        if (meta.hasDisplayName()) {
            rewardItem.add("name:" + meta.getDisplayName().replaceAll("§", "&"));
            menuItem.add("name:" + meta.getDisplayName().replaceAll("§", "&"));
        }

        if (meta.hasLore()) {
            StringBuilder lore = new StringBuilder();

            for (String line : meta.getLore()) {
                if (lore.length() > 1) {
                    lore.append("#").append(line.replaceAll("§", "&"));
                } else {
                    lore.append(line.replaceAll("§", "&"));
                }
            }

            rewardItem.add("lore:" + lore.toString());
            menuItem.add("lore:" + lore.append("##&eClick to buy for &e$%price%").toString());
        } else {
            menuItem.add("lore:#&eClick to buy for &e$%price%");
        }

        if (meta.hasEnchants()) {
            for (Enchantment en : meta.getEnchants().keySet()) {
                rewardItem.add("enchantment:" + en.getName() + "#" + meta.getEnchantLevel(en));
                menuItem.add("enchantment:" + en.getName() + "#" + meta.getEnchantLevel(en));
            }
        }

        if (meta.getItemFlags().size() > 0) {
            StringBuilder flags = new StringBuilder();

            for (ItemFlag flag : meta.getItemFlags()) {
                if (flags.length() > 1) {
                    flags.append("#").append(flag.toString());
                } else {
                    flags.append(flag.toString());
                }
            }

            rewardItem.add("hideflags:" + flags.toString());
            menuItem.add("hideflags:" + flags.toString());
        }

        StringUtils.addToConfig(path + ".Reward", rewardItem);
        StringUtils.addToConfig(path + ".MenuItem", menuItem);
        StringUtils.addToConfig(path + ".Message", "&aYou've purchased &e%reward%&a for &e$%price%");
        StringUtils.addToConfig(path + ".InventoryLocation", slot);
    }
}