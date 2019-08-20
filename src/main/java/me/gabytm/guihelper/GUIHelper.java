package me.gabytm.guihelper;

import me.gabytm.guihelper.commands.GHCreateCommand;
import me.gabytm.guihelper.guis.GuiHandler;
import me.gabytm.guihelper.listener.PlayerQuitListener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class GUIHelper extends JavaPlugin {

    private Map<UUID, Inventory> guiList;
    private GuiHandler guiHandler;

    @Override
    public void onEnable() {
        guiList = new HashMap<>();
        guiHandler = new GuiHandler(this);

        this.getCommand("ghcreate").setExecutor(new GHCreateCommand(this));

        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Map<UUID, Inventory> getGuiList() {
        return guiList;
    }
    public GuiHandler getGuiHandler() { return guiHandler; }
}
