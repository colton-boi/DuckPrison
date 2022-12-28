package colton.duckprisons.enchants.pickaxe.levelone;

import colton.duckprisons.enchants.pickaxe.PickaxeEnchant;
import colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class Speed implements PickaxeEnchant {
    @Override
    public boolean use(@NotNull BlockBreakEvent e, @NotNull ItemStack pickaxe, long level) {
        if (!e.getPlayer().hasPotionEffect(PotionEffectType.SPEED)) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100,
                    (int) level, false, false, false));
        }
        return true;
    }

    @Override
    public PickaxeEnchants getEnchant() {
        return PickaxeEnchants.SPEED;
    }

}