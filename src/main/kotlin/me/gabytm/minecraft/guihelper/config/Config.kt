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

package me.gabytm.minecraft.guihelper.config

import me.gabytm.minecraft.guihelper.functions.error
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.IOException

@Suppress("MemberVisibilityCanBePrivate")
class Config(filePath: String, plugin: Plugin, empty: Boolean = false) {

    private var file: File = File(plugin.dataFolder.parentFile, filePath)
    val path: String = file.path
    private var yaml: YamlConfiguration

    init {
        file.parentFile.mkdirs()

        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                error("Could not create $path", e)
            }
        }

        this.yaml = YamlConfiguration.loadConfiguration(file)

        if (empty) {
            empty()
        }
    }

    /**
     * Clear the content of the file by setting all keys to null
     */
    fun empty() = yaml.getKeys(false).forEach { yaml[it] = null }

    /**
     * Shortcut for creating a [ConfigurationSection]
     * @see ConfigurationSection.createSection
     */
    fun createSection(path: String): ConfigurationSection = yaml.createSection(path)

    operator fun set(path: String, value: Any?) = yaml.set(path, value)

    /**
     * Save the [yaml] and redirect any possible [IOException] to [Plugin.getLogger]
     * @see [me.gabytm.minecraft.guihelper.functions.error]
     */
    fun save() {
        try {
            yaml.save(file)
        } catch (e: IOException) {
            error("Could not save $path", e)
        }
    }

}
