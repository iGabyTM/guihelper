package me.gabytm.guihelper.generators.types;

import java.util.Set;
import java.util.stream.Collectors;
import me.gabytm.guihelper.GUIHelper;
import me.gabytm.guihelper.data.Config;
import me.gabytm.guihelper.generators.generators.IGeneratorSlot;
import me.gabytm.guihelper.utils.ItemUtil;
import me.gabytm.guihelper.utils.Message;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class BetterGUI implements IGeneratorSlot {
    private final GUIHelper plugin;

    public BetterGUI(final GUIHelper plugin) {
        this.plugin = plugin;
    }

    @Override
    public void generate(final Inventory gui, final Player player) {
        final long start = System.currentTimeMillis();
        final Config config = new Config("BetterGUI/menu", plugin);

        config.empty();

        for (int slot = 0; slot < gui.getSize(); slot++) {
            final ItemStack item = gui.getItem(slot);

            if (ItemUtil.isNull(item)) continue;

            final String path = "item-" + slot;

            addItem(config.get().createSection(path), item, slot);
        }

        config.save();
        Message.CREATION_DONE.format(System.currentTimeMillis() - start).send(player);
    }

    @Override
    public void addItem(final ConfigurationSection section, final ItemStack item, final int slot) {
        final ItemMeta meta = item.getItemMeta();

        section.set("id", item.getType().toString());

        if (item.getDurability() > 0) {
            section.set("damage", item.getDurability());
        }

        if (item.getAmount() > 1) {
            section.set("amount", item.getAmount());
        }

        section.set("slot", slot);

        if (meta.hasDisplayName()) {
            section.set("name", ItemUtil.getDisplayName(meta));
        }

        if (meta.hasLore()) {
            section.set("lore", ItemUtil.getLore(meta));
        }

        if (meta.hasEnchants()) {
            section.set("enchant", meta.getEnchants().entrySet().stream()
                .map(entry -> entry.getKey().getName() + "," + entry.getValue())
                .collect(Collectors.toList()));
        }

        Set<ItemFlag> itemFlags = meta.getItemFlags();
        if (!itemFlags.isEmpty()) {
            section.set("flag", itemFlags.stream().map(Enum::name).collect(Collectors.toList()));
        }
    }
}
