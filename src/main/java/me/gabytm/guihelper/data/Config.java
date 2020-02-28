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

package me.gabytm.guihelper.data;

import me.gabytm.guihelper.GUIHelper;
import me.gabytm.guihelper.utils.Logging;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    private File configFile;
    private FileConfiguration config;

    public Config(final String path, final GUIHelper plugin) {
        if (path.endsWith(".yml")) {
            this.configFile = new File(plugin.getDataFolder(), path);
        } else {
            this.configFile = new File(plugin.getDataFolder().getParent(),path + "/GUIHelper.yml");
        }

        configFile.getParentFile().mkdirs();

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                Logging.error(String.format("An error occurred while creating %s", configFile.getPath()), e);
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void empty() {
        config.getKeys(false).forEach(key -> config.set(key, null));
    }

    public void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            Logging.error(String.format("An error occurred while saving %s", configFile.getPath()), e);
        }
    }

    public FileConfiguration get() {
        return config;
    }
}
