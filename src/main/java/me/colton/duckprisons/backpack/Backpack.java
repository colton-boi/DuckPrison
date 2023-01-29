package me.colton.duckprisons.backpack;

import me.colton.duckprisons.PrisonPlayer;
import me.colton.duckprisons.mines.MineBlock;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Backpack {

    /**
     * Add a certain amount of blocks of a type to a player's backpack
     * @param player    The player whose backpack is to be added to
     * @param block     The MineBlock to add to the backpack
     * @param count     The amount to add to the backpack
     */
    public static void addBlocks(@NotNull OfflinePlayer player, @NotNull MineBlock block, long count) {
        PrisonPlayer.getBackpack(player).addBlocks(block, count);
    }

    /**
     * Get a player's backpack
     * @param player    The player
     * @return          The player's backpack
     */
    public static Backpack get(@NotNull OfflinePlayer player) {
        return PrisonPlayer.getBackpack(player);
    }

    private final @NotNull PrisonPlayer player;
    private final @NotNull HashMap<MineBlock, Long> storedItems = new HashMap<>();

    public Backpack(@NotNull PrisonPlayer prisonPlayer) {
        this.player = prisonPlayer;
    }

    /**
     * Add 1 block of a type to the player's backpack
     * @param block     the MineBlock to add to
     */
    public void addBlocks(@NotNull MineBlock block) {
        addBlocks(block, 1);
    }

    /**
     * Add a certain amount of blocks of a type to the player's backpack
     * @param block     The MineBlock to add to
     * @param count     The amount to add
     */
    public void addBlocks(@NotNull MineBlock block, long count) {
        long amount = storedItems.getOrDefault(block, 0L);
        amount+=count;
        storedItems.replace(block, amount);
    }

    /**
     * Get the list of stored items
     * @return  The items stored in the player's backpack
     */
    public @NotNull HashMap<MineBlock, Long> getStoredItems() {
        return storedItems;
    }

    /**
     * Get the displayable list of materials
     * @return  The stored items with keys of Material instead of MineBlock for displaying
     */
    public @NotNull HashMap<Material, Long> getStoredMaterials() {
        HashMap<Material, Long> storedMaterials = new HashMap<>();
        storedItems.forEach((mineBlock, count) ->
                storedMaterials.put(mineBlock.getDropMaterial(), count));
        return storedMaterials;
    }

    /**
     * Get the PrisonPlayer that owns this backpack
     */
    public @NotNull PrisonPlayer getPlayer() {
        return player;
    }
}
