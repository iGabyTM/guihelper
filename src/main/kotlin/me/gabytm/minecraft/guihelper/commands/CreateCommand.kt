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

package me.gabytm.minecraft.guihelper.commands

import me.gabytm.minecraft.guihelper.functions.isNotEmpty
import me.gabytm.minecraft.guihelper.generators.GeneratorsManager
import me.gabytm.minecraft.guihelper.generators.base.GeneratorContext
import me.gabytm.minecraft.guihelper.inventories.InventoryManager
import me.gabytm.minecraft.guihelper.utils.Constants
import me.gabytm.minecraft.guihelper.utils.Message
import me.mattstudios.mf.annotations.*
import me.mattstudios.mf.base.CommandBase
import org.apache.commons.cli.*
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

@Command(Constants.COMMAND)
@Alias(Constants.ALIAS)
class CreateCommand(private val manager: GeneratorsManager, private val inventoryManager: InventoryManager) : CommandBase() {

    private val commandParser = DefaultParser()

    @Completion("#generators")
    @Permission(Constants.PERMISSION)
    @SubCommand("create")
    fun onCommand(sender: Player, args: Array<String>) {
        if (args.size == 1) { // /gh create
            sender.openInventory(inventoryManager[sender])
            return
        }

        val generatorId = args[1]
        val generator = manager.getGenerator(generatorId)

        if (generator == null) {
            Message.UNKNOWN_GENERATOR.send(sender, generatorId)
            return
        }

        val inventory = inventoryManager[sender]

        if (inventory.isNotEmpty) {
            try {
                generator.generate(
                    GeneratorContext(sender, inventoryManager[sender]),
                    commandParser.parse(generator.options, args.copyOfRange(2, args.size))
                )
            } catch (e: ParseException) {
                when (e) {
                    // TODO: Add messages
                    is MissingArgumentException -> sender.sendMessage("Missing argument for ${e.option}")
                    is MissingOptionException -> sender.sendMessage("Missing option(s): ${e.missingOptions.joinToString(", ")}")
                    is UnrecognizedOptionException -> sender.sendMessage("Unknown option ${e.option}")
                }
            }
            return
        }

        Message.EMPTY_GUI.send(sender)
    }

    @CompleteFor("create")
    fun tabCompletion(args: List<String>, sender: Player): List<String> {
        if (args.size == 1) { // /gh create <tab>
            return StringUtil.copyPartialMatches(args[0], manager.registeredGeneratorsIds(), mutableListOf())
        }

        return emptyList()
    }

    /*
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
            Message.UNKNOWN_GENERATOR.send(player, generatorId)
            return
        }

        val inventory = inventoryManager[player]

        if (inventory.isNotEmpty) {
            generator.generate(GeneratorContext(player, inventory), args.copyOfRange(2, args.size))
            return
        }

        Message.EMPTY_GUI.send(player)
    }*/

    /*
    @CompleteFor("create")
    fun tabCompletion(args: List<String>, sender: Player): List<String> {
        if (args.size == 1) { // /gh create <tab>
            return manager.registeredGeneratorsIds()
        }

        val generator = manager.getGenerator(args[0]) ?: return emptyList()
        return generator.tabCompletion(sender, args.subList(1, args.size))
    }
     */

}