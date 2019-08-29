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

package me.gabytm.guihelper;

import me.gabytm.guihelper.commands.GHCreateCommand;
import me.gabytm.guihelper.commands.GHHelpCommand;
import me.gabytm.guihelper.commands.GHListCommand;
import me.gabytm.guihelper.utils.TabCompleterUtil;
import me.gabytm.guihelper.types.GuiHandler;
import me.gabytm.guihelper.listeners.InventoryCloseListener;
import me.gabytm.guihelper.listeners.PlayerQuitListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class GUIHelper extends JavaPlugin {

    private Map<UUID, Inventory> guiList;
    private GuiHandler guiHandler;

    private int aSkyBlockCounter;
    private int chestCommandsCounter;
    private int deluxeMenusCounter;

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this);
        guiList = new HashMap<>();
        guiHandler = new GuiHandler(this);

        getCommand("ghcreate").setExecutor(new GHCreateCommand(this));
        getCommand("ghcreate").setTabCompleter(new TabCompleterUtil());
        getCommand("ghhelp").setExecutor(new GHHelpCommand(this));
        getCommand("ghlist").setExecutor(new GHListCommand(this));

        getServer().getPluginManager().registerEvents(new InventoryCloseListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);

        /*metrics.addCustomChart(new Metrics.AdvancedPie("configsGenerated", () -> {
            Map<String, Integer> map = new HashMap<>();

            map.put("aSkyBlock", aSkyBlockCounter);
            map.put("ChestCommands", chestCommandsCounter);
            map.put("DeluxeMenus", deluxeMenusCounter);

            return map;
        }));*/
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Map<UUID, Inventory> getGuiList() {
        return guiList;
    }
    public GuiHandler getGuiHandler() { return guiHandler; }

    public void updateMetrics(String chart, String type, int value) {
    }

    public void addMetrics(String type, int value) {
        switch (type) {
            case "aSkyBlock":
                aSkyBlockCounter += value;
                break;
            case "ChestCommands":
                chestCommandsCounter += value;
                break;
            case "DeluxeMenus":
                deluxeMenusCounter += value;
                break;
        }
    }
}
