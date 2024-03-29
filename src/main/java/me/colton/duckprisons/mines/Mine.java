package me.colton.duckprisons.mines;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockType;
import me.colton.duckprisons.PrisonPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import static me.colton.duckprisons.util.LocationUtil.isBetween;

public interface Mine {

    @NotNull Random random = new Random();

    default @NotNull Location getUpperCorner() {
        if (this instanceof PrivateMine mine) {
            return mine.getUpperCorner();
        } else if (this instanceof PublicMines mine) {
            return mine.getUpperCorner();
        }
        throw new RuntimeException("No upperCorner found for mine " + this);
    }

    default @NotNull Location getBottomCorner() {
        if (this instanceof PrivateMine mine) {
            return mine.getBottomCorner();
        } else if (this instanceof PublicMines mine) {
            return mine.getBottomCorner();
        }
        throw new RuntimeException("No bottomCorner found for mine " + this);
    }

    static @Nullable Mine getMineAt(@NotNull Location location) {
        for (PublicMines mine : PublicMines.values()) {
            if (isBetween(location, mine.topCorner, mine.bottomCorner)) {
                return mine;
            }
        }
        for (PrivateMine mine : PrivateMine.getPrivateMines().values()) {
            if (isBetween(location, mine.topCorner, mine.bottomCorner)) {
                return mine;
            }
        }

        return null;
    }

    static boolean canMine(@NotNull Block block, @NotNull Player player) {
        if (player.isOp()) {
            if (!PrisonPlayer.getBooleanSetting(player, "test.mining", true)) {
                return true;
            }
        }

        Mine mine = getMineAt(block.getLocation());

        if (mine == null) {
            return false;
        }

        if (mine instanceof PrivateMine privateMine) {
            return privateMine.isMember(player);
        } else {
            return PrisonPlayer.isMineUnlocked(player, (PublicMines) mine);
        }
    }

    default void reset(@NotNull Location topCorner, @NotNull Location bottomCorner, @NotNull List<Material> blockTypes) {
        BukkitWorld world = new BukkitWorld(topCorner.getWorld());
        BlockVector3 topVector = BlockVector3.at(topCorner.getX(), topCorner.getY(), topCorner.getZ());
        BlockVector3 bottomVector = BlockVector3.at(bottomCorner.getX(), bottomCorner.getY(), bottomCorner.getZ());
        CuboidRegion region = new CuboidRegion(world, topVector, bottomVector);

        EditSession session = WorldEdit.getInstance().newEditSession(world);
        RandomPattern pattern = new RandomPattern();
        blockTypes.forEach(material -> pattern.add(new BaseBlock(Objects.requireNonNull(BlockType.REGISTRY
                .get(material.toString().toLowerCase())).getDefaultState()), 1));

        session.setBlocks((Region) region, pattern);
        session.flushQueue();
    }
}
