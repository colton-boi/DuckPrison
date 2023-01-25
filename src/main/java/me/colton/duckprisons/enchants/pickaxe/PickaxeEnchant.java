package me.colton.duckprisons.enchants.pickaxe;

import me.colton.duckprisons.PrisonPlayer;
import me.colton.duckprisons.enchants.Enchant;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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
            ItemStack pickaxe = event.getPlayer().getInventory().getItemInMainHand();
            long level = PrisonPlayer.getPlayer(event.getPlayer()).getLevel(getEnchant());
            if (level == 0) {
                return false;
            }
            if (!pickaxe.getType().toString().contains("PICKAXE")) {
                return false;
            }
            use(event, pickaxe, level);
            return true;
        }
        return false;
    }

    /**
     * Use method specifically for pickaxes
     *
     * @param event   The BlockBreakEvent triggering the enchant
     * @param pickaxe The player's tool.
     * @param level   The level of the enchant on the player's tool. Greater than 0
     */
    default void use(@NotNull BlockBreakEvent event, @NotNull ItemStack pickaxe, long level) {
        event.setDropItems(false);
        use(event.getPlayer(), event.getBlock(), pickaxe, level);
    }

    /**
     * Use method for fake block mining (Ex: Explosive)
     *
     * @param player    The player
     * @param block     The block "mined"
     */
    default void use(@NotNull Player player, @NotNull Block block) {
        ItemStack pickaxe = player.getInventory().getItemInMainHand();
        long level = PrisonPlayer.getPlayer(player).getLevel(getEnchant());
        if (level > 0 && pickaxe.getType().toString().contains("PICKAXE")) {
            use(player, block, pickaxe, level);
        }
    }

    /**
     * Use method for pickaxes that doesn't require an event.
     *
     * @param player    The player "mining" the block
     * @param block     The block that has been "mined"
     * @param pickaxe   The player's tool
     * @param level     The level of the enchant on the player's tool. Greater than 0
     */
    void use(@NotNull Player player, @NotNull Block block,
             @NotNull ItemStack pickaxe, long level);

    @NotNull PickaxeEnchants getEnchant();
}
