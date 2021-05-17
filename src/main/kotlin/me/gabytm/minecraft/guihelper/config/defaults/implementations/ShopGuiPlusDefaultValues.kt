package me.gabytm.minecraft.guihelper.config.defaults.implementations

import me.gabytm.minecraft.guihelper.config.defaults.DefaultValues
import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Comment
import me.mattstudios.config.annotations.Description
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property.create

class ShopGuiPlusDefaultValues : DefaultValues("ShopGUIPlus", DefaultValue::class.java) {

    @Description(
        " ",
        "Default values that will be used in the config creation process",
        " ",
        "▪ ShopGUIPlus 1.59.2 by brc (https://spigotmc.org/resources/6515/)",
        "▪ Wiki: https://docs.brcdev.net/#/shopgui/faq",
        " "
    )
    object DefaultValue : SettingsHolder {

        @Path("buyPrice")
        val BUY_PRICE = create(10.0)

        @Path("sellPrice")
        val SELL_PRICE = create(10.0)

        @Comment("https://docs.brcdev.net/#/shopgui/stack-size?id=unstack")
        @Path("unstack")
        val UNSTACK = create(false)

        @Comment("https://docs.brcdev.net/#/shopgui/stack-size?id=stacked")
        @Path("stacked")
        val STACKED = create(true)

    }

}