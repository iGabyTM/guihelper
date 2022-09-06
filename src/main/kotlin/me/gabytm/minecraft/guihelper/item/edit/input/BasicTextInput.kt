package me.gabytm.minecraft.guihelper.item.edit.input

import me.gabytm.minecraft.guihelper.GUIHelper
import org.bukkit.conversations.Conversation
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class BasicTextInput(player: Player, inputConsumer: (String) -> Unit) {

	init {
	    player.beginConversation(
			Conversation(
				JavaPlugin.getProvidingPlugin(GUIHelper::class.java),
				player,
				Prompt(inputConsumer)
			).apply {
				isLocalEchoEnabled = false
			}
		)
	}

	private class Prompt(private val inputConsumer: (String) -> Unit) : StringPrompt() {

		override fun getPromptText(context: ConversationContext): String = ""

		override fun acceptInput(context: ConversationContext, input: String?): Prompt? {
			if (input != null) {
				inputConsumer(input)
			}

			return null
		}

	}

}
