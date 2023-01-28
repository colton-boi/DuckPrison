package me.colton.duckprisons.enchants.pickaxe.leveltwo;

import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchant;
import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import me.colton.duckprisons.mines.Mine;
import me.colton.duckprisons.util.BlockGetter;
import me.colton.duckprisons.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Explosive implements PickaxeEnchant {

    private final Vector smallVector = new Vector(1, 1, 1);
    private final Vector mediumVector = new Vector(2, 2, 2);
    private final Vector largeVector = new Vector(3, 3, 3);
    private final Vector massiveVector = new Vector(5, 5, 5);
    private Long bound;

    @Override
    public void use(@NotNull Player player, @NotNull Block block, @NotNull ItemStack pickaxe, long level) {

        Vector toCorner;
        if (bound == null) {
            bound = getEnchant().getMaxLevel()*100L;
        }

        // A chance of level/maxLevel*100
        // Max chance of 1%
        if (random.nextLong(0, bound) <= level) {

            Mine mine = Mine.getMineAt(block.getLocation());

            if (mine == null) {
                throw new RuntimeException("Found no mine at explosive enchant trigger. Block at " + block.getLocation());
            }

            if (level < 2500) {
                toCorner = smallVector;
            } else if (level < 5000) {
                toCorner = mediumVector;
            } else if (level < 10000) {
                toCorner = largeVector;
            } else {
                toCorner = massiveVector;
            }

            Location upperCorner = block.getLocation().add(toCorner);
            Location bottomCorner = block.getLocation().subtract(toCorner);

            // Make sure it doesn't go out of the mine area.
            upperCorner = LocationUtil.minimum(upperCorner, mine.getUpperCorner());
            bottomCorner = LocationUtil.maximum(bottomCorner, mine.getBottomCorner());

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
        return PickaxeEnchants.EXPLOSIVE;
    }
}
