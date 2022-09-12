package me.gabytm.minecraft.guihelper.command.arg

class ArgumentParser {

	fun parse(string: String, args: List<Argument<*>>): Arguments {
		val sortedArgs = args.sortedBy { it.isRequired }.toMutableList()
		val parts = string.split(" ")
		val map: MutableMap<Argument<*>, Any> = mutableMapOf()

		sortedArgs.removeIf { arg ->
			parts.any { string ->
				if (!string.startsWith(arg.name)) {
					return@any false
				}

				if (arg.requireValue) {
					val split = string.split(":", limit = 2)

					if (split.size != 2) {
						if (arg.defaultValue != null) {
							map[arg] = arg.defaultValue
							return@any true
						}

						throw IllegalArgumentException("Expected value for argument ${arg.name} (${arg::class.simpleName})")
					}

					val value = arg.parseValue(split[1]) ?: arg.defaultValue ?: throw IllegalArgumentException("Value '${split[1]}' of argument '${arg.name}' is not a valid '${arg.valueClass.qualifiedName}'")
					map[arg] = value
					return@any true
				}

				return@any string == arg.name
			}
		}

		sortedArgs
			.filter { it.defaultValue != null }
			.forEach { map[it] = it.defaultValue as Any }

		if (sortedArgs.any { it.isRequired }) {
			throw IllegalArgumentException("Missing required argument(s): ${sortedArgs.joinToString(", ", transform = { "${it.name} (${it::class.simpleName})" })}")
		}

		return Arguments(map)
	}

}
