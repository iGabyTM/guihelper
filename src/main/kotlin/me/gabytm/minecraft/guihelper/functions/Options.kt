package me.gabytm.minecraft.guihelper.functions

import me.gabytm.minecraft.guihelper.items.heads.HeadIdProvider.Provider
import org.apache.commons.cli.Option

fun createHeadsOption(vararg providers: Provider): Option {
    return Option.builder("h")
        .longOpt("heads")
        .argName(providers.joinToString(", ") { it.alias })
        .desc("The method used to process player heads")
        .build()
}