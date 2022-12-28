package colton.duckprisons.util;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockGetter {
    public static List<Block> getBlocksBetween(Location loc1, Location loc2) {
        List<Block> blocks = new ArrayList<>();

        double lowerX = Math.min(loc1.getX(), loc2.getX());
        double higherX = Math.max(loc1.getX(), loc2.getX());

        double lowerY = Math.min(loc1.getY(), loc2.getY());
        double higherY = Math.max(loc1.getY(), loc2.getY());

        double lowerZ = Math.min(loc1.getZ(), loc2.getZ());
        double higherZ = Math.max(loc1.getZ(), loc2.getZ());

        for (double x = lowerX; x < higherX; x++) {
            for (double y = lowerY; y < higherY; y++) {
                for (double z = lowerZ; z < higherZ; z++) {
                    blocks.add(loc1.getWorld().getBlockAt(new Location(loc1.getWorld(), x, y, z)));
                }
            }
        }

        return blocks;
    }
}
