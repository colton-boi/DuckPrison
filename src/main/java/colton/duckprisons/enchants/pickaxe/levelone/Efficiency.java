package colton.duckprisons.enchants.pickaxe.levelone;


import colton.duckprisons.enchants.pickaxe.PickaxeEnchant;
import colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Efficiency implements PickaxeEnchant {
    @Override
    public void use(@NotNull BlockBreakEvent e, @NotNull ItemStack pickaxe, long level) {
        // Uses the vanilla enchant, nothing goes here
    }

    @Override
    public PickaxeEnchants getEnchant() {
        return PickaxeEnchants.EFFICIENCY;
    }
}
