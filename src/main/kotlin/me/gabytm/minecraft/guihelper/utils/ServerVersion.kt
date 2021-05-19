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

package me.gabytm.minecraft.guihelper.utils

import org.bukkit.Bukkit

/**
 * Util class mostly used for calling the right method for each version
 *
 * @param id an id created using the version number, 1.16.5 => 1165
 * @param nmsName the NMS name of a version
 * @param bukkitName the bukkit name of a version ([Bukkit.getVersion])
 */
@Suppress("unused")
enum class ServerVersion(val id: Int = 0, val nmsName: String? = null, val bukkitName: String? = null) {

    UNKNOWN,

    /**
     * Legacy versions
     */
    V1_8(180, "v1_8_R1", "1.8-R0.1-SNAPSHOT"),
    V1_8_3(183, "v1_8_R2", "1.8.3-R0.1-SNAPSHOT"),
    V1_8_4(184, "v1_8_R3", "1.8.8-R0.1-SNAPSHOT"),
    V1_8_5(185, "v1_8_R3", "1.8.8-R0.1-SNAPSHOT"),
    V1_8_6(186, "v1_8_R3", "1.8.8-R0.1-SNAPSHOT"),
    V1_8_7(187, "v1_8_R3", "1.8.8-R0.1-SNAPSHOT"),
    V1_8_8(188, "v1_8_R3", "1.8.8-R0.1-SNAPSHOT"),

    V1_9(190, "v1_9_R1", "1.9-R0.1-SNAPSHOT"),
    V1_9_2(192, "v1_9_R1", "1.9.2-R0.1-SNAPSHOT"),
    V1_9_4(194, "v1_9_R2", "1.9.4-R0.1-SNAPSHOT"),

    V1_10(1100, "v1_10_R1", "1.10.2-R0.1-SNAPSHOT"),
    V1_10_2(1102, "v1_10_R1", "1.10.2-R0.1-SNAPSHOT"),

    V1_11(1110, "v1_11_R1", "1.11-R0.1-SNAPSHOT"),
    V1_11_1(1111, "v1_11_R1", "1.11.2-R0.1-SNAPSHOT"),
    V1_11_2(1112, "v1_11_R1", "1.11.2-R0.1-SNAPSHOT"),

    V1_12(1120, "v1_12_R1", "1.12-R0.1-SNAPSHOT"),
    V1_12_1(1121, "v1_12_R1", "1.12.1-R0.1-SNAPSHOT"),
    V1_12_2(1122, "v1_12_R1", "1.12.2-R0.1-SNAPSHOT"),

    /**
     * New versions
     */
    V1_13(1130, "v1_13_R1", "1.13-R0.1-SNAPSHOT"),
    V1_13_1(1131, "v1_13_R2", "1.13.1-R0.1-SNAPSHOT"),
    V1_13_2(1132, "v1_13_R2", "1.13.2-R0.1-SNAPSHOT"),

    V1_14(1140, "v1_14_R1", "1.14-R0.1-SNAPSHOT"),
    V1_14_1(1141, "v1_14_R1", "1.14.1-R0.1-SNAPSHOT"),
    V1_14_2(1142, "v1_14_R1", "1.14.2-R0.1-SNAPSHOT"),
    V1_14_3(1143, "v1_14_R1", "1.14.3-R0.1-SNAPSHOT"),

    V1_15(1150, "v1_15_R1", "1.15-R0.1-SNAPSHOT"),
    V1_15_1(1151, "v1_15_R1", "1.15.1-R0.1-SNAPSHOT"),
    V1_15_2(1152, "v1_15_R1", "1.15.2-R0.1-SNAPSHOT"),

    V1_16_1(1161, "v1_16_R1", "1.16.1-R0.1-SNAPSHOT"),
    V1_16_2(1162, "v1_16_R2", "1.16.2-R0.1-SNAPSHOT"),
    V1_16_3(1162, "v1_16_R2", "1.16.3-R0.1-SNAPSHOT"),
    V1_16_4(1164, "v1_16_R3", "1.16.4-R0.1-SNAPSHOT"),
    V1_16_5(1165, "v1_16_R3", "1.16.5-R0.1-SNAPSHOT");

    /**
     * Check if the version is _ancient_ by comparing its id with the ID of [V1_9]
     *
     * Ancient = version older than 1.9
     */
    val isAncient: Boolean
        get() = this.id < V1_9.id

    /**
     * Check if the version is legacy by comparing its id with the id of [V1_13]
     *
     * Legacy = version older than 1.13
     */
    val isLegacy: Boolean
        get() = this.id < V1_13.id

    /**
     * Check if the version has hex support by comparing its id with the id of [V1_15_2]
     */
    val supportHex: Boolean
        get() = this.id > V1_15_2.id

    fun isAny(vararg versions: ServerVersion): Boolean = versions.any { this.id == it.id }

    /**
     * Check if the version is newer than another version by comparing their [id] (this.id > other.id)
     * @param other version to compare with
     * @return whether is newer or not
     */
    fun isNewerThan(other: ServerVersion): Boolean = this.id > other.id

    fun isBetween(older: ServerVersion, newer: ServerVersion): Boolean {
        return this.id >= older.id && this.id <= newer.id
    }

    /**
     * Check if the version is older than another version by comparing their [id] (this.id < other.id)
     * @param other version to compare with
     * @return whether is older or not
     */
    fun isOlderThan(other: ServerVersion): Boolean = this.id < other.id

    companion object {

        private val NMS_VERSION: String = Bukkit.getServer().javaClass.getPackage().name.substringAfterLast('.')

        var CURRENT_VERSION: ServerVersion

        init {
            CURRENT_VERSION = getByBukkitName(Bukkit.getBukkitVersion())
        }

        /**
         * Shortcut for [CURRENT_VERSION.isAncient][ServerVersion.isAncient]
         */
        val isAncient: Boolean
            get() = CURRENT_VERSION.isAncient

        /**
         * Shortcut for [CURRENT_VERSION.isLegacy][ServerVersion.isLegacy]
         */
        val isLegacy: Boolean
            get() = CURRENT_VERSION.isLegacy

        /**
         * Shortcut for [CURRENT_VERSION.supportHex][ServerVersion.supportHex]
         */
        val supportHex: Boolean
            get() = CURRENT_VERSION.supportHex

        /**
         * Shortcut for [CURRENT_VERSION.isNewerThan][ServerVersion.isNewerThan]
         */
        fun isNewerThan(other: ServerVersion): Boolean = CURRENT_VERSION.isNewerThan(other)

        /**
         * Shortcut for [CURRENT_VERSION.isOlderThan][ServerVersion.isOlderThan]
         */
        fun isOlderThan(other: ServerVersion): Boolean = CURRENT_VERSION.isOlderThan(other)

        fun isBetween(older: ServerVersion, newer: ServerVersion): Boolean = CURRENT_VERSION.isBetween(older, newer)

        fun getById(id: Int): ServerVersion {
            return values().firstOrNull { it.nmsName != null && it.id == id } ?: UNKNOWN
        }

        fun getByNmsName(name: String): ServerVersion {
            return values().firstOrNull { name.equals(it.nmsName, true) } ?: UNKNOWN
        }

        fun getByBukkitName(name: String): ServerVersion {
            return values().firstOrNull { name.equals(it.bukkitName, true) } ?: UNKNOWN
        }

    }

}