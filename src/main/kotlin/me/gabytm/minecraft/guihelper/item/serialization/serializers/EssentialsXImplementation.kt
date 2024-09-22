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

import com.earth2me.essentials.Essentials
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class EssentialsXImplementation : ItemSerializer() {

    override fun serialize(item: ItemStack): String {
        checkItem(item)

		if (Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
			return JavaPlugin.getPlugin(Essentials::class.java).itemDb.serialize(item)
		} else {
			throw RuntimeException("EssentialsX is not enabled, can not serialize items without it")
		}

        /*return buildString {
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
        }*/
    }

    /*@Suppress("DEPRECATION")
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
                itemsManager.getHeadId(item, HeadIdProvider.Provider.PLAYER_NAME).let { id -> builder.append(" player:$id") }
            }

			// https://github.com/EssentialsX/Essentials/blob/6157668a631ed9c53468831e6007a3ba49f31edb/Essentials/src/main/java/com/earth2me/essentials/items/AbstractItemDb.java#L286
            item.isPotion -> {
				when {
					ServerVersion.IS_ANCIENT -> builder.appendPotion(Reflections.potionFromItemStack(item))
					ServerVersion.POTION_DATA_IS_DEPRECATED -> {
						val base = (meta as PotionMeta).basePotionType ?: return
						val amplifier = if (base.name.startsWith("LONG_")) 1 else 0
						builder.appendPotion(item.isSplashPotion, base.name, amplifier, Ticks.TICKS_PER_SECOND)
					}
					else -> {
						val base = (meta as PotionMeta).basePotionData ?: return
						val amplifier = if (base.isExtended) 1 else 0
						builder.appendPotion(item.isSplashPotion, base.type.name, amplifier, Ticks.TICKS_PER_SECOND)
					}
				}
            }

            item.isShield || item.isBanner -> {
                builder.appendShieldOrBanner(item)
            }

            item.type == Material.WRITTEN_BOOK -> {
                builder.appendWrittenBook(meta as BookMeta)
            }
        }
    }*/

}

/*private val colorStringFormat: (Color) -> String = { "#${it.asHex()}" }

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

@Suppress("DEPRECATION") // only used in OLD versions where PotionType.name() exists
private fun StringBuilder.appendPotion(potion: Any) {
	val isSplash = Reflections.potionIsSplash(potion)

    Reflections.potionGetEffects(potion).forEach {
        appendPotion(isSplash, it.type.name, it.amplifier, it.duration)
    }
}

private fun StringBuilder.appendPotion(splash: Boolean, effect: String, amplifier: Int, duration: Int) {
    this.append(" splash:").append(splash)
        .append(" effect:").append(effect.lowercase())
        .append(" power:").append(amplifier)
        .append(" duration:").append(duration.ticksToSeconds())
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

private fun String.removeSpace() = this.replace(' ', '_')*/
