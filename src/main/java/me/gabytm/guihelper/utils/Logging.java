/*
 * Copyright 2020 GabyTM
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

package me.gabytm.guihelper.utils;

import me.gabytm.guihelper.GUIHelper;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Logging {
    private static final Logger LOGGER = JavaPlugin.getProvidingPlugin(GUIHelper.class).getLogger();

    public static void log(final Level level, final String message) {
        LOGGER.log(level, StringUtil.color(message));
    }

    public static void info(final String message) {
        log(Level.INFO, message);
    }

    public static void warning(final String message) {
        log(Level.WARNING, message);
    }

    public static void error(final String message, final Exception error) {
        LOGGER.log(Level.SEVERE, message, error);
    }

    public static void error(final Message message, final Exception error) {
        LOGGER.log(Level.SEVERE, message.getMessage(), error);
    }

    public enum Message {
        SAVE_ERROR("An error occurred while saving the config.");

        private final String message;

        Message(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
