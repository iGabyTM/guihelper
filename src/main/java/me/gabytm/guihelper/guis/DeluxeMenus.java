package me.gabytm.guihelper.guis;

import me.gabytm.guihelper.GUIHelper;
import org.bukkit.entity.Player;

public class DeluxeMenus {
    private GUIHelper plugin;

    DeluxeMenus(GUIHelper plugin) {
        this.plugin = plugin;
    }

    public void test(String input, Player player) {
        player.sendMessage(input);
    }
}
