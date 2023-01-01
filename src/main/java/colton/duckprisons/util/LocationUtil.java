package colton.duckprisons.util;

import org.bukkit.Location;

public class LocationUtil {
    public static boolean isBetween(Location location, Location corner1, Location corner2) {
        if (Math.min(corner1.getX(), corner2.getX()) <= location.getX() &&
                Math.max(corner1.getX(), corner2.getX()) >= location.getX()) {
            if (Math.min(corner1.getY(), corner2.getY()) <= location.getY() &&
                    Math.max(corner1.getY(), corner2.getY()) >= location.getY()) {
                return Math.min(corner1.getZ(), corner2.getZ()) <= location.getZ() &&
                        Math.max(corner1.getZ(), corner2.getZ()) >= location.getZ();
            }
        }
        return false;
    }
}
