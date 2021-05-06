package me.gabytm.minecraft.guihelper.functions

import java.util.logging.Level
import java.util.logging.Logger

/**
 * Log a [Throwable] with plugin's [Logger] with a custom message
 * @sample me.gabytm.minecraft.guihelper.config.Config.save
 */
fun Logger.error(message: String, thrown: Throwable) = log(Level.SEVERE, message, thrown)