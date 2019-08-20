package me.gabytm.guihelper.commands;

import me.gabytm.guihelper.GUIHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import static me.gabytm.guihelper.utils.StringUtils.*;

public class GHCreateCommand implements CommandExecutor {
    private GUIHelper plugin;

    public GHCreateCommand(GUIHelper plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("guihelper.use")) {
                if (args.length == 0) {
                    if (!plugin.getGuiList().containsKey(player.getUniqueId())) {
                        Inventory GUI = Bukkit.createInventory(player, 54, "GUIHelper");

                        player.openInventory(GUI);
                        plugin.getGuiList().put(player.getUniqueId(), GUI);
                        player.sendMessage(colorize("&cGUI not found, creating one!"));
                    } else {
                        player.openInventory(plugin.getGuiList().get(player.getUniqueId()));
                        player.sendMessage(colorize("&aGUI found."));
                    }
                } else {
                    switch (args[0].toLowerCase()) {
                        case "deluxemenus":
                            plugin.getGuiHandler().deluxeMenus().test("Test!", player);
                            break;
                        default:
                            player.sendMessage("default");
                            break;
                    }
                }
            }
        } else {
            sender.sendMessage(colorize("&c[GUIHelper] Only players can run this command!"));
            return true;
        }
        return true;
    }
}
