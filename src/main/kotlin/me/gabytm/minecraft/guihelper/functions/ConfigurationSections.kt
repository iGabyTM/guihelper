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

@file:JvmName("ConfigurationSections")
package me.gabytm.minecraft.guihelper.functions

import org.bukkit.configuration.ConfigurationSection

/**
 * Usage example:
 * ```
 * section.set("item.lore", { itemMeta.hasLore }) { itemMeta.lore }
 * section.set("item.lore", itemMeta::hasLore, itemMeta::lore)
 * ```
 *
 * In this example if the item has lore, will be set in config
 *
 * @see [org.bukkit.inventory.meta.ItemMeta.hasLore]
 * @see [org.bukkit.inventory.meta.ItemMeta.getLore]
 */
fun ConfigurationSection.set(path: String, condition: () -> Boolean, value: () -> Any?) {
    if (condition()) {
        this[path] = value()
        println("this[$path] = ${value()}")
    }
}

/**
 * Usage example:
 * ```
 * section.set("path", item.amount) { it > 1 }
 * ```
 *
 * @see [org.bukkit.inventory.ItemStack.getAmount]
 */
fun <V> ConfigurationSection.set(path: String, value: V, condition: (V) -> Boolean) {
    if (condition(value)) {
        this[path] = value
    }
}

/**
 * Usage example:
 * ```
 * section.set("nbt_int", item.customModelData, { it > 0}) { "CustomModelData:$it" }
 * ```
 *
 * In this example the value is set only if it is greater than 0 and will be formatted as "CustomModelData:{value}",
 * eg: `nbt_int: CustomModelData:5` for an item with customModelData equal with 5
 *
 * @see [me.gabytm.minecraft.guihelper.functions.customModelData]
 */
fun <V> ConfigurationSection.set(path: String, value: V, condition: (V) -> Boolean, transformer: (V) -> Any) {
    if (condition(value)) {
        this[path] = transformer(value)
    }
}

/**
 * Usage example:
 * ```
 * section.setList("lore", true, item.lore)
 * ```
 *
 * In this example if the item doesn't have lore, an empty list will still be set in config
 *
 * @see [me.gabytm.minecraft.guihelper.functions.lore]
 */
fun ConfigurationSection.setList(path: String, setIfEmpty: Boolean, list: List<Any>) {
    if (!setIfEmpty && list.isEmpty()) {
        return
    }

    this[path] = list
}

/**
 * Set a list in config only if [is not empty][List.isNotEmpty]
 *
 * Usage example:
 * ```
 * section.setList("lore", item.lore)
 * // Also the equivalent of this
 * // section.setList("lore", false, item.lore)
 * ```
 *
 * @param path where the list will be set
 * @param list list
 * @see [me.gabytm.minecraft.guihelper.functions.lore]
 */

fun ConfigurationSection.setList(path: String, list: List<Any>) {
    if (list.isNotEmpty()) {
        this[path] = list
    }
}

fun ConfigurationSection.setList(path: String, setIfEmpty: Boolean, list: () -> List<Any>) {
    with(list()) {
        if (!setIfEmpty && isEmpty()) {
            return@with
        }

        set(path, this)
    }
}

/**
 * Set a list in config only if [is not empty][List.isNotEmpty]
 *
 * The equivalent of **setList(path: String, false, list: () -> List<Any>)**
 *
 * @param path where the value will be set
 * @param list list
 */
fun ConfigurationSection.setList(path: String, list: () -> List<Any>) {
    with(list()) {
        if (isNotEmpty()) {
            set(path, list)
        }
    }
}