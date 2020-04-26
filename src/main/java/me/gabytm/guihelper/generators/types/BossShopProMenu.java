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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.ArrayList;
import java.util.List;

public final class BossShopProMenu implements IGeneratorSlot {
    private GUIHelper plugin;
    private ConfigurationSection defaults;

    public BossShopProMenu(final GUIHelper plugin) {
        this.plugin = plugin;
        defaults = plugin.getConfig().getConfigurationSection("BossShopPro.menu");
    }

    @Override
    public void generate(final Inventory inventory, final Player player) {
        final long start = System.currentTimeMillis();
        final Config config = new Config("BossShopPro/shops", plugin);

        config.empty();

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            final ItemStack item = inventory.getItem(slot);

            if (ItemUtil.isNull(item)) continue;

            final String path = "shop.item-" + (slot + 1);

            addItem(config.get().createSection(path), item, slot);
        }

        config.save();
        Message.CREATION_DONE.format(System.currentTimeMillis() - start).send(player);
    }

    @Override
    public void addItem(final ConfigurationSection section, final ItemStack item, final int slot) {
        final ItemMeta meta = item.getItemMeta();
        final List<String> menuItem = new ArrayList<>();
        final String itemType = item.getType().toString();

        section.set("RewardType", defaults.getString("RewardType", "playercommand"));
        section.set("PriceType", defaults.getString("PriceType", "free"));

        if (ItemUtil.isMonsterEgg(item)) {
            menuItem.add("type:" + itemType + ":" + ((SpawnEggMeta) meta).getSpawnedType().getTypeId());
        } else if (ItemUtil.isLeatherArmor(item)) {
            final LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();

            menuItem.add("type:" + itemType);
            menuItem.add("color:" + lam.getColor().getRed() + "#" + lam.getColor().getGreen() + "#" + lam.getColor().getBlue());
        } else {
            menuItem.add("type:" + (item.getDurability() > 0 ? itemType + ":" + item.getDurability() : itemType));
        }

        menuItem.add("amount:" + item.getAmount());

        if (meta.hasDisplayName()) {
            menuItem.add("name:" + ItemUtil.getDisplayName(meta));
        }

        if (meta.hasLore()) {
            final StringBuilder lore = new StringBuilder();

            for (String line : meta.getLore()) {
                if (lore.length() > 1) lore.append("#");

                lore.append(line);
            }

            menuItem.add("lore:" + lore.toString());
        }

        if (meta.hasEnchants()) {
            meta.getEnchants().keySet().forEach(en -> menuItem.add("enchantment:" + en.getName() + "#" + meta.getEnchantLevel(en)));
        }

        if (meta.getItemFlags().size() > 0) {
            final StringBuilder flags = new StringBuilder();

            for (ItemFlag flag : meta.getItemFlags()) {
                if (flags.length() > 1) flags.append("#");

                flags.append(flag.toString());
            }

            menuItem.add("hideflags:" + flags.toString());
        }

        section.set("Reward", defaults.getStringList("Reward"));
        section.set("MenuItem", menuItem);
        section.set("Message", defaults.getString("Message", "&aChange me @ " + section.getName() + "Message"));
        section.set("InventoryLocation", slot);
    }
}
