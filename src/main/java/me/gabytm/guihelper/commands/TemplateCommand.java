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
import me.gabytm.guihelper.data.Config;
import me.gabytm.guihelper.data.InventoryManager;
import me.gabytm.guihelper.template.TemplateManager;
import me.gabytm.guihelper.utils.Message;
import me.gabytm.guihelper.utils.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TemplateCommand implements CommandExecutor {
    private final GUIHelper plugin;
    private final InventoryManager inventoryManager;
    private final TemplateManager templateManager;

    public TemplateCommand(final GUIHelper plugin, final InventoryManager inventoryManager, final TemplateManager templateManager) {
        this.plugin = plugin;
        this.inventoryManager = inventoryManager;
        this.templateManager = templateManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, String[] args) {
        if (!(sender instanceof Player)) {
            Message.PLAYERS_ONLY.send(sender);
            return true;
        }

        final Player player = (Player) sender;

        if (!player.hasPermission(GUIHelper.PERMISSION)) {
            return true;
        }

        if (args.length == 0) {
            Message.TEMPLATE_USAGE.send(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            final List<String> templates = templateManager.getTemplates();

            if (templates.size() == 0) {
                Message.NO_TEMPLATES.send(player);
                return true;
            }

            Collections.sort(templates);
            player.sendMessage(StringUtil.color("&2Available templates: &a" + String.join("&7, &a", templates)));
            return true;
        }

        final Inventory inventory = inventoryManager.get(player.getUniqueId());

        if (inventoryManager.isEmpty(inventory)) {
            Message.EMPTY_GUI.send(player);
            return true;
        }

        if (templateManager.getTemplate(args[0]) == null) {
            Message.WRONG_TEMPLATE.format(args[0]).send(player);
            return true;
        }

        final Config config = new Config("template-" + args[0] + ".yml", plugin);
        config.empty();

        if (args.length > 1) {
            templateManager.generate(args[0], config, inventoryManager.get(player.getUniqueId()), player, Arrays.copyOfRange(args, 1, args.length));
            return true;
        }

        templateManager.generate(args[0], config, inventoryManager.get(player.getUniqueId()), player, new String[]{});
        return true;
    }
}
