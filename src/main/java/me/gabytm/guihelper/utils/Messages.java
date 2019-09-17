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

package me.gabytm.guihelper.utils;

import org.jetbrains.annotations.Nullable;

public enum Messages {
    CREATION_DONE("&aDone! &7({duration}ms)"),
    CREATION_ERROR("&cSomething went wrong, please check the console."),
    EMPTY_GUI("&cPlease add some items to the GUI first!"),
    HELP("\n" +
            "&7GUIHelper v{version}\n" +
            "  &2/ghcreate\n" +
            "  &2/ghcreate [type] &a(argument)\n" +
            "  &2/ghhelp\n  &2/ghlist"),
    PLAYERS_ONLY("&cOnly players can run this command!"),
    TYPES_LIST("\n" +
            "&7GUIHelper v{version}\n" +
            "  &2ASkyBlock &8- &fIsland minishop items\n" +
            "  &2CrazyCrates &a(page) &8- &fCrate prizes\n" +
            "  &2CrazyEnvoy &a(page) &8- &fEnvoy items\n" +
            "  &2DeluxeMenus &8- &fExternal menu\n" +
            "  &2DeluxeMenusLocal &8- &fLocal menu &7(config.yml)\n" +
            "  &2LemonMobCoins &8- &fShop items" +
            "  &2ShopGuiPlus &a(page) &8- &fShop items\n" +
            "\n" +
            "&7Usage: &2&o/ghcreate [type] &a&o(argument)"),
    WRONG_TYPE("&c{type} is not a valid type.");

    private String message;

    Messages(String message) { this.message = message; }

    public String format(@Nullable String type, @Nullable Long duration, @Nullable String version) {
        String msg = StringUtils.colorize(message);

        if (type != null) msg = msg.replaceAll("\\{type}", type);
        if (duration != null) msg = msg.replaceAll("\\{duration}", duration.toString());
        if (version != null) msg = msg.replaceAll("\\{version}", version);

        return msg;
    }
}
