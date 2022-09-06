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

package me.gabytm.minecraft.guihelper.generator.implementations

import me.gabytm.minecraft.guihelper.GUIHelper
import me.gabytm.minecraft.guihelper.config.Config
import me.gabytm.minecraft.guihelper.config.DefaultValues
import me.gabytm.minecraft.guihelper.functions.addOption
import me.gabytm.minecraft.guihelper.functions.arg
import me.gabytm.minecraft.guihelper.functions.getOrDefault
import me.gabytm.minecraft.guihelper.generator.base.ConfigGenerator
import me.gabytm.minecraft.guihelper.generator.base.GeneratorContext
import me.gabytm.minecraft.guihelper.item.serialization.serializers.Serializer
import me.gabytm.minecraft.guihelper.util.Message
import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Comment
import me.mattstudios.config.annotations.Description
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.configurationdata.CommentsConfiguration
import me.mattstudios.config.properties.Property.create
import org.apache.commons.cli.CommandLine
import org.bukkit.inventory.ItemStack
import kotlin.system.measureTimeMillis

class CrateReloadedGenerator(
    private val plugin: GUIHelper,
    override val pluginName: String = "CrateReloaded",
    override val pluginVersion: String = "2.0.(23/35)",
    override val rgbFormat: (String) -> String = { "{#$it}" }
) : ConfigGenerator() {

    private val defaults = Defaults(pluginName)

    init {
        options.addOption('c') {
            longOpt("chance")
            arg(Int::class, 1, "chance", false)
            desc("The chance used for all items (default: took from config)")
        }
    }

    override fun getMessage(): String = "  &2$pluginName &av$pluginVersion &8- &fCrate prizes"

    override fun generate(context: GeneratorContext, input: CommandLine): Boolean {
        val config = Config(pluginName, plugin, true)

        val duration = measureTimeMillis {
            val rewards = mutableListOf<String>()

            context.forEach(5, pluginName) { item, _ -> rewards.add(createItem(input, item)) }
            config["GUIHelper.reward.rewards"] = rewards
        }

        config.save()
        Message.GENERATION_DONE.send(context, config.path, duration)
        return true
    }

    private fun createItem(input: CommandLine, item: ItemStack): String {
        val properties = mutableListOf<String>()
        val chance = input.getOrDefault('c', defaults[Value.TAGS__CHANCE]) { it.toIntOrNull() }

        if (defaults[Value.TAGS__UNIQUE]) {
            properties.add("unique:()")
        }

        if (defaults[Value.TAGS__ALWAYS]) {
            properties.add("always:()")
        }

        properties.add("chance:($chance)")
        properties.add(defaults[Value.TAGS__COMMANDS]) { "cmd:($it)" }
        properties.add(defaults[Value.TAGS__PERMISSIONS]) { "permission:($it)" }

        with(plugin.itemsManager.serialize(item, Serializer.CRATE_RELOADED)) {
            properties.add("display:($this)")

            if (defaults[Value.SETTINGS__GIVE_DISPLAY_ITEM]) {
                properties.add("item:($this)")
            }
        }

        properties.add(defaults[Value.TAGS__ITEMS]) { "item:($it)" }
        return properties.joinToString(", ")
    }

    private class Defaults(name: String) : DefaultValues(name, Value::class.java)

    @Description(
        " ",
        "Default values that will be used in the config creation process",
        " ",
        "▪ CrateReloaded 2.0.23 by dinosaur (https://spigotmc.org/resources/861/)",
        "▪ CrateReloaded PRO 2.0.35 by dinosaur (https://spigotmc.org/resources/3663/)",
        "▪ Wiki: https://crates.hazebyte.com/#/",
        " "
    )
    private object Value : SettingsHolder {

        @Comment("Whether or not the display:() will be also used as item:()")
        @Path("settings.giveDisplayItem")
        val SETTINGS__GIVE_DISPLAY_ITEM = create(true)

        @Comment("always:() - Reward that is always given regardless of the probability [1]")
        @Path("tags.always")
        val TAGS__ALWAYS = create(false)

        @Comment("chance:(<chance>) - Represents the raw weighted chance [1]")
        @Path("tags.chance")
        val TAGS__CHANCE = create(10)

        @Comment("cmd:(<command>) - Represents a command [∞]")
        @Path("tags.cmd")
        val TAGS__COMMANDS = create(emptyList<String>())

        @Comment("item:(<item>) - Represents an item [∞]")
        @Path("tags.item")
        val TAGS__ITEMS = create(emptyList<String>())

        @Comment("permission:(<permission>) - Reward that is given only if the player does not have the permission [∞]")
        @Path("tags.permission")
        val TAGS__PERMISSIONS = create(emptyList<String>())

        @Comment("unique:() - Reward that is only given once in a single crate probability roll [1]")
        @Path("tags.unique")
        val TAGS__UNIQUE = create(false)

        override fun registerComments(conf: CommentsConfiguration?) {
            if (conf == null) {
                return
            }

            with(conf) {
                setComment("settings", "GUIHelper specific settings")

                setComment(
                    "tags",
                    " ",
                    "Tags are used to identify the important values set in the config that is passed to the plugin.",
                    "Wiki: https://crates.hazebyte.com/#/config/crate?id=tag",
                    " "
                )
            }
        }

    }

}

private fun MutableList<String>.add(elements: List<String>, format: (String) -> String) {
    if (elements.isNotEmpty()) {
        addAll(elements.map { format(it) })
    }
}
