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

import java.util.stream.Collectors;

public final class DeluxeMenus {
    private GUIHelper plugin;

    public DeluxeMenus(GUIHelper plugin) {
        this.plugin = plugin;
    }

    public void generateExternal(final Inventory inventory, final Player player) {
        final long start = System.currentTimeMillis();
        final Config config = new Config("DeluxeMenus/gui_menus", plugin);

        config.empty();
        config.get().createSection("items");

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            final ItemStack item = inventory.getItem(slot);

            if (ItemUtil.isNull(item)) {
                continue;
            }

            final String path = "items." + slot;

            addItem(config.get().createSection(path), item, slot);
        }

        config.save();
        Message.CREATION_DONE.format(System.currentTimeMillis() - start).send(player);
    }

    public void generateLocal(final Inventory inventory, final Player player) {
        final long start = System.currentTimeMillis();
        final Config config = new Config("DeluxeMenus", plugin);

        config.empty();

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            final ItemStack item = inventory.getItem(slot);

            if (ItemUtil.isNull(item)) {
                continue;
            }

            final String path = "gui_menus.GUIHelper.items." + slot;

            addItem(config.get().createSection(path), item, slot);
        }

        config.save();
        Message.CREATION_DONE.format(System.currentTimeMillis() - start).send(player);
    }

    private void addItem(final ConfigurationSection section, final ItemStack item, final int slot) {
        final ItemMeta meta = item.getItemMeta();
        section.set("material", item.getType().toString());

        if (item.getDurability() > 0) {
            section.set("data", item.getDurability());
        }

        if (item.getAmount() > 1) {
            section.set("amount", item.getAmount());
        }

        if (ItemUtil.isLeatherArmor(item)) {
            LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();

            section.set("color", lam.getColor().getRed() + ", " + lam.getColor().getGreen() + ", " + lam.getColor().getBlue());
        } else if (ItemUtil.isMonsterEgg(item)) {
            section.set("data", ((SpawnEggMeta) meta).getSpawnedType().getTypeId());
        }

        //TODO Add support for banners

        section.set("slot", slot);

        if (meta.getItemFlags().size() > 0) {
            for (ItemFlag flag : meta.getItemFlags()) {
                switch (flag) {
                    case HIDE_ENCHANTS: {
                        section.set("hide_enchantments", true);
                        break;
                    }

                    case HIDE_ATTRIBUTES: {
                        section.set("hide_attributes", true);
                        break;
                    }

                    case HIDE_POTION_EFFECTS: {
                        section.set("hide_effects", true);
                        break;
                    }
                }
            }
        }

        if (meta.hasDisplayName()) {
            section.set("display_name", ItemUtil.getDisplayName(meta));
        }

        if (meta.hasLore()) {
            section.set("lore", ItemUtil.getLore(meta));
        }

        if (meta.hasEnchants()) {
            section.set("enchantments", meta.getEnchants().keySet().stream().map(en -> en.getName() + ";" + meta.getEnchantLevel(en)).collect(Collectors.toList()));
        }
    }
}