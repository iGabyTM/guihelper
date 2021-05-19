package me.gabytm.minecraft.guihelper.commands

import me.gabytm.minecraft.guihelper.generators.GeneratorsManager
import me.gabytm.minecraft.guihelper.utils.Constants
import me.gabytm.minecraft.guihelper.utils.Message
import me.mattstudios.mf.annotations.Alias
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Completion
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import me.rayzr522.jsonmessage.JSONMessage
import org.apache.commons.cli.Option
import org.bukkit.ChatColor
import org.bukkit.entity.Player

@Command(Constants.COMMAND)
@Alias(Constants.ALIAS)
class OptionsCommand(private val manager: GeneratorsManager) : CommandBase() {

    @Completion("#generators")
    @SubCommand("options")
    fun onCommand(sender: Player, generatorId: String) {
        val generator = manager.getGenerator(generatorId) ?: kotlin.run {
            Message.UNKNOWN_GENERATOR.send(sender, generatorId)
            return
        }

        createHelpMessage(generator.options.options).send(sender)
    }

    private fun createHelpMessage(options: Collection<Option>): JSONMessage {
        val message = JSONMessage.create()

        for (option in options.sortedByDescending { it.isRequired }) {
            if (option.hasLongOpt()) {
                message.then("-${option.opt}, --${option.longOpt}")
            } else {
                message.then("-${option.opt}")
            }

            if (option.hasArg()) {
                if (option.hasOptionalArg()) {
                    message.then(" (${option.argName})")
                } else {
                    message.then(" [${option.argName}]")
                }
            }

            if (option.isRequired) {
                message.color(ChatColor.RED)
            } else {
                message.color(ChatColor.WHITE)
            }

            message.then(" (hover)").color(ChatColor.GRAY).tooltip(option.description).newline()
        }

        /*
        return buildString {
            for ((i, option) in opt.withIndex()) {
                if (i != 0) {
                    append("\n")
                }

                if (option.isRequired) {
                    append(ChatColor.RED)
                } else {
                    append(ChatColor.WHITE)
                }

                if (option.hasLongOpt()) {
                    append('-').append(option.opt).append(", --").append(option.longOpt)
                } else {
                    append('-').append(option.opt)
                }

                if (option.hasArg()) {
                    if (option.hasOptionalArg()) {
                        append(" (").append(option.argName).append(')')
                    } else {
                        append(" [").append(option.argName).append(']')
                    }
                }

                append("   ").append(option.description)
            }*/
            return message
    }

}