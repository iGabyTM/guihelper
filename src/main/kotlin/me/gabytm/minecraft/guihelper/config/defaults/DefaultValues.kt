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

package me.gabytm.minecraft.guihelper.config.defaults

import me.gabytm.minecraft.guihelper.GUIHelper
import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.SettingsManager
import me.mattstudios.config.properties.Property
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/*
class DefaultValues(name: String, plugin: Plugin, val generator: ConfigGenerator) {

    val file = File(JavaPlugin.getProvidingPlugin(GUIHelper::class.java).dataFolder, "defaults/$name.yml")
    var yaml: YamlConfiguration

    init {
        val exists = file.exists()
        file.parentFile.mkdirs()

        if (!exists) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                plugin.logger.error("Could not create ${file.path}", e)
            }
        }

        this.yaml = YamlConfiguration.loadConfiguration(file)
        yaml.options().copyDefaults(true)
        yaml.addDefaults(generator.defaults())

        if (!exists) {
            try {
                yaml.save(file)
            } catch (e: IOException) {
                plugin.logger.error("Could not save ${file.path}", e)
            }
        }
    }

    fun reload() {
        this.yaml = YamlConfiguration.loadConfiguration(file)
    }

}*/

open class DefaultValues(name: String, settingsHolder: Class<out SettingsHolder>) {

    private val config = SettingsManager
        .from(File(JavaPlugin.getProvidingPlugin(GUIHelper::class.java).dataFolder, "defaults/$name.yml"))
        .configurationData(settingsHolder)
        .create()

    fun reload() = config.reload()

    operator fun <T> get(property: Property<T>) = config[property]

}