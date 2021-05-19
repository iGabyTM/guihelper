package me.gabytm.minecraft.guihelper.items.heads.providers

import me.gabytm.minecraft.guihelper.functions.skullTexture
import org.bukkit.inventory.ItemStack
import java.util.*

class TextureImplementation(pattern: String) : HeadIdProvider() {

    private val regex = Regex(pattern)

    override fun getId(item: ItemStack): String {
        checkItem(item)
        val texture = item.skullTexture

        if (texture == DEFAULT) {
            return DEFAULT
        }

        val matcher = regex.find(String(Base64.getDecoder().decode(texture))) ?: return DEFAULT
        return matcher.groupValues[1]
    }

}