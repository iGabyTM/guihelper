package me.gabytm.minecraft.guihelper.generator.implementations

import de.tr7zw.changeme.nbtapi.NBT
import me.gabytm.minecraft.guihelper.GUIHelper
import me.gabytm.minecraft.guihelper.config.Config
import me.gabytm.minecraft.guihelper.functions.*
import me.gabytm.minecraft.guihelper.generator.base.ConfigGenerator
import me.gabytm.minecraft.guihelper.generator.base.GeneratorContext
import me.gabytm.minecraft.guihelper.item.heads.providers.HeadIdProvider
import me.gabytm.minecraft.guihelper.item.serialization.serializers.Serializer
import me.gabytm.minecraft.guihelper.util.Message
import org.apache.commons.cli.CommandLine
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import kotlin.system.measureTimeMillis

class AdvancedCratesGenerator(
	private val plugin: GUIHelper,
	override val pluginName: String = "AdvancedCrates",
	override val pluginVersion: String = "4.8.9",
	override val configPath: String = "GUIHelper/generated-guis/$pluginName",
	override val rgbFormat: (String) -> String = SPIGOT_RGB_FORMAT,
) : ConfigGenerator() {

	override fun getMessage(): String = "  &2$pluginName &av$pluginVersion &8- &fCrate reawards"

	override fun generate(context: GeneratorContext, input: CommandLine): Boolean {
		val config = Config("$configPath/${getConfigFileName(input)}.yml", plugin, true)

		val duration = measureTimeMillis {
			context.forEach { item, slot -> createItem(config.createSection("Prizes.item${slot + 1}"), item, slot) }
		}

		config.save()
		Message.GENERATION_DONE.send(context, config.path, duration)
		return true
	}

	override fun createItem(section: ConfigurationSection, item: ItemStack, slot: Int) {
		section["Material"] = item.type.name
		section["Amount"] = item.amount

		val serialized = plugin.itemsManager.serialize(item, Serializer.ESSENTIALSX)
		section.setList("Commands", listOf("give {player_name} $serialized"))

		val meta = item.meta ?: return

		section.set("Name", meta::hasDisplayName) { item.displayName(rgbFormat) }
		section.set("Lores", meta::hasLore) { item.lore(rgbFormat) }
		section.setList("Enchantments", item.enchants { enchant, level -> "${enchant.name};${level}" })
		section.set("CustomModelData", item.customModelData) { it > 0 }
		setMetaSpecificValues(section, item, meta)
		setNbtTags(section, item)
	}

	private fun setMetaSpecificValues(section: ConfigurationSection, item: ItemStack, meta: ItemMeta) {
		when {
			item.isPlayerHead -> {
				plugin.itemsManager.getHeadId(item, HeadIdProvider.Provider.BASE_64)?.let { id ->
					section["Base64Texture"] = id
				}
			}
		}
	}

	private fun setNbtTags(section: ConfigurationSection, item: ItemStack) {
		NBT.getComponents(item) {
			val components = it.toString()

			if (components.isNotEmpty() && components != "{}") {
				section["NBTtags"] = components
			}
		}
	}

}
