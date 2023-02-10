package me.colton.duckprisons.util;

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

    public static Location minimum(@NotNull Location location, @NotNull Location location2) {

        Location cloned = location.clone();

        cloned.setX(Math.min(location.x(), location2.x()));
        cloned.setY(Math.min(location.y(), location2.y()));
        cloned.setZ(Math.min(location.z(), location2.z()));

        return cloned;
    }

    public static Location maximum(@NotNull Location location, @NotNull Location location2) {

        Location cloned = location.clone();

        cloned.setX(Math.max(location.x(), location2.x()));
        cloned.setY(Math.max(location.y(), location2.y()));
        cloned.setZ(Math.max(location.z(), location2.z()));

        return cloned;
    }
}
