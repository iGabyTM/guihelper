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

package me.gabytm.minecraft.guihelper.utils

import me.gabytm.minecraft.guihelper.functions.color
import me.gabytm.minecraft.guihelper.generators.base.GeneratorContext
import org.bukkit.command.CommandSender

enum class Message(var value: String) {

    /**
     * %s - the path of the file, [Config.path][me.gabytm.minecraft.guihelper.config.Config.path]
     *
     * %d - how long the process took (in ms)
     */
    GENERATION_DONE("&2&lGH &8| &aDone, the config can be found &2@ &f%s&a! &7(%dms)"),

    EMPTY("&2&lGH &8| &aThe GUI has been cleared and is ready to use!"),
    EMPTY_GUI("&4&lGH &8| &cPlease add some items to the GUI first!"),

    /**
     * %s - user input for generator name
     */
    UNKNOWN_GENERATOR("&4&lGH &8| &cUnknown generator &8'&c&n%s&8'&c.")
    ;

    init {
        value = value.color()
    }

    fun send(receiver: CommandSender, vararg args: Any?) {
        if (args.isEmpty()) {
            receiver.sendMessage(value)
        } else {
            receiver.sendMessage(value.format(*args))
        }
    }

    fun send(context: GeneratorContext, vararg args: Any?) = send(context.player, *args)

}