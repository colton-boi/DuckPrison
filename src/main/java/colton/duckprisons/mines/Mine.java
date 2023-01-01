package colton.duckprisons.mines;

import colton.duckprisons.PrisonPlayer;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import static colton.duckprisons.util.LocationUtil.isBetween;

public interface Mine {
    Random random = new Random();

    static @Nullable Mine getMineAt(Location location) {
        for (PublicMines mine : PublicMines.values()) {
            if (isBetween(location, mine.topCorner, mine.bottomCorner)) {
                return mine;
            }
        }


        return null;
    }

    static boolean canMine(Block block, Player player) {
        Mine mine = getMineAt(block.getLocation());
        if (player.isOp()) {
            if (!PrisonPlayer.getBooleanSetting(player, "test.mining", true)) {
                return true;
            }
        }

        if (mine == null) {
            return false;
        }

        if (mine instanceof PrivateMines privateMine) {
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
