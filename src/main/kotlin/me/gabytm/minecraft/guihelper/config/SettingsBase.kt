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

import me.gabytm.minecraft.guihelper.GUIHelper
import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.SettingsManager
import me.mattstudios.config.properties.Property
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

open class SettingsBase(name: String, settingsHolder: Class<out SettingsHolder>) {

    private val config = SettingsManager
        .from(File(JavaPlugin.getProvidingPlugin(GUIHelper::class.java).dataFolder, "settings/$name.yml"))
        .configurationData(settingsHolder)
        .create()

    fun reload() = config.reload()

    operator fun <T> get(property: Property<T>) = config[property]

	companion object {

		fun move(dataFolder: Path) {
			if (!Files.exists(dataFolder)) {
				Files.createDirectory(dataFolder)
			}

			val defaults = dataFolder.resolve("defaults")

			if (Files.exists(defaults)) {
				Files.move(defaults, dataFolder.resolve("settings"))
			}
		}

	}

}
