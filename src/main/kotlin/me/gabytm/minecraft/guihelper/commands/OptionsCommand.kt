package me.gabytm.minecraft.guihelper.commands

import me.gabytm.minecraft.guihelper.generators.GeneratorsManager
import me.gabytm.minecraft.guihelper.utils.Constants
import me.gabytm.minecraft.guihelper.utils.Message
import me.mattstudios.mf.annotations.*
import me.mattstudios.mf.base.CommandBase
import org.bukkit.entity.Player

@Command(Constants.COMMAND)
@Alias(Constants.ALIAS)
class OptionsCommand(private val manager: GeneratorsManager) : CommandBase() {

    @Completion("#generators")
    @Permission(Constants.PERMISSION)
    @SubCommand("options")
    fun onCommand(sender: Player, generatorId: String) {
        val generator = manager.getGenerator(generatorId) ?: kotlin.run {
            Message.UNKNOWN_GENERATOR.send(sender, generatorId)
            return
        }

        generator.optionsMessage.send(sender)
    }

}
