package colton.duckprisons.backpack;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Backpack {
    public static void addItems(Player p, ItemStack items) {

    }

    public static void addItems(Player p, List<ItemStack> items) {
        items.forEach(itemStack -> addItems(p, itemStack));
    }
}
