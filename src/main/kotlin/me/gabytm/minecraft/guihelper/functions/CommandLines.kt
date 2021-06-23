package me.gabytm.minecraft.guihelper.functions

import me.gabytm.minecraft.guihelper.items.heads.providers.HeadIdProvider.Provider
import org.apache.commons.cli.CommandLine

fun <V> CommandLine.getOrDefault(opt: String, default: V, transformer: (String) -> V?): V {
    if (!hasOption(opt)) {
        return default
    }

    val value = getOptionValue(opt) ?: return default
    return transformer(value) ?: default
}

fun <V> CommandLine.getOrDefault(opt: Char, default: V, transformer: (String) -> V?): V {
    return getOrDefault(opt.toString(), default, transformer)
}

fun <V> CommandLine.getArg(opt: Char, transformer: (String) -> V?): V? {
    if (!hasOption(opt)) {
        return null
    }

    val value = getOptionValue(opt) ?: return null
    return transformer(value)
}

fun <V> CommandLine.getCollectionOrDefault(
    opt: String,
    default: Collection<V>,
    transformer: (Array<String>) -> Collection<V>?
): Collection<V> {
    if (!hasOption(opt)) {
        return default
    }

    val values = getOptionValues(opt) ?: return default
    return transformer(values) ?: default
}

fun CommandLine.getHeadIdProvider(option: String = "heads", default: Provider = Provider.BASE_64): Provider {
    return getOrDefault(option, default) { Provider.getProvider(it, default) }
}