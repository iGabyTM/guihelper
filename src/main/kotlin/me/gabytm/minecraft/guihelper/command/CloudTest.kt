package me.gabytm.minecraft.guihelper.command

import me.gabytm.minecraft.guihelper.functions.send
import me.gabytm.minecraft.guihelper.generator.GeneratorsManager
import me.gabytm.minecraft.guihelper.generator.base.GeneratorContext
import me.gabytm.minecraft.guihelper.inventory.InventoryManager
import me.gabytm.minecraft.guihelper.util.Constants
import me.gabytm.minecraft.guihelper.util.Message
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.bukkit.CloudBukkitCapabilities
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.LegacyPaperCommandManager

class CloudTest(
	plugin: JavaPlugin,
	private val generatorsManager: GeneratorsManager,
	private val inventoryManager: InventoryManager
) {

	init {
		val manager = LegacyPaperCommandManager.createNative(plugin, ExecutionCoordinator.simpleCoordinator())

		if (manager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER) ||
			manager.hasCapability(CloudBukkitCapabilities.COMMODORE_BRIGADIER)
		) {
			manager.registerBrigadier();
		}

		/*manager.command(
			manager.commandBuilder("ghc")
				.literal("create")
				.optional("generator", StringParser.stringParser(), Description.description("The generator"), SuggestionProvider.suggestingStrings("deluxemenus", "shopguiplus"))
				.handler {
					val generator = it.optional<String>("generator")

					if (generator.isEmpty) {
						it.sender().sendMessage("Create...")
						return@handler
					}

					it.sender().sendMessage("Generator = ${generator.get()}")
				}
		)*/
		manager.command(
			manager.commandBuilder("ghc")
				.literal("create")
				.senderType(Player::class.java)
				.permission(Constants.PERMISSION)
				.handler { it.sender().openInventory(inventoryManager[it.sender()]) }
		)

		generatorsManager.registeredGeneratorsIds().forEach { generatorId ->
			val generator = generatorsManager.getGenerator(generatorId)!!
			var commandBuilder = manager.commandBuilder("ghc")
				.literal("create")
				.literal(generatorId)
				.senderType(Player::class.java)
				.permission(Constants.PERMISSION)

			generator.flags.map { it.toCommandFlag(manager) }
				.forEach { commandBuilder = commandBuilder.flag(it) }

			commandBuilder.handler { ctx ->
				val requiredFlags = generator.flags.filter { it.required }.sortedBy { it.names[0] }

				requiredFlags.firstOrNull { !ctx.flags().hasFlag(it.names[0]) }?.let {
					val argument = if (it.argumentName != null) {
						" <${it.argumentName}>"
					} else {
						""
					}
					Component.text("Missing flag ")
						.append(
							Component.text("--${it.names[0]}$argument").color(NamedTextColor.RED)
								.hoverEvent(Component.text(it.description).asHoverEvent())
						)
						.send(ctx.sender())
					return@handler
				}

				val inventory = inventoryManager[ctx.sender()]

				if (inventory.isEmpty) {
					Message.EMPTY_GUI.send(ctx.sender())
					return@handler
				}

				generator.generate(
					GeneratorContext(ctx.sender(), inventory),
					ctx.flags()
				)
			}.let(manager::command)
		}

		/*		val dmCommand = manager.commandBuilder("ghc")
					.literal("create")
					.literal("deluxemenus")

				val dmGenerator = generatorsManager.getGenerator("deluxemenus")!!

				dmGenerator.flags.map { it.toCommandFlag(manager) }.forEach { dmCommand.flag(it) }
				dmCommand.senderType(Player::class.java)
				dmCommand.handler { ctx -> dmGenerator.generate() }

				manager.command(
					manager.commandBuilder("ghc")
						.literal("create")
						.literal("deluxemenus")
						.flag(manager.flagBuilder("external").withAliases("e").build())
						.flag(
							manager.flagBuilder("headtype")
								.withComponent(EnumParser.enumParser(HeadIdProvider.Provider::class.java))
								.build()
						)
						.senderType(Player::class.java)
						.handler { ctx -> ctx.sender().sendMessage("ghc create deluxemenus, external: ${ctx.flags().hasFlag("external")}") }
				)

				manager.command(
					manager.commandBuilder("ghc")
						.literal("create")
						.literal("shopguiplus")
						.flag(manager.flagBuilder("page").withComponent(IntegerParser.integerParser(1)).build())
						.handler { ctx -> ctx.sender().sendMessage("ghc create shopguiplus, page: ${ctx.flags().hasFlag("page")} ${ctx.flags().get<Int>("page")}") }
				)*/
	}

}
