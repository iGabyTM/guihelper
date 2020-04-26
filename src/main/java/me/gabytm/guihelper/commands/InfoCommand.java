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

package me.gabytm.guihelper.commands;

import me.gabytm.guihelper.GUIHelper;
import me.gabytm.guihelper.utils.ItemUtil;
import me.gabytm.guihelper.utils.Message;
import me.gabytm.guihelper.utils.StringUtil;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(GUIHelper.PERMISSION)) {
            return true;
        }

        if (!(sender instanceof Player)) {
            Message.PLAYERS_ONLY.send(sender);
            return true;
        }

        final Player player = (Player) sender;
        final ItemStack hand = ItemUtil.getItemInHand(player);

        if (ItemUtil.isNull(hand)) {
            Message.INFO_NO_ITEM.send(player);
            return true;
        }

        final ItemMeta meta = hand.getItemMeta();
        final StringBuilder builder = new StringBuilder("\n&aMaterial: &7").append(hand.getType().name()).append("&8:&7").append(hand.getDurability());
        final List<String> enchantments = new ArrayList<>();

        if (ItemUtil.isLeatherArmor(hand)) {
            final LeatherArmorMeta lam = (LeatherArmorMeta) meta;
            final Color color = lam.getColor();
            final int red = color.getRed();
            final int green = color.getGreen();
            final int blue = color.getBlue();

            builder.append("\n&aColor: ")
                    .append("&7RGB &8(&c").append(red).append("&f, &a").append(green).append("&f, &9").append(blue).append("&8)&f, ")
                    .append("&7HEX &8(&f#").append(String.format("%02X%02X%02X", red, green, blue)).append("&8)");
        }

        if (meta.hasEnchants()) {
            enchantments.addAll(meta.getEnchants().entrySet().stream().map(entry -> "&7" + entry.getKey().getName() + "&8:" + "&7" + entry.getValue()).collect(Collectors.toList()));
        }

        if (meta instanceof EnchantmentStorageMeta) {
            final EnchantmentStorageMeta esm = (EnchantmentStorageMeta) meta;

            if (esm.hasStoredEnchants()) {
                enchantments.addAll(esm.getStoredEnchants().entrySet().stream().map(entry -> "&7" + entry.getKey().getName() + "&8:" + "&7" + entry.getValue()).collect(Collectors.toList()));
            }
        }

        if (enchantments.size() > 0) {
            builder.append("\n&aEnchantments: ").append(String.join("&f, ", enchantments));
        }

        if (meta.getItemFlags().size() > 0) {
            builder.append("\n&aFlags: &7").append(meta.getItemFlags().stream().map(ItemFlag::name).collect(Collectors.joining("&f, &7")));
        }

        sender.sendMessage(StringUtil.color(builder.append("\n ").toString()));
        return false;
    }
}
