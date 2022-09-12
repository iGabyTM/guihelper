package me.gabytm.minecraft.guihelper.command.arg

import kotlin.reflect.KClass

abstract class Argument<T : Any>(
	val name: String,
	val requireValue: Boolean,
	val valueClass: KClass<T>,
	val defaultValue: T?,
	val isRequired: Boolean,
	val suggestions: List<String>
) {

	abstract fun parseValue(string: String): T?

	abstract fun isValue(t: T): Boolean

}
