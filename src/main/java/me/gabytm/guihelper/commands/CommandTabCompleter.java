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

import me.gabytm.guihelper.template.TemplateManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class CommandTabCompleter implements TabCompleter {
    private TemplateManager templateManager;

    public CommandTabCompleter(TemplateManager templateManager) {
        this.templateManager = templateManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
        if (!s.hasPermission("guihelper.use")) {
            return null;
        }

        if (cmd.getName().equalsIgnoreCase("ghcreate")) {
            if (args.length > 1) {
                return Collections.singletonList("");
            }

            final List<String> types = Arrays.asList("ASkyBlock", "BossShopPro", "BossShopProMenu", "ChestCommands", "CratesPlus", "CrazyCrates", "CrazyEnvoy", "DeluxeMenus", "DeluxeMenusLocal", "GUIShop", "ShopGuiPlus", "SuperLobbyDeluxe");
            return order(args[0], types);
        }

        if (cmd.getName().equalsIgnoreCase("ghtemplate")) {
            if (args.length > 1) {
                return Collections.singletonList("");
            }

            return order(args[0], templateManager.getTemplates());
        }

        return null;
    }

    private List<String> order(String arg, List<String> list) {
        final List<String> completions = new ArrayList<>();

        StringUtil.copyPartialMatches(arg, list, completions);
        Collections.sort(completions);
        return completions;
    }
}
