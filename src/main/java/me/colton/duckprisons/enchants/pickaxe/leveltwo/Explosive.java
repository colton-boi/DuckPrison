package me.colton.duckprisons.enchants.pickaxe.leveltwo;

import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchant;
import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import me.colton.duckprisons.enchants.pickaxe.levelone.Fortune;
import me.colton.duckprisons.util.BlockGetter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Explosive implements PickaxeEnchant {

    private final Vector smallVector = new Vector(1, 1, 1);
    private final Vector mediumVector = new Vector(2, 2, 2);
    private final Vector largeVector = new Vector(3, 3, 3);
    private final Vector massiveVector = new Vector(5, 5, 5);
    private final long bound = getEnchant().getMaxLevel()*100L;

    @Override
    public void use(@NotNull BlockBreakEvent e, @NotNull ItemStack pickaxe, long level) {
        Vector toCorner;

        // A chance of level/maxLevel*100
        // Max chance of 1%
        if (random.nextLong(0, bound) <= level) {
            if (level < 2500) {
                toCorner = smallVector;
            } else if (level < 5000) {
                toCorner = mediumVector;
            } else if (level < 10000) {
                toCorner = largeVector;
            } else {
                toCorner = massiveVector;
            }

            Location upperCorner = e.getBlock().getLocation().add(toCorner);
            Location lowerCorner = e.getBlock().getLocation().subtract(toCorner);
            long fortuneLevel = PickaxeEnchants.FORTUNE.getLevel(pickaxe);

            for (Block block : BlockGetter.getBlocksBetween(lowerCorner, upperCorner)) {
                Fortune.use(block, e.getPlayer(), fortuneLevel);
            }
            System.out.println();
        }
    }

    @Override
    public @NotNull PickaxeEnchants getEnchant() {
        return PickaxeEnchants.EFFICIENCY;
    }
}
