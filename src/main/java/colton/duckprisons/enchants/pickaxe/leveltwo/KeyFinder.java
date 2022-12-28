package colton.duckprisons.enchants.pickaxe.leveltwo;

import colton.duckprisons.enchants.pickaxe.PickaxeEnchant;
import colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class KeyFinder implements PickaxeEnchant {
    @Override
    public boolean use(@NotNull BlockBreakEvent e, @NotNull ItemStack pickaxe, long level) {
        return true;
    }

    @Override
    public PickaxeEnchants getEnchant() {
        return PickaxeEnchants.EFFICIENCY;
    }
}
