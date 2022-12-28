package colton.duckprisons.enchants.pickaxe.leveltwo;

import colton.duckprisons.DuckPrisons;
import colton.duckprisons.PrisonPlayer;
import colton.duckprisons.enchants.pickaxe.PickaxeEnchant;
import colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Charity implements PickaxeEnchant {
    private final long bound = getEnchant().getMaxLevel()*50L;

    @Override
    public boolean use(@NotNull BlockBreakEvent e, @NotNull ItemStack pickaxe, long level) {
        // A chance of level/maxLevel*50
        // Max chance of 2%
        if (random.nextLong(0, bound) <= level) {

            long amount = random.nextLong(level*100, level*1000);

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (PrisonPlayer.getBooleanSetting(p, "alert.charity")) {
                    p.sendActionBar(Component.text(DuckPrisons.getInstance().getConfigOption("proc.charity",
                            Map.of("%amount%", String.valueOf(amount)))));
                }

                PrisonPlayer.addBalance(p, amount);
            }
        }
        return true;
    }

    @Override
    public PickaxeEnchants getEnchant() {
        return PickaxeEnchants.CHARITY;
    }
}
