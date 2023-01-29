package me.colton.duckprisons.enchants.pickaxe.levelone;

import me.colton.duckprisons.backpack.Backpack;
import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchant;
import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import me.colton.duckprisons.mines.MineBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Fortune implements PickaxeEnchant {
    @Override
    public void use(@NotNull Player player, @NotNull Block block, @NotNull ItemStack pickaxe, long level) {

        Backpack.addBlocks(player, MineBlock.getFrom(block.getType()), Math.toIntExact(level));
    }

    @Override
    public @NotNull PickaxeEnchants getEnchant() {
        return PickaxeEnchants.FORTUNE;
    }
}