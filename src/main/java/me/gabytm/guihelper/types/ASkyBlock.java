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
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ASkyBlock {
    private GUIHelper plugin;

    ASkyBlock(GUIHelper plugin) {
        this.plugin = plugin;
    }

    /**
     * Generate an island mini shop config
     *
     * @param gui    the gui from where the items are took
     * @param player the command sender
     */
    public void generate(Inventory gui, Player player) {
        try {
            long start = System.currentTimeMillis();

            plugin.emptyConfig();
            plugin.getConfig().createSection("items");

            for (int slot = 0; slot < gui.getSize(); slot++) {
                if (!ItemUtil.slotIsEmpty(gui.getItem(slot))) {
                    addItem("items.item" + (slot + 1) + ".", gui.getItem(slot));
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
     */
    private void addItem(String path, ItemStack item) {
        FileConfiguration config = plugin.getConfig();
        ItemMeta meta = item.getItemMeta();

        config.set(path + "material", item.getType().toString());
        config.set(path + "quantity", item.getAmount());
        config.set(path + "price", 100);
        config.set(path + "sellprice", 10);

        if (meta.hasLore()) {
            StringBuilder description = new StringBuilder();

            for (String line : meta.getLore()) {
                if (description.length() > 0) {
                    description.append("|").append(line);
                } else {
                    description.append(line);
                }
            }

            config.set(path + "description", description.toString().replaceAll("§", "&"));
        }
    }
}
