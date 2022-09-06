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

package me.gabytm.minecraft.guihelper.item

import me.gabytm.minecraft.guihelper.item.custom.CustomItemHandler
import me.gabytm.minecraft.guihelper.item.heads.HeadsIdHandler
import me.gabytm.minecraft.guihelper.item.heads.providers.HeadIdProvider
import me.gabytm.minecraft.guihelper.item.serialization.ItemSerializationHandler
import me.gabytm.minecraft.guihelper.item.serialization.serializers.Serializer
import org.bukkit.inventory.ItemStack
import kotlin.reflect.KClass

class ItemsManager {

    private val customItemHandler = CustomItemHandler()
    private val headsIdHandler = HeadsIdHandler()
    private val itemSerializerHandler = ItemSerializationHandler(this)

    fun <T: Any> getCustomItem(type: KClass<T>, item: ItemStack): T? = customItemHandler.get(type, item)

    fun getHeadId(item: ItemStack, provider: HeadIdProvider.Provider = HeadIdProvider.Provider.BASE_64): String {
        return headsIdHandler[item, provider]
    }

    fun serialize(item: ItemStack, serializer: Serializer = Serializer.VANILLA): String {
        return itemSerializerHandler.serialize(item, serializer)
    }

}
