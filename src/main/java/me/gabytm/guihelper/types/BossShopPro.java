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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BossShopPro {
    private GUIHelper plugin;

    BossShopPro(GUIHelper plugin) {
        this.plugin = plugin;
    }

    /**
     * Generate a shop config
     *
     * @param gui    the gui from where the items are took
     * @param player the command sender
     */
    @SuppressWarnings("DuplicatedCode")
    public void generateShop(Inventory gui, Player player) {
        try {
            long start = System.currentTimeMillis();

            plugin.emptyConfig();
            plugin.getConfig().createSection("shop");

            for (int slot = 0; slot < gui.getSize(); slot++) {
                if (!ItemUtil.slotIsEmpty(gui.getItem(slot))) {
                    addShopItem("shop.item-" + (slot + 1) + ".", gui.getItem(slot), (slot + 1));
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
     * Generate a menu config
     *
     * @param gui    the gui from where the items are took
     * @param player the command sender
     */
    @SuppressWarnings("DuplicatedCode")
    public void generateMenu(Inventory gui, Player player) {
        try {
            long start = System.currentTimeMillis();

            plugin.emptyConfig();
            plugin.getConfig().createSection("shop");

            for (int slot = 0; slot < gui.getSize(); slot++) {
                if (!ItemUtil.slotIsEmpty(gui.getItem(slot))) {
                    addMenuItem("shop.item-" + (slot + 1) + ".", gui.getItem(slot), (slot + 1));
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
     * Create a shop item
     *
     * @param path the path
     * @param item the item
     * @param slot the {@param item} slot
     */
    @SuppressWarnings("DuplicatedCode")
    private void addShopItem(String path, ItemStack item, int slot) {
        FileConfiguration config = plugin.getConfig();
        ItemMeta meta = item.getItemMeta();
        List<String> rewardItem = new ArrayList<>();
        List<String> menuItem = new ArrayList<>();

        config.set(path + "RewardType", "ITEM");
        config.set(path + "PriceType", "MONEY");
        config.set(path + "Price", 10);

        if (ItemUtil.isMonsterEgg(item)) {
            String monsterEgg = "type:" + item.getType().toString() + ":" + ((SpawnEggMeta) meta).getSpawnedType().getTypeId();

            rewardItem.add(monsterEgg);
            menuItem.add(monsterEgg);
        } else if (ItemUtil.isLeatherArmor(item)) {
            LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
            String color = "color:" + lam.getColor().getRed() + "#" + lam.getColor().getGreen() + "#" + lam.getColor().getBlue();

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

        config.set(path + "Reward", rewardItem);
        config.set(path + "MenuItem", menuItem);
        config.set(path + "Message", "&aYou've purchased &e%reward%&a for &e$%price%");
        config.set(path + "InventoryLocation", slot);
    }

    /**
     * Create a menu item
     *
     * @param path the path
     * @param item the item
     * @param slot the {@param item} slot
     */
    @SuppressWarnings("DuplicatedCode")
    private void addMenuItem(String path, ItemStack item, int slot) {
        FileConfiguration config = plugin.getConfig();
        ItemMeta meta = item.getItemMeta();
        List<String> menuItem = new ArrayList<>();

        config.set(path + "RewardType", "playercommand");
        config.set(path + "PriceType", "free");

        if (ItemUtil.isMonsterEgg(item)) {
            String monsterEgg = "type:" + item.getType().toString() + ":" + ((SpawnEggMeta) meta).getSpawnedType().getTypeId();

            menuItem.add(monsterEgg);
        } else if (ItemUtil.isLeatherArmor(item)) {
            LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
            String color = "color:" + lam.getColor().getRed() + "#" + lam.getColor().getGreen() + "#" + lam.getColor().getBlue();

            menuItem.add("type:" + item.getType().toString());
            menuItem.add(color);
        } else {
            String type = "type:" + (item.getDurability() > 0 ? item.getType().toString() + ":" + item.getDurability() : item.getType().toString());

            menuItem.add(type);
        }

        menuItem.add("amount:" + item.getAmount());

        if (meta.hasDisplayName()) menuItem.add("name:" + ItemUtil.getDisplayName(meta));

        if (meta.hasLore()) {
            StringBuilder lore = new StringBuilder();

            for (String line : meta.getLore()) {
                if (lore.length() > 1) {
                    lore.append("#").append(line.replaceAll("§", "&"));
                } else {
                    lore.append(line.replaceAll("§", "&"));
                }
            }

            menuItem.add("lore:" + lore.toString());
        }

        if (meta.hasEnchants()) meta.getEnchants().keySet().forEach(en -> menuItem.add("enchantment:" + en.getName() + "#" + meta.getEnchantLevel(en)));

        if (meta.getItemFlags().size() > 0) {
            StringBuilder flags = new StringBuilder();

            for (ItemFlag flag : meta.getItemFlags()) {
                if (flags.length() > 1) {
                    flags.append("#").append(flag.toString());
                } else {
                    flags.append(flag.toString());
                }
            }

            menuItem.add("hideflags:" + flags.toString());
        }

        config.set(path + "Reward", Collections.singletonList("say Change me @ " + path + "Reward"));
        config.set(path + "MenuItem", menuItem);
        config.set(path + "Message", "&aChange me @ " + path + "Message");
        config.set(path + "InventoryLocation", slot);
    }
}