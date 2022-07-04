package me.gabytm.minecraft.guihelper.functions

import me.gabytm.minecraft.guihelper.GUIHelper
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

private val plugin = JavaPlugin.getPlugin(GUIHelper::class.java)

fun Component.send(receiver: CommandSender) = plugin.audiences.sender(receiver).sendMessage(this)