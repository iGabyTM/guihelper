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

package me.gabytm.guihelper.template;

import com.google.common.primitives.Ints;
import me.gabytm.guihelper.data.Config;
import me.gabytm.guihelper.utils.ItemUtil;
import me.gabytm.guihelper.utils.Message;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TemplateManager {
    private final Map<String, List<ConfigurationSection>> templates = new HashMap<>();

    public void loadTemplates(ConfigurationSection section) {
        if (section == null) {
            return;
        }

        templates.clear();

        for (String template : section.getKeys(false)) {
            if (section.getConfigurationSection(template).getKeys(false).size() == 0) {
                continue;
            }

            final List<ConfigurationSection> sections = new ArrayList<>();

            for (String key : section.getConfigurationSection(template).getKeys(false)) {
                final ConfigurationSection templateSection = section.getConfigurationSection(template + "." + key);

                if (templateSection != null) {
                    sections.add(templateSection);
                    continue;
                }
            }

            templates.put(template, sections);
        }
    }

    public List<ConfigurationSection> getTemplate(String key) {
        return templates.getOrDefault(key, null);
    }

    public void generate(final String template, final Config config, final Inventory inventory, final Player player) {
        final List<ConfigurationSection> sections = templates.get(template);
        final long start = System.currentTimeMillis();

        for (int slot = 0; slot < inventory.getContents().length; slot++) {
            final ItemStack item = inventory.getItem(slot);

            if (ItemUtil.isNull(item)) {
                continue;
            }

            for (ConfigurationSection section : sections) {
                final String sectionName = replaceVariables(section.getName(), slot, item);
                handleConfigurationSection(config, section, sectionName, slot, item);
            }
        }

        config.save();
        Message.CREATION_DONE.format(System.currentTimeMillis() - start).send(player);
    }

    public void handleConfigurationSection(final Config config, final ConfigurationSection template, final String path, final int slot, final ItemStack item) {
        for (String defaultKey : template.getKeys(false)) {
            final String newKey = replaceVariables(defaultKey, slot, item);

            if (template.isConfigurationSection(defaultKey)) {
                handleConfigurationSection(config, template.getConfigurationSection(defaultKey), path + "." + newKey, slot, item);
                continue;
            }

            if (template.isList(defaultKey)) {
                config.get().set(path + "." + newKey, handleStringsList(template.getStringList(defaultKey), slot, item));
                continue;
            }

            final String value = replaceVariables(template.getString(defaultKey), slot, item);
            final Integer integerValue = Ints.tryParse(value);

            if (integerValue == null) {
                config.get().set(path + "." + newKey, value);
                continue;
            }

            config.get().set(path + "." + newKey, integerValue);
        }
    }

    private List<String> handleStringsList(List<String> list, int slot, ItemStack item) {
        return list
                .stream()
                .map(line -> replaceVariables(line, slot, item))
                .collect(Collectors.toList());
    }

    private String replaceVariables(String line, int slot, ItemStack item) {
        line = line
                .replace("{amount}", String.valueOf(item.getAmount()))
                .replace("{data}", String.valueOf(item.getDurability()))
                .replace("{enchanted}", String.valueOf(item.getItemMeta().hasEnchants()))
                .replace("{material}", item.getType().toString())
                .replace("{slot}", String.valueOf(slot));


        for (ItemFlag flag : ItemFlag.values()) {
            line = line.replace("{flag_" + flag.name().toLowerCase() + "}", String.valueOf(item.getItemMeta().hasItemFlag(flag)));
        }

        if (item.getItemMeta().hasDisplayName()) {
            final String name = ItemUtil.getDisplayName(item.getItemMeta());

            line = line
                    .replace("{name}", name)
                    .replace("{essentials_name}", name.replace(" ", "_"));
        } else {
            line = line.replace("{name}", "");
        }

        if (item.getItemMeta().hasLore()) {
            final List<String> lore = ItemUtil.getLore(item.getItemMeta());

            line = line.replace("{essentials_lore}", String.join("|", lore).replace(" ", "_"));
        } else {
            line = line.replace("{essentials_lore}", "");
        }

        return line;
    }

    public List<String> getTemplates() {
        return new ArrayList<>(templates.keySet());
    }
}
