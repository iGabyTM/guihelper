package me.gabytm.minecraft.guihelper.command.arg

class Arguments(private val arguments: Map<Argument<*>, *>) {

	fun exists(name: String): Boolean = arguments.keys.any { it.name == name }

	fun <T> get(name: String): T? {
		return arguments.entries.firstOrNull { it.key.name == name }?.value as? T
	}

	fun <T> getOrElse(name: String, def: T): T = get<T>(name) ?: def

}
