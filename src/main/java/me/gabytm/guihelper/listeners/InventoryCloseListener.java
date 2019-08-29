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

package me.gabytm.guihelper.listeners;

import me.gabytm.guihelper.GUIHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import static me.gabytm.guihelper.utils.StringUtils.colorize;

public class InventoryCloseListener implements Listener {
    private GUIHelper plugin;

    public InventoryCloseListener(GUIHelper plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    @SuppressWarnings("Duplicates")
    public void onInventoryClose(InventoryCloseEvent event) {
        if (plugin.getGuiList().containsValue(event.getInventory())) {
            Player player = (Player) event.getPlayer();

            player.sendMessage(" ");
            player.sendMessage(colorize("&7GUIHelper v1.1"));
            player.sendMessage(colorize("  &2ASkyBlock &8- &fIsland minishop items"));
            player.sendMessage(colorize("  &2ChestCommands &8- &fMenu items"));
            player.sendMessage(colorize("  &2CrazyCrates &8- &fCrate prizes"));
            player.sendMessage(colorize("  &2DeluxeMenus &8- &fExternal menu"));
            player.sendMessage(colorize("  &2DeluxeMenusLocal &8- &fLocal menu &7(config.yml)"));
            player.sendMessage(colorize("  &2ShopGuiPlus &a(page) &8- &fShop items"));
            player.sendMessage(" ");
            player.sendMessage(colorize("&7Usage: &2&o/ghcreate [type] &a&o(argument)"));
        }
    }
}
