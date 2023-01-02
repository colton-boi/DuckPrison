package colton.duckprisons.backpack;

import colton.duckprisons.PrisonPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Backpack {

    public static void addItems(@NotNull Player p, @NotNull ItemStack items) {
        PrisonPlayer.getBackpack(p).addItems(items);
    }

    private final @NotNull PrisonPlayer player;
    private final @NotNull HashMap<Material, Long> storedItems = new HashMap<>();

    public Backpack(@NotNull PrisonPlayer p) {
        this.player = p;
    }

    public void addItems(@NotNull ItemStack items) {
        Long amount = storedItems.getOrDefault(items.getType(), 0L);
        amount+=items.getAmount();
        storedItems.replace(items.getType(), amount);
    }

    public @NotNull HashMap<Material, Long> getStoredItems() {
        return storedItems;
    }

    public @NotNull PrisonPlayer getPlayer() {
        return player;
    }
}
