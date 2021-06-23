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