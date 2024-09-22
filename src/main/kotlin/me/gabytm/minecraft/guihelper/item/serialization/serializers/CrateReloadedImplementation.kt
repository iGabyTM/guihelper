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

package me.gabytm.minecraft.guihelper.item.serialization.serializers

import me.gabytm.minecraft.guihelper.functions.*
import me.gabytm.minecraft.guihelper.util.ServerVersion
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta

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

			item.customModelData.ifNotZero { append("custommodeldata:$it") }
			item.isUnbreakable.takeIf { it }?.let { append(" unbreakable:true") }

			item.enchants { enchantment, level -> "${enchantment.name}:$level" }
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
				item.skullTexture?.let { texture -> builder.append(" skull:$texture") }
			}

			//TODO update potion API
			/*item.isPotion -> {
				if (ServerVersion.IS_ANCIENT) {
					builder.appendPotion(Potion.fromItemStack(item))
				} else {
					builder.appendPotion(meta as PotionMeta, item.isSplashPotion)
				}
			}*/
		}
	}

}

const val EFFECT: String = " effect:"
const val POWER: String = " power:"
const val DURATION: String = " duration:"
const val SPLASH: String = " splash:"

//TODO update potion API
/*private fun StringBuilder.appendPotion(potion: Potion) {
	if (potion.type.effectType == null) {
		return
	}

	this.append(EFFECT).append(potion.type.effectType?.name)
		.append(POWER).append(potion.level)
		.append(DURATION).append(potion.effects.first().duration.ticksToSeconds())
		.append(SPLASH).append(potion.isSplash)
}

private fun StringBuilder.appendPotion(potionMeta: PotionMeta, splash: Boolean) {
	val basePotionData = potionMeta.basePotionData

	if (basePotionData.type.effectType != null) {
		append(EFFECT).append(basePotionData.type.effectType?.name)
		append(POWER).append(if (basePotionData.isUpgraded) 2 else 1)
	}

	if (potionMeta.hasCustomEffects()) {
		potionMeta.customEffects.forEach { customEffect ->
			append(EFFECT).append(customEffect.type.name)
			customEffect.amplifier.ifNotZero { power -> append(POWER).append(power) }
			append(DURATION).append(customEffect.duration.ticksToSeconds())
		}
	}

	if (splash) {
		append(SPLASH).append(true)
	}
}*/

private fun String.removeSpace() = this.replace(' ', '_')
