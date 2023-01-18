package me.colton.duckprisons.mines;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public enum MineBlocks {
    COBBLESTONE(Material.COBBLESTONE, Material.COBBLESTONE, 1),
    STONE(Material.STONE, Material.COBBLESTONE, 1),
    DEEPSLATE(Material.DEEPSLATE, Material.COBBLED_DEEPSLATE, 2),
    //COBBLESTONE(Material.COBBLESTONE, Material.COBBLESTONE, 1),
    //COBBLESTONE(Material.COBBLESTONE, Material.COBBLESTONE, 1),
    //COBBLESTONE(Material.COBBLESTONE, Material.COBBLESTONE, 1),
    ;

    private final @NotNull Material block;
    private final @NotNull Material drop;
    private final long value;

    MineBlocks(@NotNull Material block, @NotNull Material drop, long value) {
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
