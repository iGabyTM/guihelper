package me.gabytm.minecraft.guihelper.generators.implementations

import me.gabytm.minecraft.guihelper.GUIHelper
import me.gabytm.minecraft.guihelper.config.Config
import me.gabytm.minecraft.guihelper.config.DefaultValues
import me.gabytm.minecraft.guihelper.functions.*
import me.gabytm.minecraft.guihelper.generators.base.ConfigGenerator
import me.gabytm.minecraft.guihelper.generators.base.GeneratorContext
import me.gabytm.minecraft.guihelper.utils.Message
import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Description
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property.create
import org.apache.commons.cli.CommandLine
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack
import kotlin.system.measureTimeMillis

class CratesPlusGenerator(
    private val plugin: GUIHelper,
    override val pluginName: String = "CratesPlus",
    override val pluginVersion: String = "4.5.3",
    override val rgbFormat: (String) -> String = NO_RGB_SUPPORT
) : ConfigGenerator() {

    private val defaults = Defaults(pluginName)

    override fun getMessage() = "  &2$pluginName &av$pluginVersion &8- &fCrate prizes"

    override fun generate(context: GeneratorContext, input: CommandLine): Boolean {
        val config = Config(pluginName, plugin, true)

        val duration = measureTimeMillis {
            context.forEach { item, slot ->
                createItem(config.createSection("Crates.GUIHelper.Winnings.${slot + 1}"), item, slot)
            }
        }

        config.save()
        Message.GENERATION_DONE.send(context, config.path, duration)
        return true
    }

    override fun onReload() {
        defaults.reload()
    }

    @Suppress("DEPRECATION")
    override fun createItem(section: ConfigurationSection, item: ItemStack, slot: Int) {
        section["Type"] = "ITEM"
        section["Item Type"] = item.type.name
        section.set("Item Data", item.durability) { it > 0 }
        section["Percentage"] = defaults[Value.PERCENTAGE]
        section["Amount"] = item.amount

        val meta = item.meta ?: return

        if (item.isSpawnEgg) {
            section["Item Data"] = item.spawnEggType.typeId
        }

        section.set("Name", meta::hasDisplayName) { item.displayName(rgbFormat) }
        section.set("Lore", meta::hasLore) { item.lore(rgbFormat) }
        section.setList("Flags", meta.itemFlags.map { it.name })
        section.setList("Enchantments", item.enchants { enchantment, level -> "${enchantment.name}-$level" })
    }

    private class Defaults(name: String) : DefaultValues(name, Value::class.java)

    @Description(
        " ",
        "Default values that will be used in the config creation process",
        " ",
        "▪ CratesPlus 4.5.3 by ConnorLinfoot (https://spigotmc.org/resources/5018/)",
        "▪ Wiki: https://github.com/ConnorLinfoot/CratesPlus/wiki",
        " "
    )
    private object Value : SettingsHolder {

        @Path("percentage")
        val PERCENTAGE = create(10.0)

    }

}
