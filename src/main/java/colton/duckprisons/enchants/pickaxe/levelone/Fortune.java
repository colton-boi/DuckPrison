package colton.duckprisons.enchants.pickaxe.levelone;

import colton.duckprisons.enchants.pickaxe.PickaxeEnchant;
import colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Fortune implements PickaxeEnchant {
    @Override
    public boolean use(@NotNull BlockBreakEvent e, @NotNull ItemStack pickaxe, long level) {
        e.setDropItems(false);

        //addItems(e.getPlayer(), new ItemStack(e.getBlock().getType(), Math.toIntExact(level)));
        return true;
    }

    public static boolean fakeUse(@NotNull Block block, @NotNull Player player, long level) {

        //addItems(player, new ItemStack(block.getType(), Math.toIntExact(level)));
        return true;
    }

    @Override
    public PickaxeEnchants getEnchant() {
        return PickaxeEnchants.FORTUNE;
    }
}