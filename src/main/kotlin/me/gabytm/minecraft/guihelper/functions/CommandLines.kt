/*
 * Copyright 2021 GabyTM
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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