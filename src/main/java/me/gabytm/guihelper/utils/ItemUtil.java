/*
 * Copyright 2019 GabyTM
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

package me.gabytm.guihelper.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class ItemUtil {
    public static boolean slotIsEmpty(ItemStack item) {
        return item == null || item.getType().equals(Material.AIR);
    }

    public static boolean isLeatherArmor(ItemStack item) {
        return item.getType().toString().contains("LEATHER_");
    }

    public static boolean isMonsterEgg(ItemStack item) {
        return item.getType().toString().contains("MONSTER_EGG");
    }

    public static String getDisplayName(ItemMeta meta) {
        return meta.getDisplayName().replaceAll("§", "&");
    }

    public static List<String> getLore(ItemMeta meta) {
        List<String> lore = meta.getLore();

        Collections.replaceAll(lore, "§", "&");
        return lore;
    }
}