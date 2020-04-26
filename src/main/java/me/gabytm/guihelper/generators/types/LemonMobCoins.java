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
import me.gabytm.guihelper.utils.NumberUtil;
import me.gabytm.guihelper.utils.StringUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class LemonMobCoins implements IGeneratorSlot {
    private GUIHelper plugin;
    private ConfigurationSection defaults;

    public LemonMobCoins(final GUIHelper plugin) {
        this.plugin = plugin;
        defaults = plugin.getConfig().getConfigurationSection("LemonMobCoins");
    }

    @Override
    public void generate(final Inventory gui, final Player player) {
        final long start = System.currentTimeMillis();
        final Config config = new Config("LemonMobCoins", plugin);

        config.empty();

        for (int slot = 0; slot < gui.getSize(); slot++) {
            final ItemStack item = gui.getItem(slot);

            if (ItemUtil.isNull(item)) continue;

            final String path = "gui.items.item-" + slot;

            addItem(config.get().createSection(path), item, slot);
        }

        config.save();
        Message.CREATION_DONE.setDuration(System.currentTimeMillis() - start).send(player);
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void addItem(final ConfigurationSection section, final ItemStack item, final int slot) {
        final ItemMeta meta = item.getItemMeta();
        final StringBuilder rewardItem = new StringBuilder();
        final StringBuilder rewardItemDisplayName = new StringBuilder();
        final StringBuilder rewardItemLore = new StringBuilder();
        final StringBuilder rewardItemEnchantments = new StringBuilder();
        final List<String> rewardItemsList = new ArrayList<>();

        section.set("material", item.getType().toString());
        section.set("slot", slot);
        section.set("amount", item.getAmount());
        section.set("glowing", meta.hasEnchants());

        if (meta.hasDisplayName()) {
            section.set("displayname", ItemUtil.getDisplayName(meta));
            rewardItemDisplayName.append(" name:").append(ItemUtil.getDisplayName(meta).replace(" ", "_"));
        }

        if (meta.hasEnchants()) {
            setEnchantments(meta.getEnchants(), section, rewardItemEnchantments);
        }

        if (meta instanceof EnchantmentStorageMeta) {
            final EnchantmentStorageMeta esm = (EnchantmentStorageMeta) meta;
            setEnchantments(esm.getStoredEnchants(), section, rewardItemEnchantments);
        }

        if (meta.hasLore()) {
            rewardItemLore.append(" lore:");
            ItemUtil.getLore(meta).forEach(l -> rewardItemLore.append(l.replace(" ", "_")).append("|"));
            section.set("lore", ItemUtil.getLore(meta));
        }

        section.set("permission", defaults.getBoolean("permission"));
        section.set("price", defaults.getInt("price", 100));
        rewardItem.append("give %player% ").append(item.getType().toString()).append(" ").append(item.getAmount());

        if (rewardItemDisplayName.length() > 0) {
            rewardItem.append(rewardItemDisplayName.toString());
        }

        if (rewardItemLore.length() > 0) {
            rewardItem.append(rewardItemLore.toString(), 0, rewardItemLore.length() - 1);
        }

        if (rewardItemEnchantments.length() > 0) {
            rewardItem.append(rewardItemEnchantments.toString());
        }

        rewardItemsList.add(rewardItem.toString());
        section.set("commands", rewardItemsList);
    }

    public void setEnchantments(final Map<Enchantment, Integer> enchantments, final ConfigurationSection section, final StringBuilder builder) {
        final List<String> list = section.getStringList("lore");

        enchantments.forEach((enchantment, level) -> {
            list.add(StringUtil.formatEnchantmentName(enchantment) + " " + NumberUtil.toRoman(level));
            builder.append(" ").append(enchantment.getName()).append(":").append(level);
        });

        section.set("lore", list);
    }
}
