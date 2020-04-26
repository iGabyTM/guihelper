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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Collectors;

/**
 * @author HSGamer
 * @plugin https://spigotmc.org/resources/75620/
 */
public final class BetterGUI implements IGeneratorSlot {
    private final GUIHelper plugin;

    public BetterGUI(final GUIHelper plugin) {
        this.plugin = plugin;
    }

    @Override
    public void generate(final Inventory gui, final Player player) {
        final long start = System.currentTimeMillis();
        final Config config = new Config("BetterGUI/menu", plugin);

        config.empty();

        for (int slot = 0; slot < gui.getSize(); slot++) {
            final ItemStack item = gui.getItem(slot);

            if (ItemUtil.isNull(item)) continue;

            final String path = "item-" + slot;

            addItem(config.get().createSection(path), item, slot);
        }

        config.save();
        Message.CREATION_DONE.setDuration(System.currentTimeMillis() - start).send(player);
    }

    @Override
    public void addItem(final ConfigurationSection section, final ItemStack item, final int slot) {
        final ItemMeta meta = item.getItemMeta();

        section.set("id", item.getType().toString());

        if (item.getDurability() > 0) {
            section.set("damage", item.getDurability());
        }

        if (item.getAmount() > 1) {
            section.set("amount", item.getAmount());
        }

        section.set("slot", slot);

        if (meta.hasDisplayName()) {
            section.set("name", ItemUtil.getDisplayName(meta));
        }

        if (meta.hasLore()) {
            section.set("lore", ItemUtil.getLore(meta));
        }

        if (meta.hasEnchants()) {
            section.set("enchant", meta.getEnchants().entrySet().stream()
                    .map(entry -> entry.getKey().getName() + "," + entry.getValue())
                    .collect(Collectors.toList()));
        }

        if (meta.getItemFlags().size() > 0) {
            section.set("flag", meta.getItemFlags().stream().map(Enum::name).collect(Collectors.toList()));
        }
    }
}
