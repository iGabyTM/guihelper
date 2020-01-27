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
import me.gabytm.guihelper.generators.generators.IGeneratorSlot;
import me.gabytm.guihelper.utils.ItemUtil;
import me.gabytm.guihelper.utils.Message;
import me.gabytm.guihelper.utils.StringUtil;
import org.bukkit.configuration.ConfigurationSection;
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
import java.util.List;

public class BossShopPro implements IGeneratorSlot {
    private FileConfiguration defaultConfig;

    public BossShopPro(GUIHelper plugin) {
        this.defaultConfig = plugin.getConfig();
    }

    @Override
    public void generate(Inventory inventory, Player player) {
        try {
            final long start = System.currentTimeMillis();
            final Config config = new Config("BossShopPro/shops");

            config.empty();

            for (int slot = 0; slot < inventory.getSize(); slot++) {
                final ItemStack item = inventory.getItem(slot);

                if (ItemUtil.isNull(item)) continue;

                final String path = "shop.item-" + (slot + 1);

                addItem(config.get().createSection(path), item, (slot + 1));
            }

            config.save();
            Message.CREATION_DONE.format(System.currentTimeMillis() - start).send(player);
        } catch (Exception e) {
            StringUtil.saveError(e);
            Message.CREATION_ERROR.send(player);
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void addItem(ConfigurationSection section, ItemStack item, int slot) {
        final ConfigurationSection defaults = defaultConfig.getConfigurationSection("BossShopPro.shop");
        final ItemMeta meta = item.getItemMeta();
        final List<String> rewardItem = new ArrayList<>();
        final List<String> menuItem = new ArrayList<>();

        section.set("RewardType", defaults.getString("RewardType", "ITEM"));
        section.set("PriceType", defaults.getString("PriceType", "MONEY"));
        section.set("Price", defaults.getInt("Price", 10));

        if (ItemUtil.isMonsterEgg(item)) {
            final String monsterEgg = "type:" + item.getType().toString() + ":" + ((SpawnEggMeta) meta).getSpawnedType().getTypeId();

            rewardItem.add(monsterEgg);
            menuItem.add(monsterEgg);
        } else if (ItemUtil.isLeatherArmor(item)) {
            final LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
            final String color = "color:" + lam.getColor().getRed() + "#" + lam.getColor().getGreen() + "#" + lam.getColor().getBlue();

            rewardItem.add("type:" + item.getType().toString());
            menuItem.add("type:" + item.getType().toString());
            rewardItem.add(color);
            menuItem.add(color);
        } else {
            final String type = "type:" + (item.getDurability() > 0 ? item.getType().toString() + ":" + item.getDurability() : item.getType().toString());

            rewardItem.add(type);
            menuItem.add(type);
        }

        rewardItem.add("amount:" + item.getAmount());
        menuItem.add("amount:" + item.getAmount());

        if (meta.hasDisplayName()) {
            rewardItem.add("name:" + ItemUtil.getDisplayName(meta));
            menuItem.add("name:" + ItemUtil.getDisplayName(meta));
        }

        if (meta.hasLore()) {
           final StringBuilder lore = new StringBuilder();

            for (String line : ItemUtil.getLore(meta)) {
                if (lore.length() > 1) lore.append("#");

                lore.append(line);
            }

            rewardItem.add("lore:" + lore.toString());
            menuItem.add("lore:" + lore.append(defaults.getString("lore", "")).toString());
        } else {
            menuItem.add("lore:" + defaults.getString("lore", ""));
        }

        if (meta.hasEnchants()) {
            for (Enchantment en : meta.getEnchants().keySet()) {
                final String enchantment = "enchantment:" + en.getName() + "#" + meta.getEnchantLevel(en);

                rewardItem.add(enchantment);
                menuItem.add(enchantment);
            }
        }

        if (meta.getItemFlags().size() > 0) {
            final StringBuilder flags = new StringBuilder();

            for (ItemFlag flag : meta.getItemFlags()) {
                if (flags.length() > 1) flags.append("#");

                flags.append(flag.toString());
            }

            rewardItem.add("hideflags:" + flags.toString());
            menuItem.add("hideflags:" + flags.toString());
        }

        section.set("Reward", rewardItem);
        section.set("MenuItem", menuItem);
        section.set("Message", defaults.getString("Message", ""));
        section.set("InventoryLocation", slot);
    }
}