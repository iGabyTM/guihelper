package me.gabytm.minecraft.guihelper.command

import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.CompleteFor
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

@Command("gh")
class TestCommand : CommandBase() {

	private val options = listOf(
		Option("page", hasValue = true, isRequired = false),
		Option("heads", hasValue = true, isRequired = true, listOf("PLAYER", "HDB", "BASE64"))
	).sortedBy { it.isRequired }

	@SubCommand("test")
	fun onCommand(sender: Player, args: Array<String>) {
		val optionsCopy = options.toMutableList()
		val arguments = args.drop(1)

		optionsCopy.removeIf { option ->
			arguments.any { arg ->
				if (option.hasValue) {
					arg.startsWith("${option.name}:")
				} else {
					arg == option.name
				}
			}
		}

		if (optionsCopy.isEmpty()) {
			sender.sendMessage("Good")
		} else {
			sender.sendMessage("Bad: ${optionsCopy.joinToString(", ", transform = { it.name })}")
		}
	}

	@CompleteFor("test")
	fun tabCompletion(args: List<String>, sender: Player): List<String> {
		val optionsCopy = options.toMutableList()

		optionsCopy.removeIf { option ->
			args.any { arg ->
				if (option.hasValue) {
					arg.startsWith("${option.name}:") && arg != "${option.name}:"
				} else {
					arg == option.name
				}
			}
		}

		// All options were provided
		if (optionsCopy.isEmpty()) {
			return emptyList()
		}

		val completion = mutableListOf<String>()

		for (option in optionsCopy) {
			if (option.hasValue) {
				if (option.suggestions.isEmpty()) {
					completion.add("${option.name}:")
				} else {
					option.suggestions.forEach { completion.add("${option.name}:$it") }
				}
			} else {
				completion.add(option.name)
			}
		}

		return StringUtil.copyPartialMatches(args.last(), completion, mutableListOf())
	}

	private data class Option(val name: String, val hasValue: Boolean, val isRequired: Boolean, val suggestions: List<String> = emptyList())

}
