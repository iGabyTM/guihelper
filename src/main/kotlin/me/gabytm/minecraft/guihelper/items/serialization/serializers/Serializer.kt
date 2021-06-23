package me.gabytm.minecraft.guihelper.items.serialization.serializers

import java.util.*

enum class Serializer(val alias: String) {

    CRATE_RELOADED("cratereloaded"),
    ESSENTIALSX("essentials"),
    VANILLA("");

    companion object {

        private val serializers = EnumSet.allOf(Serializer::class.java)

        fun getSerializer(string: String, default: Serializer = VANILLA): Serializer {
            return serializers.firstOrNull {
                it.name.equals(string, true) ||
                        it.alias.equals(string, true) ||
                        it.name.replace("_", "").equals(string, true)
            } ?: default
        }

    }

}