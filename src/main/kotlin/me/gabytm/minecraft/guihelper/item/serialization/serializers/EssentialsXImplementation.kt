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

@file:Suppress("SpellCheckingInspection")

package me.gabytm.minecraft.guihelper.item.serialization.serializers

import me.gabytm.minecraft.guihelper.functions.*
import me.gabytm.minecraft.guihelper.item.ItemsManager
import me.gabytm.minecraft.guihelper.item.heads.providers.HeadIdProvider
import me.gabytm.minecraft.guihelper.util.ServerVersion
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
        when {
            item.isFirework -> {
                builder.appendFirework(meta as FireworkMeta)
            }

            item.isFireworkStar -> {
                builder.appendFirework(meta as FireworkMeta)
            }

            item.isLeatherArmor -> {
                (meta as LeatherArmorMeta).color.ifNotDefault { builder.append(" color:").append(it.asRGB()) }
            }

            item.isPlayerHead -> {
                itemsManager.getHeadId(item, HeadIdProvider.Provider.PLAYER_NAME)
                    .takeIf { it != HeadIdProvider.DEFAULT }
                    ?.let { builder.append(" player:").append(it) }
            }

            item.isPotion -> {
                if (ServerVersion.IS_ANCIENT) {
                    builder.appendPotion(Potion.fromItemStack(item))
                } else {
                    builder.appendPotion((meta as PotionMeta).basePotionData, item.isSplashPotion)
                }
            }

            item.isShield || item.isBanner -> {
                builder.appendShieldOrBanner(item)
            }

            item.type == Material.WRITTEN_BOOK -> {
                builder.appendWrittenBook(meta as BookMeta)
            }
        }
    }

}

private val colorStringFormat: (Color) -> String = { "#${it.asHex()}" }

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
