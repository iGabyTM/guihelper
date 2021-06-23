package me.gabytm.minecraft.guihelper.items.edit.editors

import me.gabytm.minecraft.guihelper.functions.*
import me.rayzr522.jsonmessage.JSONMessage
import org.apache.commons.cli.CommandLine
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class EnchantmentsEditor : ItemEditor() {

    private val enchantments = JSONMessage.create("hover").tooltip(
        Enchantment.values().sortedBy { it.name }.joinToString("&7, &f") { it.name.lowercase() }.color()
    )

    init {
        options.addOption('c') {
            longOpt("clear")
        }

        options.addOption('r') {
            longOpt("remove")
            arg(String::class, 1, "enchantment")
        }
    }

    override fun edit(item: ItemStack, executor: Player, input: CommandLine) {
        checkItem(item)

        val meta = item.meta ?: return

        when {
            input.hasOption('a') -> {
                val enchantment = input.getArg('a') { Enchantment.getByName(it.uppercase()) } ?: kotlin.run {
                    enchantments.send(executor)
                    return
                }
                val level = if (input.args.isEmpty()) 1 else input.args[0].toIntOrNull() ?: 1

                meta.addEnchant(enchantment, level, true)
                item.itemMeta = meta
            }

            input.hasOption('c') -> {
                meta.enchants.keys.forEach { meta.removeEnchant(it) }
                item.itemMeta = meta
            }

            input.hasOption('r') -> {
                val enchantment = input.getArg('r') { Enchantment.getByName(it.uppercase()) } ?: kotlin.run {
                    enchantments.send(executor)
                    return
                }
                meta.removeEnchant(enchantment)
                item.itemMeta = meta
            }
        }
    }

}