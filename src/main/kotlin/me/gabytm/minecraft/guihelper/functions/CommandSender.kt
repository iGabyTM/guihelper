package me.gabytm.minecraft.guihelper.functions

import me.gabytm.minecraft.guihelper.utils.Message
import org.bukkit.command.CommandSender

fun CommandSender.sendMessage(message: Message) = sendMessage(message.value)