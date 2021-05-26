package me.gabytm.minecraft.guihelper.items.serialization.serializers

import me.gabytm.minecraft.guihelper.functions.*
import me.gabytm.minecraft.guihelper.utils.ServerVersion
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.Potion
import org.bukkit.potion.PotionData

@Suppress("DEPRECATION")
class CrateReloadedImplementation : ItemSerializer() {

    private val rgbFormat: (String) -> String = { "{#$it}" }

    override fun serialize(item: ItemStack): String {
        checkItem(item)

        return buildString {
            append(item.type.name)
            item.durability.ifNotZero { append(':').append(it) }
            append(' ').append(item.amount)

            val meta = item.meta ?: return@buildString

            if (meta.hasDisplayName()) {
                append(" name:").append(item.displayName(rgbFormat).removeSpace())
            }

            if (meta.hasLore()) {
                append(" lore:").append(item.lore().joinToString("|").removeSpace())
            }

            item.isUnbreakable.takeIf { it }?.let { append(" unbreakable:true") }

            item.enchants { enchantment, level -> "${enchantment.name.lowercase()}:$level" }
                .ifNotEmpty { append(it.joinToString(" ", " ")) }

            meta.itemFlags.ifNotEmpty { append(it.joinToString(",", " flag:")) }
            appendMetaSpecificValues(this, item, meta)
        }
    }

    private fun appendMetaSpecificValues(builder: StringBuilder, item: ItemStack, meta: ItemMeta) {
        when {
            item.isLeatherArmor -> {
                (meta as LeatherArmorMeta).color.ifNotDefault { builder.append(" color:").append(it.asString()) }
            }
            item.isPlayerHead -> {
                builder.append(" skull:").append(item.skullTexture)
            }
            item.isPotion -> {
                if (ServerVersion.isAncient) {
                    builder.appendPotion(Potion.fromItemStack(item))
                } else {
                    builder.appendPotion((meta as PotionMeta).basePotionData, item.isSplashPotion)
                }
            }
        }
    }

}

private fun StringBuilder.appendPotion(potion: Potion) {
    if (potion.type.effectType == null) {
        return
    }

    this.append(" effect:").append(potion.type.effectType?.name)
        .append(" power:").append(potion.level)
        .append(" duration:").append(potion.effects.first().duration)
        .append(" splash:").append(potion.isSplash)
}

private fun StringBuilder.appendPotion(potion: PotionData, splash: Boolean) {
    if (potion.type.effectType == null) {
        return
    }

    this.append(" effect:").append(potion.type.effectType?.name)
        .append(" power:").append(if (potion.isUpgraded) 2 else 1)
        .append(" duration:1") // TODO: Find a way to get the duration of a potion
        .append(" splash:").append(splash)
}

private fun String.removeSpace() = this.replace(' ', '_')
