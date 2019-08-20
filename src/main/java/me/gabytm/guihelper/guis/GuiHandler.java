package me.gabytm.guihelper.guis;

import me.gabytm.guihelper.GUIHelper;

public class GuiHandler {
    private DeluxeMenus deluxeMenusType;

    public GuiHandler(GUIHelper plugin) {
        deluxeMenusType = new DeluxeMenus(plugin);
    }

    public DeluxeMenus deluxeMenus() {
        return deluxeMenusType;
    }
}
