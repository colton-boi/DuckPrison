package me.colton.duckprisons.mines;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public enum MineBlock {
    COBBLESTONE(Material.COBBLESTONE, Material.COBBLESTONE, 1),
    STONE(Material.STONE, Material.COBBLESTONE, 1),
    DEEPSLATE(Material.DEEPSLATE, Material.COBBLED_DEEPSLATE, 2),
    //COBBLESTONE(Material.COBBLESTONE, Material.COBBLESTONE, 1),
    //COBBLESTONE(Material.COBBLESTONE, Material.COBBLESTONE, 1),
    //COBBLESTONE(Material.COBBLESTONE, Material.COBBLESTONE, 1),
    ;

    public static MineBlock getFrom(Material material) {
        Optional<MineBlock> mineBlock = Arrays.stream(MineBlock.values()).filter(block ->
                block.getBlock() == material).findFirst();
        return mineBlock.orElse(null);
    }

    private final @NotNull Material block;
    private final @NotNull Material drop;
    private final long value;

    MineBlock(@NotNull Material block, @NotNull Material drop, long value) {
        this.block = block;
        this.drop = drop;
        this.value = value;
    }

    public @NotNull Material getBlock() {
        return block;
    }

    public @NotNull Material getDropMaterial() {
        return drop;
    }

    public long getValue() {
        return value;
    }
}
