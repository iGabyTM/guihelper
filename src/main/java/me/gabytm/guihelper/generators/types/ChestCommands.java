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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

public final class ChestCommands implements IGeneratorSlot {
    private GUIHelper plugin;

    public ChestCommands(final GUIHelper plugin) {
        this.plugin = plugin;
    }

    @Override
    public void generate(final Inventory gui, final Player player) {
        final long start = System.currentTimeMillis();
        final Config config = new Config("ChestCommands/menu", plugin);

        config.empty();

        for (int slot = 0; slot < gui.getSize(); slot++) {
            final ItemStack item = gui.getItem(slot);

            if (ItemUtil.isNull(item)) continue;

            final String path = "item-" + slot;

            addItem(config.get().getConfigurationSection(path), item, slot);
        }

        config.save();
        Message.CREATION_DONE.format(System.currentTimeMillis() - start).send(player);
    }

    @Override
    public void addItem(final ConfigurationSection section, final ItemStack item, final int slot) {
        final ItemMeta meta = item.getItemMeta();

        section.set("ID", item.getType().toString());

        if (item.getDurability() > 0) {
            section.set("DATA-VALUE", item.getDurability());
        }

        if (item.getAmount() > 1) {
            section.set("AMOUNT", item.getAmount());
        }

        setItemPosition(section, slot);

        if (meta.hasDisplayName()) {
            section.set("NAME", ItemUtil.getDisplayName(meta));
        }

        if (meta.hasLore()) {
            section.set("LORE", ItemUtil.getLore(meta));
        }

        if (meta.hasEnchants()) {
            StringBuilder enchantments = new StringBuilder();

            for (Enchantment en : meta.getEnchants().keySet()) {
                if (enchantments.length() > 0) enchantments.append(";");
                enchantments.append(en.getName()).append(",").append(meta.getEnchantLevel(en));
            }

            section.set("ENCHANTMENT", enchantments.toString());
        }

        if (ItemUtil.isLeatherArmor(item)) {
            LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();

            section.set("COLOR", lam.getColor().getRed() + ", " + lam.getColor().getGreen() + ", " + lam.getColor().getBlue());
        } else if (ItemUtil.isMonsterEgg(item)) {
            section.set("DATA-VALUE", ((SpawnEggMeta) meta).getSpawnedType().getTypeId());
        }
    }

    /**
     * Transform a slot into x and y position
     *
     * @param section the path where the position will be saved
     * @param slot    the item slot
     */
    private void setItemPosition(ConfigurationSection section, int slot) {
        final int y = Math.toIntExact(slot / 9 + 1);
        final int x = slot + 1 - (9 * (y - 1));

        section.set("POSITION-X", x);
        section.set("POSITION-Y", y);
    }
}
