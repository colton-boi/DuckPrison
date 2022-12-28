package colton.duckprisons.enchants.pickaxe.levelone;

import colton.duckprisons.DuckPrisons;
import colton.duckprisons.PrisonPlayer;
import colton.duckprisons.enchants.pickaxe.PickaxeEnchant;
import colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import net.kyori.adventure.text.Component;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Salary implements PickaxeEnchant {
    private final long bound = getEnchant().getMaxLevel()*10L;

    @Override
    public boolean use(@NotNull BlockBreakEvent e, @NotNull ItemStack pickaxe, long level) {
        // A chance of level/maxLevel*10
        // Max chance of 10%
        if (random.nextLong(0, bound) <= level) {

            long amount = random.nextLong(level*100, level*1000);

            if (PrisonPlayer.getBooleanSetting(e.getPlayer(), "alert.salary")) {
                e.getPlayer().sendActionBar(Component.text(DuckPrisons.getInstance().getConfigOption("proc.salary",
                        Map.of("%amount%", String.valueOf(amount)))));
            }

            PrisonPlayer.addBalance(e.getPlayer(), amount);
        }

        return true;
    }

    @Override
    public PickaxeEnchants getEnchant() {
        return PickaxeEnchants.SALARY;
    }

}