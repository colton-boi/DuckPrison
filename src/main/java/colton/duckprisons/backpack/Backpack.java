package colton.duckprisons.backpack;

import colton.duckprisons.PrisonPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Backpack {

    public static void addItems(Player p, ItemStack items) {
        PrisonPlayer.getBackpack(p).addItems(items);
    }

    private final PrisonPlayer player;
    private final HashMap<Material, Long> storedItems;

    public Backpack(PrisonPlayer p) {
        this.player = p;
        this.storedItems = new HashMap<>();
    }

    public void addItems(ItemStack items) {
        Long amount = storedItems.getOrDefault(items.getType(), 0L);
        amount+=items.getAmount();
        storedItems.replace(items.getType(), amount);
    }

    public PrisonPlayer getPlayer() {
        return player;
    }
}
