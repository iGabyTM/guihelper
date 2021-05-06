package me.gabytm.minecraft.guihelper.generators

import me.gabytm.minecraft.guihelper.GUIHelper
import me.gabytm.minecraft.guihelper.functions.color
import me.gabytm.minecraft.guihelper.generators.base.ConfigGenerator
import me.gabytm.minecraft.guihelper.generators.implementations.DeluxeMenusGenerator
import me.gabytm.minecraft.guihelper.utils.Constants.COMMAND

/**
 * Main point of interacting with generators
 *
 * @since 1.1.0
 */
class GeneratorsManager(plugin: GUIHelper) {

    private val registeredGenerators: MutableMap<String, ConfigGenerator> = mutableMapOf()

    init {
        sequenceOf(
            "deluxemenus" to DeluxeMenusGenerator(plugin)
        ).forEach { register(it.first, it.second) }
    }

    fun register(id: String, generator: ConfigGenerator, replaceIfPresent: Boolean = false) {
        if (replaceIfPresent) {
            registeredGenerators[id] = generator
        } else {
            registeredGenerators.putIfAbsent(id, generator)
        }
    }

    /**
     * Get a [ConfigGenerator] by id
     *
     * @param id the id of the generator
     * @return the generator if found, otherwise null
     */
    fun getGenerator(id: String): ConfigGenerator? = registeredGenerators[id.toLowerCase()]

    fun getRegisteredGeneratorsIds(): List<String> = registeredGenerators.keys.toList()

    fun getMessage(): String {
        return """
            ${registeredGenerators.values.joinToString("\n") { it.getMessage() }}
             
            &7Usage: &2/$COMMAND create [type] &a(arguments)
        """.trimIndent().color()
    }

}