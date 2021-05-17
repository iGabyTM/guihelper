package me.gabytm.minecraft.guihelper.functions

import me.gabytm.minecraft.guihelper.items.heads.HeadIdProvider.Type
import org.apache.commons.cli.Option

fun createHeadsOption(vararg types: Type): Option {
    return Option.builder("h")
        .longOpt("heads")
        .argName(types.joinToString(", ") { it.alias })
        .desc("The method used to process player heads")
        .build()
}