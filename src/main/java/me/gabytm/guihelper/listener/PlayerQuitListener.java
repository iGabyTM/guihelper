package me.gabytm.guihelper.listener;

import me.gabytm.guihelper.GUIHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private GUIHelper plugin;

    public PlayerQuitListener(GUIHelper plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (plugin.getGuiList().containsKey(player.getUniqueId())) {
            plugin.getGuiList().remove(player.getUniqueId());
        }
    }
}
