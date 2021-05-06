package me.gabytm.minecraft.guihelper.generators.implementations

import me.gabytm.minecraft.guihelper.GUIHelper
import me.gabytm.minecraft.guihelper.config.Config
import me.gabytm.minecraft.guihelper.functions.*
import me.gabytm.minecraft.guihelper.generators.base.ConfigGenerator
import me.gabytm.minecraft.guihelper.generators.base.GeneratorContext
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.LeatherArmorMeta

class DeluxeMenusGenerator(private val plugin: GUIHelper) : ConfigGenerator() {

    override fun generate(context: GeneratorContext, args: Array<String>): Boolean {
        val external = if (args.isEmpty()) true else args[0].toBoolean()
        val config = Config(if (external) "DeluxeMenus/gui_menus" else "DeluxeMenus", plugin)

        config.empty()

        for ((slot, item) in context.inventory.contents.withIndex()) {
            if (item.isInvalid) {
                continue
            }

            val path = if (external) "items.$slot" else "gui_menus.GUIHelper.items.$slot"
            createItem(config.createSection(path), item, slot)
        }

        config.save()
        return true
    }

    override fun tabCompletion(sender: Player, args: List<String>): List<String> {
        if (args.size == 1) {
            return listOf("<external>")
        }

        return emptyList()
    }

    override fun getMessage(): String {
        return "  &2DeluxeMenus &a(external) &8- &fExternal / local (config.yml) menus"
    }

    override fun createItem(section: ConfigurationSection, item: ItemStack, slot: Int) {
        val meta = item.itemMeta ?: Bukkit.getItemFactory().getItemMeta(item.type)

        section["material"] = if (item.isPlayerSkull) "basehead-${item.skullTexture}" else item.type.name

        item.durability.takeIf { it > 0 }?.let { section["data"] = it }

        if (item.amount > 1) {
            section["amount"] = item.amount
        }

        section["slot"] = slot

        item.customModelData.takeIf { it > 0 }?.let { section["nbt_int"] = "CustomModelData:$it" }

        if (meta == null) {
            return
        }

        if (item.isLeatherArmor) {
            val color = (meta as LeatherArmorMeta).color

            if (!color.isDefaultLeatherColor) {
                section["rgb"] = "${color.red},${color.green},${color.blue}"
            }
        } else if (item.isSpawnEgg) {
            section["data"] = item.spawnEggType.typeId
        }

        meta.itemFlags.forEach { flag ->
            when(flag) {
                ItemFlag.HIDE_ENCHANTS -> "hide_enchantments"
                ItemFlag.HIDE_ATTRIBUTES -> "hide_attributes"
                ItemFlag.HIDE_POTION_EFFECTS -> "hide_effects"
                else -> null
            }?.let { section[it] = true }
        }

        if (meta.hasDisplayName()) {
            section["display_name"] = item.displayName
        }

        if (meta.hasLore()) {
            section["lore"] = item.lore
        }

        val enchantments = meta.enchants.entries.map { "${it.key.name};${it.value}" }.toMutableList()

        if (meta is EnchantmentStorageMeta) {
            enchantments.addAll(meta.storedEnchants.entries.map { "${it.key.name};${it.value}" })
        }

        if (enchantments.isNotEmpty()) {
            section["enchantments"] = enchantments
        }
    }

}