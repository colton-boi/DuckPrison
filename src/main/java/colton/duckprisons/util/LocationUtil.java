package colton.duckprisons.util;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class LocationUtil {
    public static boolean isBetween(@NotNull Location location, @NotNull Location corner1, @NotNull Location corner2) {
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
