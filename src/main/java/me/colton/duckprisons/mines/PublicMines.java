package me.colton.duckprisons.mines;

import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.bukkit.Bukkit.getWorld;

public enum PublicMines implements Mine {
    A(new Location(getWorld("mines"), 0, 0, 0), 50, 50,
            List.of(Material.COBBLESTONE, Material.STONE, Material.DEEPSLATE)),
    B(new Location(getWorld("mines"), 250, 0, 0), 50, 50,
            List.of(Material.STONE, Material.DEEPSLATE, Material.COBBLED_DEEPSLATE)),
    C(new Location(getWorld("mines"), 500, 0, 0), 50, 50,
            List.of(Material.DEEPSLATE, Material.COBBLED_DEEPSLATE, Material.BLACKSTONE)),



    P1(new Location(getWorld("mines"), 750, 0, 0), 50, 50,
            List.of(Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE, Material.DIAMOND_BLOCK)),
    P5(new Location(getWorld("mines"), 750, 0, 0), 50, 50,
            List.of(Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE, Material.EMERALD_BLOCK)),
    P10(new Location(getWorld("mines"), 750, 0, 0), 50, 50,
            List.of(Material.ANCIENT_DEBRIS, Material.NETHERITE_BLOCK)),
    P25(new Location(getWorld("mines"), 750, 0, 0), 50, 50,
            List.of(Material.NETHERITE_BLOCK, Material.OBSIDIAN, Material.CRYING_OBSIDIAN)),
    ;

    public final Location topCorner;
    public final Location bottomCorner;
    public final List<Material> blockTypes;

    PublicMines(@NotNull Location bottomCenter, long apothem, long height, @NotNull List<Material> blocks) {
        topCorner = bottomCenter.clone().add(apothem, height, apothem);
        bottomCorner = bottomCenter.clone().subtract(apothem, 0, apothem);
        blockTypes = blocks;
    }

    public void reset() {
        reset(topCorner, bottomCorner, blockTypes);
    }

}
