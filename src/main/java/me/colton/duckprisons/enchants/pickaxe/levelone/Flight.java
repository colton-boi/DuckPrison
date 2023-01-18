package me.colton.duckprisons.enchants.pickaxe.levelone;

import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchant;
import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Flight implements PickaxeEnchant {
    @Override
    public void use(@NotNull BlockBreakEvent e, @NotNull ItemStack pickaxe, long level) {

        if (level > 0) {
            if (!e.getPlayer().getAllowFlight()) {
                e.getPlayer().setAllowFlight(true);
            }
        }
    }

    @Override
    public @NotNull PickaxeEnchants getEnchant() {
        return PickaxeEnchants.FLIGHT;
    }
}

