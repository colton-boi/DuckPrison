package colton.duckprisons.mines;

import org.bukkit.Material;

public enum MineBlocks {
    COBBLESTONE(Material.COBBLESTONE, Material.COBBLESTONE, 1),
    STONE(Material.STONE, Material.COBBLESTONE, 1),
    DEEPSLATE(Material.DEEPSLATE, Material.COBBLED_DEEPSLATE, 2),
    //COBBLESTONE(Material.COBBLESTONE, Material.COBBLESTONE, 1),
    //COBBLESTONE(Material.COBBLESTONE, Material.COBBLESTONE, 1),
    //COBBLESTONE(Material.COBBLESTONE, Material.COBBLESTONE, 1),
    ;

    MineBlocks(Material block, Material drops, long value) {

    }
}
