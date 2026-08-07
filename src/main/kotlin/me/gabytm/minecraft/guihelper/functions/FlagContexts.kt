package me.gabytm.minecraft.guihelper.functions

import org.incendo.cloud.parser.flag.FlagContext

fun <V> FlagContext.getOrDefault(flagName: String, default: V): V = getValue<V>(flagName).orElse(default)
