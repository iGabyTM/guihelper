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

package me.gabytm.minecraft.guihelper.generators

import me.gabytm.minecraft.guihelper.GUIHelper
import me.gabytm.minecraft.guihelper.functions.color
import me.gabytm.minecraft.guihelper.generators.base.ConfigGenerator
import me.gabytm.minecraft.guihelper.generators.implementations.*
import me.gabytm.minecraft.guihelper.utils.Constants.ALIAS

/**
 * Main point of interacting with generators
 *
 * @since 1.1.0
 */
class GeneratorsManager(plugin: GUIHelper) {

    private val registeredGenerators = mutableMapOf<String, ConfigGenerator>()
    private var registeredGeneratorsIds = listOf<String>()

    init {
        @Suppress("SpellCheckingInspection")
        sequenceOf(
            ASkyBlockGenerator(plugin),
            CrateReloadedGenerator(plugin),
            CratesPlusGenerator(plugin),
            DeluxeMenusGenerator(plugin),
            ShopGuiPlusGenerator(plugin)
        ).forEach { register(it) }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun register(generator: ConfigGenerator, replaceIfPresent: Boolean = false) {
        if (replaceIfPresent) {
            registeredGenerators[generator.pluginName.lowercase()] = generator
        } else {
            registeredGenerators.putIfAbsent(generator.pluginName.lowercase(), generator)
        }

        registeredGeneratorsIds = registeredGenerators.keys.toList()
    }

    fun reload() = registeredGenerators.values.forEach { it.onReload() }

    /**
     * Get a [ConfigGenerator] by id
     *
     * @param id the id of the generator
     * @return the generator if found, otherwise null
     */
    fun getGenerator(id: String): ConfigGenerator? = registeredGenerators[id.lowercase()]

    fun registeredGeneratorsIds(): List<String> = registeredGeneratorsIds

    fun getMessage(): String {
        return """
            ${registeredGenerators.values.joinToString("\n") { it.getMessage() }}
             
            &7Usage: &2/$ALIAS create [type] &a(arguments)
        """.trimIndent().color()
    }

}