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
import me.gabytm.guihelper.generators.generators.IGeneratorPage;
import me.gabytm.guihelper.utils.ItemUtil;
import me.gabytm.guihelper.utils.Message;
import me.gabytm.guihelper.utils.StringUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.ArrayList;
import java.util.List;

public final class CrazyCrates implements IGeneratorPage {
    private ConfigurationSection defaultConfig;

    public CrazyCrates(GUIHelper plugin) {
        defaultConfig = plugin.getConfig();
    }

    @Override
    public void generate(Inventory gui, Player player, int page) {
        try {
            long start = System.currentTimeMillis();
            Config config = new Config("CrazyCrates/crates");

            config.empty();
            config.get().createSection("Crates.Prizes");

            for (int slot = 0; slot < gui.getSize(); slot++) {
                final ItemStack item = gui.getItem(slot);

                if (ItemUtil.isItem(item)) {
                    String path = "Crate.Prizes." + (page > 1 ? slot + 1 + (53 * (page - 1)) : slot);

                    addItem(config.get().createSection(path), item);
                }
            }

            config.save();
            Message.CREATION_DONE.format(System.currentTimeMillis() - start).send(player);
        } catch (Exception e) {
            StringUtil.saveError(e);
            Message.CREATION_ERROR.send(player);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void addItem(ConfigurationSection section, ItemStack item) {
        final ConfigurationSection defaults = defaultConfig.getConfigurationSection("CrazyCrates");
        final ItemMeta meta = item.getItemMeta();
        final StringBuilder rewardItem = new StringBuilder();
        final StringBuilder rewardItemMaterial = new StringBuilder();
        final StringBuilder rewardItemAmount = new StringBuilder();
        final StringBuilder rewardItemDisplayName = new StringBuilder();
        final StringBuilder rewardItemLore = new StringBuilder();
        final StringBuilder rewardItemEnchantments = new StringBuilder();
        final List<String> rewardItemsList = new ArrayList<>();
        final String itemType = item.getType().toString();

        if (meta.hasDisplayName()) {
            section.set("DisplayName", ItemUtil.getDisplayName(meta));
            rewardItemDisplayName.append(", Name:").append(ItemUtil.getDisplayName(meta));
        }

        section.set("DisplayItem", itemType);
        rewardItemMaterial.append("Item:").append(itemType);

        if (ItemUtil.isMonsterEgg(item)) {
            section.set("DisplayItem", itemType + ":" + ((SpawnEggMeta) meta).getSpawnedType().getTypeId());
            rewardItemMaterial.append(":").append(((SpawnEggMeta) meta).getSpawnedType().getTypeId());
        } else if (item.getType().toString().contains("TIPPED_ARROW")) {
            section.set("DisplayItem", itemType + ":" + ((PotionMeta) meta).getBasePotionData().getType().toString());
            rewardItemMaterial.append(":").append(((PotionMeta) meta).getBasePotionData().getType().toString());
        }

        if (item.getDurability() > 0) {
            section.set("DisplayItem", itemType + ":" + item.getDurability());
            rewardItemMaterial.append(":").append(item.getDurability());
        }

        section.set("DisplayAmount", item.getAmount());
        rewardItemAmount.append(", Amount:").append(item.getAmount());

        if (meta.hasLore()) {
            final List<String> lore = new ArrayList<>();

            rewardItemLore.append(", Lore:");

            for (String line : ItemUtil.getLore(meta)) {
                lore.add(line);
                rewardItemLore.append(line).append(",");
            }

            section.set("Lore", lore);
        }

        if (meta.hasEnchants()) {
            final List<String> enchantments = new ArrayList<>();

            for (Enchantment en : meta.getEnchants().keySet()) {
                enchantments.add(en.getName() + ":" + meta.getEnchantLevel(en));
                rewardItemEnchantments.append(", ").append(en.getName()).append(":").append(meta.getEnchantLevel(en));
            }

            section.set("DisplayEnchantments", enchantments);
        }

        section.set("MaxRange", defaults.getInt("MaxRange", 100));
        section.set("Chance", defaults.getInt("Chance", 10));
        section.set("Firework", defaults.getBoolean("Firework"));
        rewardItem.append(rewardItemMaterial.toString()).append(rewardItemAmount.toString());

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
        section.set("Items", rewardItemsList);
    }
}
