package me.gabytm.minecraft.guihelper.config

import me.gabytm.minecraft.guihelper.functions.error
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.IOException
import java.util.logging.Level

@Suppress("MemberVisibilityCanBePrivate")
class Config(path: String, private val plugin: Plugin) {

    private var file: File = if (path.endsWith(".yml")) {
        File(plugin.dataFolder, path)
    } else {
        File(plugin.dataFolder.parentFile, "$path/GUIHelper.yml")
    }

    var yaml: YamlConfiguration
        private set

    init {
        file.parentFile.mkdirs()

        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                plugin.logger.error("Could not create ${file.path}", e)
            }
        }

        this.yaml = YamlConfiguration.loadConfiguration(file)
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

    /**
     * Save the [yaml] and redirect any possible [IOException] to [Plugin.getLogger]
     * @see [me.gabytm.minecraft.guihelper.functions.error]
     */
    fun save() {
        try {
            yaml.save(file)
        } catch (e: IOException) {
            plugin.logger.error("Could not save ${file.path}", e)
        }
    }

}