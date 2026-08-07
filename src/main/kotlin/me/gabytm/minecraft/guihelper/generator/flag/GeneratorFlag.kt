package me.gabytm.minecraft.guihelper.generator.flag

import org.bukkit.command.CommandSender
import org.incendo.cloud.description.Description
import org.incendo.cloud.paper.LegacyPaperCommandManager
import org.incendo.cloud.parser.ParserDescriptor
import org.incendo.cloud.parser.flag.CommandFlag
import org.incendo.cloud.parser.standard.StringParser

data class GeneratorFlag(
	val names: List<String>,
	val description: String,
	val required: Boolean,
	val argumentName: String? = null,
	val argumentType: ParserDescriptor<in CommandSender, out Any>? = null,
) {

	fun toCommandFlag(commandManager: LegacyPaperCommandManager<CommandSender>): CommandFlag<*> {
		var builder: CommandFlag.Builder<CommandSender, out Any> = commandManager.flagBuilder(names[0])
			.withAliases(names.drop(1))
			.withDescription(Description.description(description))

		if (argumentType != null) {
			builder = builder.withComponent(argumentType)
		}

		return builder.build()
	}

	companion object {

		fun fileNameFlag(): GeneratorFlag = GeneratorFlag(
			listOf("fileName"),
			"The name of the file without the .yml extension, default: GUIHelper-<current time>",
			false,
			"name",
			StringParser.quotedStringParser()
		)

	}

}
