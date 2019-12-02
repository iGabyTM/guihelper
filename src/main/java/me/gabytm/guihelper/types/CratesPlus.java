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

import java.util.ArrayList;
import java.util.List;

public class CratesPlus {
    private GUIHelper plugin;

    CratesPlus(GUIHelper plugin) {
        this.plugin = plugin;
    }

    /**
     * Generate a shop config
     *
     * @param gui    the gui from where the items are took
     * @param player the command sender
     */
    public void generate(Inventory gui, Player player) {
        try {
            long start = System.currentTimeMillis();

            plugin.emptyConfig();
            plugin.getConfig().createSection("Crates.GUIHelper.Winnings");

            for (int slot = 0; slot < gui.getSize(); slot++) {
                if (!ItemUtil.slotIsEmpty(gui.getItem(slot))) {
                    addItem("Crates.GUIHelper.Winnings." + (slot + 1) + ".", gui.getItem(slot));
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
     * Create a shop config
     *
     * @param path the path
     * @param item the item
     */
    private void addItem(String path, ItemStack item) {
        FileConfiguration config = plugin.getConfig();
        ItemMeta meta = item.getItemMeta();

        config.set(path + "Type", "ITEM");
        config.set(path + "Item Type", item.getType().toString());

        if (item.getDurability() > 0) config.set(path + "Item Data", item.getDurability());

        if (ItemUtil.isMonsterEgg(item)) config.set(path + ".Item Data", ((SpawnEggMeta) meta).getSpawnedType().getTypeId());

        config.set(path + "Percentage", 10);

        if (meta.hasDisplayName()) config.set(path + "Name", meta.getDisplayName().replaceAll("§", "&"));

        config.set(path + "Amount", item.getAmount());

        if (meta.hasEnchants()) {
            List<String> enchantments = new ArrayList<>();

            meta.getEnchants().keySet().forEach(en -> enchantments.add(en.getName() + "-" + meta.getEnchantLevel(en)));
            config.set(path + "Enchantments", enchantments);
        }

        if (meta.hasLore()) config.set(path + "Lore", ItemUtil.getLore(meta));

        if (meta.getItemFlags().size() > 0) {
            List<String> flags = new ArrayList<>();

            meta.getItemFlags().forEach(flag -> flags.add(flag.toString()));
            config.set(path + "Flags", flags);
        }
    }
}
