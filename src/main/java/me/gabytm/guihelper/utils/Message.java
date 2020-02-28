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

package me.gabytm.guihelper.utils;

import org.bukkit.command.CommandSender;

public enum Message {
    CLEAR("&aThe GUI has been cleared and is ready to use!"),
    CREATION_DONE("&aDone! &7({duration}ms)"),
    CREATION_ERROR("&cSomething went wrong, please check the console."),
    EMPTY_GUI("&cPlease add some items to the GUI first!"),
    HELP("\n" +
            "&7GUIHelper &fv{version} &7by GabyTM\n" +
            "  &2/ghcreate [type] &a(argument) &8- &fCreate a config\n" +
            "  &2/ghempty &8- &fEmpty the GUI faster\n" +
            "  &2/ghhelp &8- &fDisplay the commands list\n" +
            "  &2/ghlist &8- &fDisplay the supported plugins list\n" +
            "  &2/ghreload &8- &fReload the plugin\n" +
            "  &2/ghtemplate [list|template] &8- &fList all available templates or create a config following a template"),
    NO_TEMPLATES("&cNo templates found."),
    PLAYERS_ONLY("&cOnly players can run this command!"),
    RELOAD("&aThe plugin has been successfully reloaded!"),
    TEMPLATE_USAGE("&cUsage: &7/ghtemplate [list|template]"),
    TYPES_LIST("\n" +
            "&7GUIHelper &fv{version} &7GabyTM\n" +
            "  &2ASkyBlock &8- &fIsland minishop items\n" +
            "  &2BossShopPro &8- &fShop items\n" +
            "  &2BossShopProMenu &8- &fMenu items\n" +
            "  &2ChestCommands &8- &fMenu items\n" +
            "  &2CratesPlus &8- &fCrate prizes\n" +
            "  &2CrazyCrates &a(page) &8- &fCrate prizes\n" +
            "  &2CrazyEnvoy &a(page) &8- &fEnvoy items\n" +
            "  &2DeluxeMenus &8- &fExternal menu\n" +
            "  &2DeluxeMenusLocal &8- &fLocal menu &7(config.yml)\n" +
            //"  &2GUIShop &8- &fShop items\n" +
            "  &2LemonMobCoins &8- &fShop items\n" +
            "  &2ShopGuiPlus &a(page) &8- &fShop items\n" +
            "  &2SuperLobbyDeluxe &8- &f" +
            " \n" +
            "&7Usage: &2/ghcreate [type] &a(argument)"),
    WRONG_TEMPLATE("&c{type} is not a valid template."),
    WRONG_TYPE("&c{type} is not a valid type.");

    private String messageFormatted;
    private String message;

    Message(String message) { this.message = message; }

    public String getMessage() {
        return StringUtil.color(message);
    }

    public Message format(String string) {
        if (messageFormatted == null) messageFormatted = getMessage();

        messageFormatted = messageFormatted
                .replace("{type}", string)
                .replace("{version}", string);
        return this;
    }

    public Message format(long duration) {
        if (messageFormatted == null) messageFormatted = getMessage();

        messageFormatted = messageFormatted
                .replace("{duration}", String.valueOf(duration));
        return this;
    }

    public void send(CommandSender sender) {
        if (messageFormatted != null) sender.sendMessage(messageFormatted);
        else sender.sendMessage(getMessage());

        messageFormatted = null;
    }
}