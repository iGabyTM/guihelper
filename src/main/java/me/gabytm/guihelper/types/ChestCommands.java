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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.Collections;
import java.util.List;

public class ChestCommands {
    private GUIHelper plugin;

    ChestCommands(GUIHelper plugin) {
        this.plugin = plugin;
    }

    /**
     * Generate a menu config
     *
     * @param gui    the gui from where the items are took
     * @param player the command sender
     */
    public void generate(Inventory gui, Player player) {
        try {
            long start = System.currentTimeMillis();

            plugin.emptyConfig();

            for (int slot = 0; slot < gui.getSize(); slot++) {
                if (ItemUtil.slotIsEmpty(gui.getItem(slot))) {
                    plugin.getConfig().createSection("item-" + slot);
                    addItem("item-" + slot + ".", gui.getItem(slot), slot);
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
     * Create a menu item
     *
     * @param path the path
     * @param item the item
     * @param slot the {@param item} slot
     */
    private void addItem(String path, ItemStack item, int slot) {
        FileConfiguration config = plugin.getConfig();
        ItemMeta meta = item.getItemMeta();

        config.set(path + "ID", item.getType().toString());

        if (item.getDurability() > 0) config.set(path + "DATA-VALUE", item.getDurability());

        if (item.getAmount() > 1) config.set(path + "AMOUNT", item.getAmount());

        setItemPosition(config, path, slot);

        if (meta.hasDisplayName()) config.set(path + "NAME", meta.getDisplayName().replaceAll("§", "&"));

        if (meta.hasLore()) {
            List<String> lore = meta.getLore();

            Collections.replaceAll(lore, "§", "&");
            config.set(path + "LORE", lore);
        }

        if (meta.hasEnchants()) {
            StringBuilder enchantments = new StringBuilder();

            for (Enchantment en : meta.getEnchants().keySet()) {
                if (enchantments.length() > 0) {
                    enchantments.append(";").append(en.getName()).append(",").append(meta.getEnchantLevel(en));
                } else {
                    enchantments.append(en.getName()).append(",").append(meta.getEnchantLevel(en));
                }
            }

            config.set(path + "ENCHANTMENT", enchantments.toString());
        }

        if (ItemUtil.isLeatherArmor(item)) {
            LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();

            config.set(path + "COLOR", lam.getColor().getRed() + ", " + lam.getColor().getGreen() + ", " + lam.getColor().getBlue());
        } else if (ItemUtil.isMonsterEgg(item)) {
            config.set(path + "DATA-VALUE", ((SpawnEggMeta) meta).getSpawnedType().getTypeId());
        }
    }

    /**
     * Transform a slot into x and y position
     *
     * @param path the path where the position will be saved
     * @param slot the item slot
     */
    private void setItemPosition(FileConfiguration config, String path, int slot) {
        int y = Math.toIntExact(slot / 9 + 1);
        int x = slot + 1 - (9 * (y - 1));

        config.set(path + "POSITION-X", x);
        config.set(path + "POSITION-Y", y);
    }
}
