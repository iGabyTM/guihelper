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

package me.gabytm.guihelper.types;

import me.gabytm.guihelper.GUIHelper;

public class GuiManager {
    private ASkyBlock aSkyBlockType;
    private BossShopPro bossShopProType;
    private ChestCommands chestCommandsType;
    private CratesPlus cratesPlusType;
    private CrazyCrates crazyCratesType;
    private CrazyEnvoy crazyEnvoyType;
    private DeluxeMenus deluxeMenusType;
    //private GuiShop guiShopType;
    private LemonMobCoins lemonMobCoinsType;
    private ShopGuiPlus shopGuiPlusType;

    public ASkyBlock aSkyBlock() { return aSkyBlockType; }
    public BossShopPro bossShopPro() { return bossShopProType; }
    public ChestCommands chestCommands() { return chestCommandsType; }
    public CratesPlus cratesPlus() { return cratesPlusType; }
    public CrazyEnvoy crazyEnvoy() { return crazyEnvoyType; }
    public CrazyCrates crazyCrates() { return crazyCratesType; }
    public DeluxeMenus deluxeMenus() { return deluxeMenusType; }
    //public GuiShop guiShop() { return guiShopType; }
    public LemonMobCoins lemonMobCoins() { return lemonMobCoinsType; }
    public ShopGuiPlus shopGuiPlus() { return shopGuiPlusType; }

    public GuiManager(GUIHelper plugin) {
        aSkyBlockType = new ASkyBlock(plugin);
        bossShopProType = new BossShopPro(plugin);
        chestCommandsType = new ChestCommands(plugin);
        cratesPlusType = new CratesPlus(plugin);
        crazyCratesType = new CrazyCrates(plugin);
        crazyEnvoyType = new CrazyEnvoy(plugin);
        deluxeMenusType = new DeluxeMenus(plugin);
        //guiShopType = new GuiShop(plugin);
        lemonMobCoinsType = new LemonMobCoins(plugin);
        shopGuiPlusType = new ShopGuiPlus(plugin);
    }
}
