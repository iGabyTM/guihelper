package me.gabytm.minecraft.guihelper.commands

import me.gabytm.minecraft.guihelper.functions.isNotEmpty
import me.gabytm.minecraft.guihelper.functions.sendMessage
import me.gabytm.minecraft.guihelper.generators.GeneratorsManager
import me.gabytm.minecraft.guihelper.generators.base.GeneratorContext
import me.gabytm.minecraft.guihelper.inventories.InventoryManager
import me.gabytm.minecraft.guihelper.utils.Constants
import me.gabytm.minecraft.guihelper.utils.Message
import me.mattstudios.mf.annotations.*
import me.mattstudios.mf.base.CommandBase
import org.bukkit.entity.Player

@Command(Constants.COMMAND)
@Alias(Constants.ALIAS)
class CreateCommand(private val manager: GeneratorsManager, private val inventoryManager: InventoryManager) : CommandBase() {

    @Permission(Constants.PERMISSION)
    @SubCommand("create")
    fun onCommand(player: Player, args: Array<String>) {
        if (args.size == 1) { // /gh create
            player.openInventory(inventoryManager[player])
            return
        }

        val generatorId = args[1]
        val generator = manager.getGenerator(generatorId)

        if (generator == null) {
            player.sendMessage("No generator found with id $generatorId")
            return
        }

        val inventory = inventoryManager[player]

        if (inventory.isNotEmpty) {
            generator.generate(GeneratorContext(player, inventory), args.copyOfRange(2, args.size))
            return
        }

        player.sendMessage(Message.EMPTY_GUI)
    }

    @CompleteFor("create")
    fun tabCompletion(args: List<String>, sender: Player): List<String> {
        if (args.size == 1) { // /gh create <tab>
            return manager.getRegisteredGeneratorsIds()
        }

        val generator = manager.getGenerator(args[0]) ?: return emptyList()
        return generator.tabCompletion(sender, args.subList(1, args.size))
    }

}