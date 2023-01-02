package colton.duckprisons.enchants.pickaxe.leveltwo;

import colton.duckprisons.enchants.pickaxe.PickaxeEnchant;
import colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LevelFinder implements PickaxeEnchant {
    @Override
    public void use(@NotNull BlockBreakEvent e, @NotNull ItemStack pickaxe, long level) {
    }

    @Override
    public @NotNull PickaxeEnchants getEnchant() {
        return PickaxeEnchants.EFFICIENCY;
    }
}
