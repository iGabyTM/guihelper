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
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gabytm.guihelper.utils.Messages;

public class GHCreateCommand implements CommandExecutor {
    private GUIHelper plugin;

    public GHCreateCommand(GUIHelper plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("guihelper.use")) {
                if (args.length == 0) {
                    if (!plugin.getGuiList().containsKey(player.getUniqueId())) {
                        Inventory gui = Bukkit.createInventory(player, 54, "GUIHelper");

                        player.openInventory(gui);
                        plugin.getGuiList().put(player.getUniqueId(), gui);
                    } else {
                        player.openInventory(plugin.getGuiList().get(player.getUniqueId()));
                    }
                } else {
                    if (!plugin.getGuiList().containsKey(player.getUniqueId()) || isEmpty(plugin.getGuiList().get(player.getUniqueId()))) {
                        player.sendMessage(Messages.EMPTY_GUI.format(null, null, null));
                        return true;
                    } else {
                        Inventory gui = plugin.getGuiList().get(player.getUniqueId());

                        switch (args[0].toLowerCase()) {
                            case "askyblock":
                                plugin.getGuiHandler().aSkyBlock().generate(gui, player);
                                break;
                            case "chestcommands":
                                plugin.getGuiHandler().chestCommands().generate(gui, player);
                                break;
                            case "crazycrates":
                                if (args.length >= 2 && isInteger(args[1])) {
                                    int page = Integer.parseInt(args[1]) >= 2 ? Integer.parseInt(args[1]) : 1;

                                    plugin.getGuiHandler().crazyCrates().generate(gui, player, page);
                                } else {
                                    plugin.getGuiHandler().crazyCrates().generate(gui, player, 1);
                                }
                                break;
                            case "crazyenvoy":
                                if (args.length >= 2 && isInteger(args[1])) {
                                    int page = Integer.parseInt(args[1]) >= 2 ? Integer.parseInt(args[1]) : 1;

                                    plugin.getGuiHandler().crazyEnvoy().generate(gui, player, page);
                                } else {
                                    plugin.getGuiHandler().crazyEnvoy().generate(gui, player, 1);
                                }
                                break;
                            case "deluxemenus":
                                plugin.getGuiHandler().deluxeMenus().generateExternal(gui, player);
                                break;
                            case "deluxemenuslocal":
                                plugin.getGuiHandler().deluxeMenus().generateLocal(gui, player);
                                break;
                            /*case "guishop":
                                plugin.getGuiHandler().guiShop().generate(gui, player);
                                break;*/
                            case "lemonmobcoins":
                                plugin.getGuiHandler().lemonMobCoins().generate(gui, player);
                                break;
                            case "shopguiplus":
                                if (args.length >= 2 && isInteger(args[1])) {
                                    int page = Integer.parseInt(args[1]) >= 2 ? Integer.parseInt(args[1]) : 1;

                                    plugin.getGuiHandler().shopGuiPlus().generate(gui, player, page);
                                } else {
                                    plugin.getGuiHandler().shopGuiPlus().generate(gui, player, 1);
                                }
                                break;
                            default:
                                player.sendMessage(Messages.WRONG_TYPE.format(args[0], null, null));
                                break;
                        }
                    }
                }
            }
        } else {
            sender.sendMessage(Messages.PLAYERS_ONLY.format(null, null, null));
            return true;
        }
        return true;
    }

    /**
     * Check if the gui it's empty or not
     * @param gui the gui
     * @return boolean
     */
    private boolean isEmpty(Inventory gui) {
        for(ItemStack item : gui.getContents()) {
            if(item != null) return false;
        }

        return true;
    }

    /**
     * Check if the provided {@param input} it's an integer or not
     * @param input the input
     * @return boolean
     */
    private static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
}
