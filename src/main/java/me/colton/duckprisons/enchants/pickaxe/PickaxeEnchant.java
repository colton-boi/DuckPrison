package me.colton.duckprisons.enchants.pickaxe;

import me.colton.duckprisons.PrisonPlayer;
import me.colton.duckprisons.enchants.Enchant;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public interface PickaxeEnchant extends Enchant {
    Random random = new Random();

    @Override
    default boolean use(@NotNull Event e) {
        if (e instanceof BlockBreakEvent event) {
            ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
            long level = PrisonPlayer.getPlayer(event.getPlayer()).getLevel(getEnchant());
            if (level == 0) {
                return false;
            }
            if (!itemStack.getType().toString().contains("PICKAXE")) {
                return false;
            }
            use(event, itemStack, level);
            return true;
        }
        return false;
    }

    /**
     * The use method specifically for pickaxes
     *
     * @param e       The BlockBreakEvent triggering the enchant
     * @param pickaxe The player's tool.
     * @param level   The level of the enchant on the player's tool. Greater than 0
     */
    void use(@NotNull BlockBreakEvent e,
             @NotNull ItemStack pickaxe,
             long level);

    @NotNull PickaxeEnchants getEnchant();
}
