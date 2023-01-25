package me.colton.duckprisons.enchants.pickaxe.leveltwo;

import me.colton.duckprisons.DuckPrisons;
import me.colton.duckprisons.PrisonPlayer;
import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchant;
import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Charity implements PickaxeEnchant {
    private final long bound = getEnchant().getMaxLevel()*50L;

    @Override
    public void use(@NotNull Player player, @NotNull Block block, @NotNull ItemStack pickaxe, long level) {
        // A chance of level/maxLevel*50
        // Max chance of 2%
        if (random.nextLong(0, bound) <= level) {

            long amount = random.nextLong(level*100, level*1000);

            for (Player p : Bukkit.getOnlinePlayers()) {
                long newBalance = PrisonPlayer.addBalance(p, amount);

                if (PrisonPlayer.getBooleanSetting(p, "alert.charity", true)) {
                    p.sendActionBar(Component.text(DuckPrisons.getInstance().getConfigOption("proc.charity",
                            Map.of("%amount%", String.valueOf(amount), "%newBalance%", String.valueOf(newBalance),
                                    "%player%", player.getName()))));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                }
            }
        }
    }

    @Override
    public @NotNull PickaxeEnchants getEnchant() {
        return PickaxeEnchants.CHARITY;
    }
}
