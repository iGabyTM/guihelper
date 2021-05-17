package me.gabytm.minecraft.guihelper.config.defaults.implementations

import me.gabytm.minecraft.guihelper.config.defaults.DefaultValues
import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Description
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property

class ASkyBlockDefaultValues : DefaultValues("ASkyBlock", DefaultValue::class.java) {

    @Description(
        " ",
        "Default values that will be used in the config creation process",
        " ",
        "▪ ASkyBlock 3.0.9.4 by Tastybento (https://spigotmc.org/resources/1220/)",
        "▪ Wiki: https://github.com/tastybento/askyblock/wiki",
        " "
    )
    object DefaultValue : SettingsHolder {

        @Path("price")
        val PRICE = Property.create(100)

        @Path("sellprice")
        val SELL_PRICE = Property.create(10)

    }

}