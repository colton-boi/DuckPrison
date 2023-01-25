package me.colton.duckprisons.enchants.pickaxe.levelone;

import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchant;
import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class Haste implements PickaxeEnchant {
    @Override
    public void use(@NotNull Player player, @NotNull Block block, @NotNull ItemStack pickaxe, long level) {
        if (!player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100,
                    (int) level, false, false, false));
        }
    }

    @Override
    public @NotNull PickaxeEnchants getEnchant() {
        return PickaxeEnchants.HASTE;
    }
}