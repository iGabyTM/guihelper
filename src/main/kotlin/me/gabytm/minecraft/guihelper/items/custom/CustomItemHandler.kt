package me.gabytm.minecraft.guihelper.items.custom

import me.gabytm.minecraft.guihelper.items.custom.providers.CustomItemProvider
import me.gabytm.minecraft.guihelper.items.custom.providers.headdatabase.HeadDatabaseCustomItemProvider
import me.gabytm.minecraft.guihelper.items.custom.providers.headdatabase.HeadDatabaseItem
import me.gabytm.minecraft.guihelper.items.custom.providers.itemsadder.ItemsAdderCustomItemProvider
import me.gabytm.minecraft.guihelper.items.custom.providers.itemsadder.ItemsAdderItem
import me.gabytm.minecraft.guihelper.items.custom.providers.oraxen.OraxenCustomItemProvider
import me.gabytm.minecraft.guihelper.items.custom.providers.oraxen.OraxenItem
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import kotlin.reflect.KClass

class CustomItemHandler {

    private val providers = mutableMapOf<Class<*>, CustomItemProvider<*>>()

    init {
        registerProviders()
    }

    private fun registerProviders() {
        register("HeadDatabase") {
            providers[HeadDatabaseItem::class.java] = HeadDatabaseCustomItemProvider()
        }

        register("ItemsAdder") {
            providers[ItemsAdderItem::class.java] = ItemsAdderCustomItemProvider()
        }

        register("Oraxen") {
            providers[OraxenItem::class.java] = OraxenCustomItemProvider()
        }
    }

    private fun register(name: String, action: () -> Unit) {
        if (Bukkit.getPluginManager().isPluginEnabled(name)) {
            action()
        }
    }

    internal fun <T : Any> get(type: KClass<T>, item: ItemStack): T? {
        val provider = providers[type.java] ?: return null
        return type.java.cast(provider.get(item))
    }

}