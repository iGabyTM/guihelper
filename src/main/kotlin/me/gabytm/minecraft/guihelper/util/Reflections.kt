package me.gabytm.minecraft.guihelper.util

import com.google.common.base.Preconditions
import org.bukkit.DyeColor
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta
import org.bukkit.potion.PotionEffect
import java.lang.reflect.Method

object Reflections {

	/**
	 * Class removed in 1.21
	 */
	private val potionClass: Class<*>? = if (ServerVersion.IS_ANCIENT) {
		Class.forName("org.bukkit.potion.Potion")
	} else {
		null
	}

	private val potion_fromItemStack: Method? = if (ServerVersion.IS_ANCIENT) {
		potionClass?.getMethod("fromItemStack", ItemStack::class.java)
	} else {
		null
	}

	private val potion_getEffects: Method? = if (ServerVersion.IS_ANCIENT) {
		potionClass?.getMethod("getEffects")
	} else {
		null
	}

	private val potion_isSplash: Method? = if (ServerVersion.IS_ANCIENT) {
		potionClass?.getMethod("isSplash")
	} else {
		null
	}

	fun potionFromItemStack(item: ItemStack): Any {
		Preconditions.checkArgument(ServerVersion.IS_ANCIENT, "In this version you can check if the material is Material.SPLASH_POTION")
		Preconditions.checkNotNull(potion_fromItemStack, "Could not get method org.bukkit.potion.Potion#fromItemStack")

		// Kotlin null check
		if (potion_fromItemStack == null) {
			return false
		}

		return potion_fromItemStack.invoke(null, item)
	}

	fun potionGetEffects(potion: Any): Collection<PotionEffect> {
		Preconditions.checkArgument(ServerVersion.IS_ANCIENT, "You should not use this method in this version")
		Preconditions.checkNotNull(potion_getEffects, "Could not get method org.bukkit.potion.Potion#getEffects")

		// Kotlin null check
		if (potion_getEffects == null) {
			return emptyList()
		}

		return potion_getEffects.invoke(potion) as Collection<PotionEffect>
	}

	fun potionIsSplash(potion: Any): Boolean {
		Preconditions.checkArgument(ServerVersion.IS_ANCIENT, "You should not use this method in this version")
		Preconditions.checkNotNull(potion_isSplash, "Could not get method org.bukkit.potion.Potion#getEffects")

		if (potion_isSplash == null) {
			return false
		}

		return potion_isSplash.invoke(potion) as Boolean
	}

	fun isSplashPotion(item: ItemStack): Boolean {
		Preconditions.checkArgument(ServerVersion.IS_ANCIENT, "In this version you can check if the material is Material.SPLASH_POTION")
		Preconditions.checkNotNull(potion_fromItemStack, "Could not get method org.bukkit.potion.Potion#fromItemStack")
		Preconditions.checkNotNull(potion_isSplash, "Could not get method org.bukkit.potion.Potion#isSplash")

		// Kotlin null check
		if (potionClass == null || potion_fromItemStack == null || potion_isSplash == null) {
			return false
		}

		val potion = potion_fromItemStack.invoke(item)
		return potionIsSplash(potion)
	}

	private val bannerMeta_getBaseColor: Method? = if (ServerVersion.IS_LEGACY) {
		BannerMeta::class.java.getMethod("getBaseColor")
	} else {
		null
	}

	fun bannerMetaGetBaseColor(meta: BannerMeta): DyeColor {
		Preconditions.checkArgument(ServerVersion.IS_LEGACY, "In this version you can get the color from the material's name")
		Preconditions.checkNotNull(bannerMeta_getBaseColor, "Could not get method ${BannerMeta::class.java.simpleName}#getBaseColor")

		// Kotlin null check
		if (bannerMeta_getBaseColor == null) {
			return DyeColor.WHITE
		}

		return bannerMeta_getBaseColor.invoke(meta) as DyeColor
	}

}
