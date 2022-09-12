package me.gabytm.minecraft.guihelper.command.arg.impl

import com.google.common.base.Enums
import me.gabytm.minecraft.guihelper.command.arg.Argument
import kotlin.reflect.KClass

class EnumArgument<E: Enum<E>>(
	name: String,
	requireValue: Boolean,
	valueClass: KClass<E>,
	defaultValue: E?,
	isRequired: Boolean,
	suggestions: List<String>
) : Argument<E>(
	name,
	requireValue,
	valueClass,
	defaultValue,
	isRequired,
	suggestions,
) {

	override fun parseValue(string: String): E? = Enums.getIfPresent(valueClass.java, string.uppercase()).orNull()

	override fun isValue(t: E): Boolean = true

}
