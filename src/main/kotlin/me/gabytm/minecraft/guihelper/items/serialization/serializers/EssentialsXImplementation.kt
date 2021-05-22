@file:Suppress("SpellCheckingInspection")

package me.gabytm.minecraft.guihelper.items.serialization.serializers

import me.gabytm.minecraft.guihelper.functions.*
import me.gabytm.minecraft.guihelper.items.ItemsManager
import me.gabytm.minecraft.guihelper.items.heads.providers.HeadIdProvider
import me.gabytm.minecraft.guihelper.utils.ServerVersion
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.*
import org.bukkit.potion.Potion
import org.bukkit.potion.PotionData

class EssentialsXImplementation(private val itemsManager: ItemsManager) : ItemSerializer() {

    @Suppress("DEPRECATION")
    override fun serialize(item: ItemStack): String {
        checkItem(item)
        return buildString {
            append(item.type)
            item.durability.ifNotZero { append(':').append(it) }
            append(' ').append(item.amount)

            if (!item.hasItemMeta()) {
                return@buildString
            }

            val meta = item.itemMeta ?: return@buildString

            if (meta.hasDisplayName()) {
                append(" name:").append(item.displayName().removeSpace())
            }

            if (meta.hasLore()) {
                append(" lore:").append(item.lore().joinToString("|").removeSpace())
            }

            item.enchants { enchantment, level -> "${enchantment.name.lowercase()}:$level" }
                .ifNotEmpty { append(it.joinToString(" ", " ")) }

            meta.itemFlags.ifNotEmpty {
                append(it.joinToString(",", " itemflags:") { flag -> flag.name })
            }

            appendMetaSpecificValues(this, item, meta)
        }
    }

    @Suppress("DEPRECATION")
    private fun appendMetaSpecificValues(builder: StringBuilder, item: ItemStack, meta: ItemMeta) {
        if (item.type == Material.FIREWORK_ROCKET) {
            builder.appendFirework(meta as FireworkMeta)
            return
        }

        if (item.isFireworkStar) {
            if ((meta as FireworkEffectMeta).hasEffect()) {
                builder.appendFireworkEffect(meta.effect!!)
            }

            return
        }

        if (item.isLeatherArmor) {
            (meta as LeatherArmorMeta).color.ifNotDefault {
                builder.append(" color:").append(it.asRGB())
            }
            return
        }

        if (item.isPlayerHead) {
            itemsManager.getHeadId(item, HeadIdProvider.Provider.PLAYER_NAME)
                .takeIf { it != HeadIdProvider.DEFAULT }
                ?.let { builder.append(" player:").append(it) }
            return
        }

        if (item.isPotion) {
            if (ServerVersion.isAncient) {
                builder.appendPotion(Potion.fromItemStack(item))
            } else {
                builder.appendPotion((meta as PotionMeta).basePotionData, item.isSplashPotion)
            }
            return
        }

        if (item.isShield || item.isBanner) {
            builder.appendShieldOrBanner(item)
            return
        }

        if (item.type == Material.WRITTEN_BOOK) {
            builder.appendWrittenBook(meta as BookMeta)
        }
    }

}

private val colorStringFormat: (Color) -> String = { "#${Integer.toHexString(it.asRGB())}" }

private fun StringBuilder.appendFirework(firework: FireworkMeta) {
    if (firework.hasEffects()) {
        firework.effects.forEach { appendFireworkEffect(it) }
    }

    append(" power:").append(firework.power)
}

private fun StringBuilder.appendFireworkEffect(effect: FireworkEffect) {
    effect.colors.ifNotEmpty {
        append(it.joinToString(",", " color:") { color -> color.asString(colorStringFormat) })
    }

    append(" shape:").append(effect.type.name)

    effect.fadeColors.ifNotEmpty {
        append(it.joinToString(",", " fade:") { color -> color.asString(colorStringFormat) })
    }
}

@Suppress("DEPRECATION")
private fun StringBuilder.appendPotion(potion: Potion) {
    potion.effects.forEach {
        appendPotion(potion.isSplash, it.type.name, it.amplifier, it.duration)
    }
}

private fun StringBuilder.appendPotion(potion: PotionData, splash: Boolean) {
    with (potion) {
        appendPotion(splash, type.name, if (isExtended) 1 else 0, 20)
    }
}

private fun StringBuilder.appendPotion(splash: Boolean, effect: String, amplifier: Int, duration: Int) {
    this.append(" splash:").append(splash)
        .append(" effect:").append(effect.lowercase())
        .append(" power:").append(amplifier)
        .append(" duration:").append(duration / 20)
}

private fun StringBuilder.appendShieldOrBanner(item: ItemStack) {
    val (patterns, color) = item.patternsAndBaseColor(false)

    color?.let { append(" basecolor:").append(it.color.asRGB()) }
    patterns.ifNotEmpty {
        append(it.joinToString(" "," ") { pattern ->
            "${pattern.pattern.identifier},${pattern.color.color.asRGB()}"
        })
    }
}

private fun StringBuilder.appendWrittenBook(book: BookMeta) {
    if (book.hasTitle()) {
        append(" title:").append(book.title?.removeSpace())
    }

    if (book.hasAuthor()) {
        append(" author:").append(book.author)
    }
}

private fun String.removeSpace() = this.replace(' ', '_')