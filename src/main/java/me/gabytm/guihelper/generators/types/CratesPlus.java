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
import me.gabytm.guihelper.generators.generators.IGenerator;
import me.gabytm.guihelper.utils.ItemUtil;
import me.gabytm.guihelper.utils.Message;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CratesPlus implements IGenerator {
    private GUIHelper plugin;
    private ConfigurationSection defaults;

    public CratesPlus(final GUIHelper plugin) {
        this.plugin = plugin;
        defaults = plugin.getConfig().getConfigurationSection("CratesPlus");
    }

    @Override
    public void generate(final Inventory gui, final Player player) {
        final long start = System.currentTimeMillis();
        final Config config = new Config("CratesPlus", plugin);

        config.empty();

        for (int slot = 0; slot < gui.getSize(); slot++) {
            final ItemStack item = gui.getItem(slot);

            if (ItemUtil.isNull(item)) continue;

            final String path = "Crates.GUIHelper.Winnings." + (slot + 1);

            addItem(config.get().createSection(path), gui.getItem(slot));
        }

        config.save();
        Message.CREATION_DONE.format(System.currentTimeMillis() - start).send(player);
    }

    @Override
    public void addItem(final ConfigurationSection section, final ItemStack item) {
        final ItemMeta meta = item.getItemMeta();

        section.set("Type", defaults.getString("Type", "ITEM"));
        section.set("Item Type", item.getType().toString());

        if (item.getDurability() > 0) {
            section.set("Item Data", item.getDurability());
        }

        if (ItemUtil.isMonsterEgg(item)) {
            section.set("Item Data", ((SpawnEggMeta) meta).getSpawnedType().getTypeId());
        }

        section.set("Percentage", defaults.getInt("Percentage", 10));

        if (meta.hasDisplayName()) {
            section.set("Name", ItemUtil.getDisplayName(meta));
        }

        section.set("Amount", item.getAmount());

        if (meta.hasEnchants()) {
            setEnchantments(meta.getEnchants(), section);
        }

        if (meta instanceof EnchantmentStorageMeta) {
            final EnchantmentStorageMeta esm = (EnchantmentStorageMeta) meta;
            setEnchantments(esm.getStoredEnchants(), section);
        }

        if (meta.hasLore()) {
            section.set("Lore", ItemUtil.getLore(meta));
        }

        if (meta.getItemFlags().size() > 0) {
            section.set("Flags", meta.getItemFlags().stream().map(ItemFlag::toString).collect(Collectors.toList()));
        }
    }

    public void setEnchantments(final Map<Enchantment, Integer> enchantments, final ConfigurationSection section) {
        final List<String> list = section.getStringList("Enchantments");
        enchantments.forEach((enchantment, level) -> list.add(enchantment.getName() + "-" + level));
        section.set("Enchantments", list);
    }
}
