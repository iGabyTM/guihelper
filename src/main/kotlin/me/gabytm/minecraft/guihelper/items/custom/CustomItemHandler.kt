/*
 * Copyright 2021 GabyTM
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