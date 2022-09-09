package me.gabytm.minecraft.guihelper.command.arg.impl

import me.gabytm.minecraft.guihelper.command.arg.Argument
import kotlin.reflect.KClass

class IntegerArgument(
	name: String,
	requireValue: Boolean,
	defaultValue: Int,
	isRequired: Boolean,
	suggestions: List<String>
) : Argument<Int>(
	name,
	requireValue,
	defaultValue,
	isRequired,
	suggestions,
) {

	override fun getType(): KClass<Int> = Int::class

	override fun parseValue(string: String): Int? = string.toIntOrNull()

	override fun isValue(t: Int): Boolean = true

}
