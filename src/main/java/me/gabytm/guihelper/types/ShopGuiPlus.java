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
import org.bukkit.inventory.meta.SpawnEggMeta;

public class ShopGuiPlus {
    private GUIHelper plugin;

    ShopGuiPlus(GUIHelper plugin) {
        this.plugin = plugin;
    }

    /**
     * Generate a shop config
     * @param gui the gui from where the items are took
     * @param player the command sender
     * @param page the shop page (default 1)
     */
    public void generate(Inventory gui, Player player, int page) {
        try {
            long start = System.currentTimeMillis();

            plugin.emptyConfig();
            plugin.getConfig().createSection("shops.GUIHelper.items");

            for (int slot = 0; slot < gui.getSize(); slot++) {
                if (!ItemUtil.slotIsEmpty(gui.getItem(slot))) {
                    addItem("shops.GUIHelper.items.P" + page + "-" + slot + ".", gui.getItem(slot), slot, page);
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
     * @param path the path
     * @param item the item
     * @param slot the {@param item} slot
     * @param page the shop page (default 1)
     */
    @SuppressWarnings("Duplicates")
    private void addItem(String path, ItemStack item, int slot, int page) {
        FileConfiguration config = plugin.getConfig();
        ItemMeta meta = item.getItemMeta();

        config.set(path + "type", "item");
        config.set(path + "item.material", item.getType().toString());
        config.set(path + "item.quantity", item.getAmount());

        if (item.getDurability() > 0) config.set(path + "item.damage", item.getDurability());

        if (ItemUtil.isMonsterEgg(item)) config.set(path + "item.damage", ((SpawnEggMeta) meta).getSpawnedType().getTypeId());

        if (meta.hasDisplayName()) config.set(path + "item.name", ItemUtil.getDisplayName(meta));

        if (meta.hasLore()) config.set(path + "item.lore", ItemUtil.getLore(meta));

        config.set(path + "buyPrice", 10);
        config.set(path + "sellPrice", 10);
        config.set(path + "slot", slot);
        config.set(path + "page", page);
    }
}