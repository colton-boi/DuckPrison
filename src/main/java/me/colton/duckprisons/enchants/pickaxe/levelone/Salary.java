package me.colton.duckprisons.enchants.pickaxe.levelone;

import me.colton.duckprisons.DuckPrisons;
import me.colton.duckprisons.PrisonPlayer;
import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchant;
import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Salary implements PickaxeEnchant {
    private final long bound = getEnchant().getMaxLevel()*10L;

    @Override
    public void use(@NotNull Player player, @NotNull Block block, @NotNull ItemStack pickaxe, long level) {
        // A chance of level/maxLevel*10
        // Max chance of 10%
        if (random.nextLong(0, bound) <= level) {

            long amount = random.nextLong(level*100, level*1000);
            long newBalance = PrisonPlayer.addBalance(player, amount);

            if (PrisonPlayer.getBooleanSetting(player, "alert.salary", true)) {
                player.sendActionBar(Component.text(DuckPrisons.getInstance().getConfigOption("proc.salary",
                        Map.of("%amount%", String.valueOf(amount), "%newBalance%", String.valueOf(newBalance)))));
            }
        }
    }

    @Override
    public @NotNull PickaxeEnchants getEnchant() {
        return PickaxeEnchants.SALARY;
    }

}