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

package me.gabytm.guihelper.commands;

import me.gabytm.guihelper.GUIHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static me.gabytm.guihelper.utils.StringUtils.*;

public class GHListCommand implements CommandExecutor {
    private GUIHelper plugin;

    public GHListCommand(GUIHelper plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("guihelper.use")) {
            sender.sendMessage(" ");
            sender.sendMessage(colorize("&7GUIHelper v1.0"));
            sender.sendMessage(colorize("  &2CrazyCrates &8- &fCrate prizes"));
            sender.sendMessage(colorize("  &2DeluxeMenus &8- &fExternal menu"));
            sender.sendMessage(colorize("  &2DeluxeMenusLocal &8- &fLocal menu &7(config.yml)"));
            sender.sendMessage(colorize("  &2ShopGuiPlus &a(page) &8- &fShop items"));
            sender.sendMessage(" ");
            sender.sendMessage(colorize("&7Usage: &2&o/ghcreate [type] &a&o(argument)"));
            return true;
        }
        return true;
    }
}
