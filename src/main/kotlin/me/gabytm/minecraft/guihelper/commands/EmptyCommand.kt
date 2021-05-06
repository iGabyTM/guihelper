package me.gabytm.minecraft.guihelper.commands

import me.gabytm.minecraft.guihelper.utils.Constants
import me.gabytm.minecraft.guihelper.inventories.InventoryManager
import me.mattstudios.mf.annotations.Alias
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.entity.Player

@Command(Constants.COMMAND)
@Alias(Constants.ALIAS)
class EmptyCommand(private val manager: InventoryManager) : CommandBase() {

    @Permission(Constants.PERMISSION)
    @SubCommand("empty")
    fun onCommand(player: Player) = manager.removeInventory(player.uniqueId)

}