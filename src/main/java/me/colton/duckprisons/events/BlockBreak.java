package me.colton.duckprisons.events;

import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
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
