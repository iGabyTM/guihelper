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

import me.gabytm.guihelper.HelperHolder;
import me.gabytm.guihelper.data.IManager;
import me.gabytm.guihelper.generators.TypesManager;
import me.gabytm.guihelper.utils.Message;
import me.gabytm.guihelper.utils.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CreateCommand implements CommandExecutor {
    private TypesManager manager;
    private IManager iManager;

    public CreateCommand(TypesManager manager, IManager iManager) {
        this.manager = manager;
        this.iManager = iManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Message.PLAYERS_ONLY.send(sender);
            return true;
        }

        final Player player = (Player) sender;
        final UUID uuid = player.getUniqueId();

        if (!player.hasPermission("guihelper.use")) return true;

        if (args.length == 0) {
            if (!iManager.exists(uuid)) {
                final Inventory inventory = Bukkit.createInventory(new HelperHolder(), 54, "GUIHelper");

                player.openInventory(inventory);
                iManager.add(uuid, inventory);
                return true;
            }

            player.openInventory(iManager.get(uuid));
            return true;
        }

        if (!iManager.exists(uuid) || isEmpty(iManager.get(uuid))) {
            Message.EMPTY_GUI.send(player);
            return true;
        }

        final Inventory inventory = iManager.get(uuid);

        switch (args[0].toLowerCase()) {
            // Updated
            case "askyblock": {
                manager.aSkyBlock().generate(inventory, player);
                break;
            }

            // Updated
            case "bossshoppro": {
                manager.bossShopPro().generate(inventory, player);
                break;
            }

            // Updated
            case "bossshoppromenu": {
                manager.bossShopProMenu().generate(inventory, player);
                break;
            }

            // Updated
            case "chestcommands": {
                manager.chestCommands().generate(inventory, player);
                break;
            }

            // Updated
            case "cratesplus": {
                manager.cratesPlus().generate(inventory, player);
                break;
            }

            // Updated
            case "crazycrates": {
                manager.crazyCrates().generate(inventory, player, NumberUtil.getPage(args, 1, 1));
                break;
            }

            // Updated
            case "crazyenvoy": {
                manager.crazyEnvoy().generate(inventory, player, NumberUtil.getPage(args, 1, 1));
                break;
            }

            // Updated
            case "deluxemenus": {
                manager.deluxeMenus().generateExternal(inventory, player);
                break;
            }

            // Updated
            case "deluxemenuslocal": {
                manager.deluxeMenus().generateLocal(inventory, player);
                break;
            }

            // Updated
            case "lemonmobcoins": {
                manager.lemonMobCoins().generate(inventory, player);
                break;
            }

            // Updated
            case "shopguiplus": {
                manager.shopGuiPlus().generate(inventory, player, NumberUtil.getPage(args, 1, 1));
                break;
            }

            default: {
                Message.WRONG_TYPE.format(args[0]).send(player);
                break;
            }
        }

        return true;
    }

    /**
     * Check if the gui it's empty or not
     *
     * @param gui the gui
     * @return boolean
     */
    private boolean isEmpty(Inventory gui) {
        for (ItemStack item : gui.getContents()) {
            if (item != null) return false;
        }

        return true;
    }
}
