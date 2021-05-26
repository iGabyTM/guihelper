package me.gabytm.minecraft.guihelper.functions

import me.gabytm.minecraft.guihelper.items.heads.providers.HeadIdProvider.Provider
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import kotlin.reflect.KClass

fun createHeadsOption(vararg providers: Provider): Option {
    return Option.builder("h")
        .longOpt("heads")
        .arg(String::class, 1, providers.joinToString(", ") { it.alias })
        .desc("The method used to process player heads")
        .build()
}

fun createPageOption(description: String): Option {
    return Option.builder("p")
        .longOpt("page")
        .arg(Int::class, 1, "page")
        .desc(description)
        .build()
}

fun Options.addOption(name: String, function: Option.Builder.() -> Unit) {
    addOption(Option.builder(name).apply(function).build())
}

fun Options.addOption(name: Char, function: Option.Builder.() -> Unit) {
    addOption(name.toString(), function)
}

fun Option.Builder.arg(type: Class<*>, number: Int, name: String, optional: Boolean = false): Option.Builder {
    hasArg()
    numberOfArgs(number)
    argName(name)
    optionalArg(optional)
    type(type)
    return this
}

fun Option.Builder.arg(type: KClass<*>, number: Int, name: String, optional: Boolean = false): Option.Builder {
    return arg(type.java, number, name, optional)
}