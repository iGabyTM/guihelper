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
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
                }
            }

            templates.put(template, sections);
        }
    }

    public List<ConfigurationSection> getTemplate(String key) {
        return templates.getOrDefault(key, null);
    }

    public void generate(final String template, final Config config, final Inventory inventory, final Player player, final String[] args) {
        final List<ConfigurationSection> sections = templates.get(template);
        final long start = System.currentTimeMillis();

        for (int slot = 0; slot < inventory.getContents().length; slot++) {
            final ItemStack item = inventory.getItem(slot);

            if (ItemUtil.isNull(item)) {
                continue;
            }

            final Map<String, String> placeholders = loadPlaceholders(slot, item, args);

            for (ConfigurationSection section : sections) {
                final String sectionName = replacePlaceholders(section.getName(), placeholders);
                handleConfigurationSection(config, section, sectionName, placeholders);
            }
        }

        config.save();
        Message.CREATION_DONE.format(System.currentTimeMillis() - start).send(player);
    }

    public void handleConfigurationSection(final Config config, final ConfigurationSection template, final String path, final Map<String, String> placeholders) {
        for (String defaultKey : template.getKeys(false)) {
            final String newKey = replacePlaceholders(defaultKey, placeholders);

            if (template.isConfigurationSection(defaultKey)) {
                handleConfigurationSection(config, template.getConfigurationSection(defaultKey), path + "." + newKey, placeholders);
                continue;
            }

            if (template.isList(defaultKey)) {
                config.get().set(path + "." + newKey, replacePlaceholdersFromList(template.getStringList(defaultKey), placeholders));
                continue;
            }

            final String value = replacePlaceholders(template.getString(defaultKey), placeholders);
            final Integer integerValue = Ints.tryParse(value);

            if (integerValue == null) {
                config.get().set(path + "." + newKey, value);
                continue;
            }

            config.get().set(path + "." + newKey, integerValue);
        }
    }

    private List<String> replacePlaceholdersFromList(final List<String> list, final Map<String, String> placeholders) {
        return list
                .stream()
                .map(line -> replacePlaceholders(line, placeholders))
                .collect(Collectors.toList());
    }

    private Map<String, String> loadPlaceholders(final int slot, final ItemStack item, final String[] args) {
        final ItemMeta meta = item.getItemMeta();
        final Map<String, String> map = new HashMap<>();

        map.put("{amount}", Integer.toString(item.getAmount()));
        map.put("{data}", Short.toString(item.getDurability()));
        map.put("{enchanted}", Boolean.toString(meta.hasEnchants()));
        map.put("{material}", item.getType().name());
        map.put("{slot}", Integer.toString(slot));

        for (ItemFlag flag : ItemFlag.values()) {
            map.put("{flag" + flag.name().toLowerCase() + "}", Boolean.toString(meta.hasItemFlag(flag)));
        }

        if (args.length > 0) {
            map.put("{args}", String.join(" ", args));

            for (int index = 0; index < args.length; index++) {
                map.put("{args[" + (index + 1) + "]}", args[index]);
            }
        }

        if (meta.hasDisplayName()) {
            final String name = ItemUtil.getDisplayName(meta);

            map.put("{name}", name);
            map.put("{essentials_name}", name.replace(" ", "_"));
        } else {
            map.put("{name}", "");
            map.put("{essentials_name}", "");
        }

        if (meta.hasLore()) {
            map.put("{essentials_lore}", String.join("|", ItemUtil.getLore(meta)).replace(" ", "_"));
        } else {
            map.put("{essentials_lore}", "");
        }

        return map;
    }

    private String replacePlaceholders(final String line, final Map<String, String> placeholders) {
        return StringUtils.replaceEach(line, placeholders.keySet().toArray(new String[]{}), placeholders.values().toArray(new String[]{}));
    }

    public List<String> getTemplates() {
        return new ArrayList<>(templates.keySet());
    }
}
