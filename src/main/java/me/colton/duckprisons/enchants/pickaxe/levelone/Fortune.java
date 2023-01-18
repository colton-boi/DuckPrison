package me.colton.duckprisons.enchants.pickaxe.levelone;

import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchant;
import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Fortune implements PickaxeEnchant {
    @Override
    public void use(@NotNull BlockBreakEvent e, @NotNull ItemStack pickaxe, long level) {
        e.setDropItems(false);

        use(e.getBlock(), e.getPlayer(), level);
    }

    public static void use(@NotNull Block block, @NotNull Player player, long level) {

        //addItems(player, new ItemStack(block.getType(), Math.toIntExact(level)));
    }

    @Override
    public @NotNull PickaxeEnchants getEnchant() {
        return PickaxeEnchants.FORTUNE;
    }
}