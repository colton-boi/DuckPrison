package colton.duckprisons.events;

import colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class BlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(@NotNull BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }

        if (e.getPlayer().getInventory().getItemInMainHand().getType().toString().contains("PICKAXE")) {
            // Pickaxe Enchants
            for (PickaxeEnchants enchant : PickaxeEnchants.values()) {
                enchant.use(e);
            }
        } else if (e.getPlayer().getInventory().getItemInMainHand().getType().toString().contains("HOE")) {
            // Hoe enchants
        }
    }
}
