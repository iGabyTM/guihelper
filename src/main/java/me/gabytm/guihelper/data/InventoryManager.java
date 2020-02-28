/*
 * Copyright 2020 GabyTM
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

package me.gabytm.guihelper.data;

import me.gabytm.guihelper.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class InventoryManager implements InventoryHolder {
    private final Map<UUID, Inventory> inventories = new HashMap<>();

    public boolean exists(final UUID uuid) {
        return inventories.get(uuid) != null;
    }

    public void add(final UUID uuid, final Inventory inventory) {
        inventories.put(uuid, inventory);
    }

    public void remove(final UUID uuid) {
        inventories.remove(uuid);
    }

    public Inventory get(final UUID uuid) {
        final Inventory defaultInventory = Bukkit.createInventory(this, 54, "GUIHelper");
        inventories.putIfAbsent(uuid, defaultInventory);
        return inventories.get(uuid);
    }

    public boolean isEmpty(final Inventory inventory) {
        return Arrays.stream(inventory.getContents()).allMatch(ItemUtil::isNull);
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
