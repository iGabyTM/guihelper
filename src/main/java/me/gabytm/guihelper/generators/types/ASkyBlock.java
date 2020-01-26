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
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class ASkyBlock implements IGenerator {
    private FileConfiguration defaultConfig;

    public ASkyBlock(GUIHelper plugin) {
        this.defaultConfig = plugin.getConfig();
    }

    /**
     * Generate an island mini shop config
     *
     * @param inventory the inventory from where the items are took
     * @param player    the command sender
     */
    @Override
    public void generate(Inventory inventory, Player player) {
        try {
            final long start = System.currentTimeMillis();
            final Config config = new Config("ASkyBlock");

            config.empty();
            config.get().createSection("items");

            for (int slot = 0; slot < inventory.getSize(); slot++) {
                final ItemStack item = inventory.getItem(slot);

                if (ItemUtil.isItem(item)) {
                    final String path = "items.item" + (slot + 1);

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

    /**
     * Create a shop item
     *
     * @param item the item
     */
    @Override
    public void addItem(ConfigurationSection section, ItemStack item) {
        final ConfigurationSection defaults = defaultConfig.getConfigurationSection("ASkyBlock");
        final ItemMeta meta = item.getItemMeta();

        section.set("material", item.getType().toString());
        section.set("quantity", item.getAmount());
        section.set("price", defaults.getInt("price"));
        section.set("sellprice", defaults.getInt("sellprice"));

        if (meta.hasLore()) {
            final StringBuilder description = new StringBuilder();

            for (String line : meta.getLore()) {
                if (description.length() > 0) description.append("|");

                description.append(line);
            }

            section.set("description", description.toString().replace(String.valueOf(ChatColor.COLOR_CHAR), "&"));
        }
    }
}
