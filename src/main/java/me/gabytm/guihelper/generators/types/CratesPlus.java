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
import me.gabytm.guihelper.utils.StringUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.stream.Collectors;

public class CratesPlus implements IGenerator {
    private FileConfiguration defaultConfig;

    public CratesPlus(GUIHelper plugin) {
        defaultConfig = plugin.getConfig();
    }

    @Override
    public void generate(Inventory gui, Player player) {
        try {
            final long start = System.currentTimeMillis();
            final Config config = new Config("CratesPlus");

            config.empty();

            for (int slot = 0; slot < gui.getSize(); slot++) {
                final ItemStack item = gui.getItem(slot);

                if (ItemUtil.isNull(item)) continue;

                final String path = "Crates.GUIHelper.Winnings." + (slot + 1);

                addItem(config.get().createSection(path), gui.getItem(slot));
            }

            config.save();
            Message.CREATION_DONE.format(System.currentTimeMillis() - start).send(player);
        } catch (Exception e) {
            StringUtil.saveError(e);
            Message.CREATION_ERROR.send(player);
        }
    }

    @Override
    public void addItem(ConfigurationSection section, ItemStack item) {
        final ConfigurationSection defaults = defaultConfig.getConfigurationSection("CratesPlus");
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
            section.set("Enchantments", meta.getEnchants().keySet().stream().map(en -> en.getName() + "-" + meta.getEnchantLevel(en)).collect(Collectors.toList()));
        }

        if (meta.hasLore()) {
            section.set("Lore", ItemUtil.getLore(meta));
        }

        if (meta.getItemFlags().size() > 0) {
            section.set("Flags", meta.getItemFlags().stream().map(ItemFlag::toString).collect(Collectors.toList()));
        }
    }
}
