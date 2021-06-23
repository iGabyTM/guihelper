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

@file:JvmName("Loggers")

package me.gabytm.minecraft.guihelper.functions

import me.gabytm.minecraft.guihelper.GUIHelper
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

private val plugin = JavaPlugin.getPlugin(GUIHelper::class.java)

/**
 * Log a [Throwable] through plugin's [java.util.logging.Logger] with a custom message
 * @see Level.SEVERE
 * @sample me.gabytm.minecraft.guihelper.config.Config.save
 */
fun error(message: String, thrown: Throwable) = plugin.logger.log(Level.SEVERE, message, thrown)

/**
 * Log an info message
 * @see Level.INFO
 */
fun info(message: String) = plugin.logger.info(message)

/**
 * Log a warning message
 * @see Level.WARNING
 */
fun warning(message: String) = plugin.logger.warning(message)