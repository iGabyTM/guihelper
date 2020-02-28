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

package me.gabytm.guihelper;

import me.gabytm.guihelper.commands.*;
import me.gabytm.guihelper.data.InventoryManager;
import me.gabytm.guihelper.generators.TypesManager;
import me.gabytm.guihelper.listeners.InventoryCloseListener;
import me.gabytm.guihelper.listeners.PlayerQuitListener;
import me.gabytm.guihelper.template.TemplateManager;
import me.gabytm.guihelper.utils.StringUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class GUIHelper extends JavaPlugin {
    private InventoryManager inventoryManager;
    private TemplateManager templateManager;
    private TypesManager typesManager;
    private final String version = getDescription().getVersion();

    @Override
    public void onEnable() {
        final Metrics metrics = new Metrics(this);
        inventoryManager = new InventoryManager();
        templateManager = new TemplateManager();
        typesManager = new TypesManager(this);

        saveDefaultConfig();
        templateManager.loadTemplates(getConfig().getConfigurationSection("templates"));
        StringUtil.consoleText(getServer().getConsoleSender(), version);
        loadCommands();
        loadEvents();
    }

    private void loadCommands() {
        final CommandTabCompleter tabCompleter = new CommandTabCompleter(templateManager);

        getCommand("ghcreate").setExecutor(new CreateCommand(typesManager, inventoryManager));
        getCommand("ghcreate").setTabCompleter(tabCompleter);
        getCommand("ghempty").setExecutor(new EmptyCommand(inventoryManager));
        getCommand("ghhelp").setExecutor(new HelpCommand(version));
        getCommand("ghlist").setExecutor(new ListCommand(version));
        getCommand("ghreload").setExecutor(new ReloadCommand(this, templateManager));
        getCommand("ghtemplate").setExecutor(new TemplateCommand(this, inventoryManager, templateManager));
        getCommand("ghtemplate").setTabCompleter(tabCompleter);
    }

    private void loadEvents() {
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(version), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(inventoryManager), this);
    }
}
