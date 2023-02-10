package me.colton.duckprisons.enchants.pickaxe.leveltwo;

import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchant;
import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import me.colton.duckprisons.mines.Mine;
import me.colton.duckprisons.util.BlockGetter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Jackhammer implements PickaxeEnchant {

    private Long bound;

    @Override
    public void use(@NotNull Player player, @NotNull Block block, @NotNull ItemStack pickaxe, long level) {

        if (bound == null) {
            bound = getEnchant().getMaxLevel()*100L;
        }

        // A chance of level/maxLevel*100
        // Max chance of 1%
        if (random.nextLong(0, bound) <= level) {

            Mine mine = Mine.getMineAt(block.getLocation());

            if (mine == null) {
                throw new RuntimeException("Found no mine at jackhammer enchant trigger. Block at " + block.getLocation());
            }

            Location upperCorner = mine.getUpperCorner().clone();
            upperCorner.setY(block.getY());
            Location bottomCorner = mine.getBottomCorner().clone();
            bottomCorner.setY(block.getY());

            for (Block loopedBlock : BlockGetter.getBlocksBetween(bottomCorner, upperCorner)) {
                for (PickaxeEnchants enchant : PickaxeEnchants.values()) {
                    if (!enchant.isDangerous()) {
                        enchant.use(player, loopedBlock);
                    }
                }
                loopedBlock.setType(Material.AIR);
            }
        }

    }

    @Override
    public @NotNull PickaxeEnchants getEnchant() {
        return PickaxeEnchants.JACKHAMMER;
    }
}
