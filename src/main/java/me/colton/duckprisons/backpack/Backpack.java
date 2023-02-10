package me.colton.duckprisons.backpack;

import me.colton.duckprisons.DuckPrisons;
import me.colton.duckprisons.PrisonPlayer;
import me.colton.duckprisons.mines.MineBlock;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Backpack {

    /**
     * Add a certain amount of blocks of a type to a player's backpack
     * @param player    the player whose backpack is to be added to
     * @param block     the MineBlock to add to the backpack
     * @param count     the amount to add to the backpack
     */
    public static void addBlocks(@NotNull OfflinePlayer player, @NotNull MineBlock block, long count) {
        PrisonPlayer.getBackpack(player).addBlocks(block, count);
    }

    /**
     * Remove a certain amount of blocks of a type from a player's backpack
     * @param player    the player whose backpack is to be removed from
     * @param block     the MineBlock to remove from the backpack
     * @param count     the amount to remove from the backpack
     */
    public static void removeBlocks(@NotNull OfflinePlayer player, @NotNull MineBlock block, long count) {
        get(player).removeBlocks(block, count);
    }

    /**
     * Sell the blocks in the backpack of a player
     * @param player    the player who owns the backpack
     * @return          the amount of money made
     */
    public static long sell(@NotNull OfflinePlayer player) {
        return get(player).sell();
    }

    /**
     * Get a player's backpack
     * @param player    the player
     * @return          the player's backpack
     */
    public static Backpack get(@NotNull OfflinePlayer player) {
        return PrisonPlayer.getBackpack(player);
    }

    private final @NotNull PrisonPlayer player;
    public long maxStored;

    private final @NotNull HashMap<MineBlock, Long> storedItems = new HashMap<>();

    public Backpack(@NotNull PrisonPlayer player, long maxStored) {
        this.player = player;
        this.maxStored = maxStored;
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
     * @param block     the MineBlock to add to
     * @param count     the amount to add
     */
    public void addBlocks(@NotNull MineBlock block, long count) {
        if (count < 0) {
            removeBlocks(block, -count);
            return;
        }
        long amount = storedItems.getOrDefault(block, 0L);
        storedItems.replace(block, (amount + Math.min(count, canHold())));
    }

    /**
     * Remove 1 block of a type to the player's backpack
     * @param block     the MineBlock to remove 1 from
     */
    public void removeBlocks(@NotNull MineBlock block) {
        removeBlocks(block, 1);
    }

    /**
     * Remove a certain amount of blocks of a type from the player's backpack
     * @param block     the MineBlock to remove from
     * @param count     the amount to remove
     */
    public void removeBlocks(@NotNull MineBlock block, long count) {
        if (count < 0) {
            addBlocks(block, -count);
            return;
        }
        long amount = storedItems.getOrDefault(block, 0L);
        storedItems.replace(block, (amount - Math.min(count, amount)));
    }

    /**
     * Get the amount of items the backpack can hold
     * @return  the amount of items
     */
    public long canHold() {
        return maxStored-getAmountStored();
    }

    /**
     * Check if the backpack can hold a specified amount of items
     * @param count the amount of items
     * @return      if the specified amount of items can be held
     */
    public boolean canHold(long count) {
        return canHold() >= count;
    }

    /**
     * Sell the blocks in the backpack
     * @return  the amount of money gained
     */
    public long sell() {
        long items = getAmountStored();
        AtomicLong amount = new AtomicLong();
        storedItems.forEach((mineBlock, count) -> {
            amount.addAndGet((mineBlock.getValue() * count));
            storedItems.replace(mineBlock, count, 0L);
        });
        player.addBalance(amount.get()*player.getMultiplier());
        player.getPlayer().sendMessage(DuckPrisons.getInstance().getConfigOption("backpack.sell",
                Map.of("%newBalance%", String.valueOf(player.getBalance()),
                        "%items%", Long.toString(items),
                        "%value%", String.valueOf(amount.get()/player.getMultiplier()),
                        "%gained%", amount.toString(),
                        "%multiplier%", String.valueOf(player.getMultiplier()))));
        return amount.get();
    }

    /**
     * Get the list of stored items
     * @return  the items stored in the player's backpack
     */
    public @NotNull HashMap<MineBlock, Long> getStoredItems() {
        return storedItems;
    }

    /**
     * Get the displayable list of materials
     * @return  the stored items with keys of Material instead of MineBlock for displaying (ex: Lore)
     */
    public @NotNull HashMap<Material, Long> getStoredMaterials() {
        HashMap<Material, Long> storedMaterials = new HashMap<>();
        storedItems.forEach((mineBlock, count) ->
                storedMaterials.put(mineBlock.getDropMaterial(), count));
        return storedMaterials;
    }

    /**
     * Get the amount of items stored in the backpack
     * @return  the amount of stored items
     */
    public long getAmountStored() {
        AtomicLong amount = new AtomicLong();
        storedItems.values().forEach(amount::addAndGet);
        return amount.get();
    }

    /**
     * Get the PrisonPlayer that owns this backpack
     * @return  the PrisonPlayer
     */
    public @NotNull PrisonPlayer getPlayer() {
        return player;
    }

    /**
     * Get a lore that displays important information on the backpack
     * @return  a list of components with information
     */
    public @NotNull List<Component> getLore() {
        List<Component> lore = new ArrayList<>();
        lore.add(DuckPrisons.getInstance().getConfigOption("backpack.info",
                Map.of("%stored%", Long.toString(getAmountStored()),
                        "%storage%", Long.toString(maxStored))));
        getStoredMaterials().forEach((material, count) ->
                lore.add(DuckPrisons.getInstance().getConfigOption("backpack.item",
                        Map.of("%material%", material.toString(),
                                "%amount%", count.toString()))));
        return lore;
    }

    public @NotNull ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(Material.ENDER_CHEST);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(DuckPrisons.getInstance().getConfigOption("backpack.name",
                Map.of("%player%", player.getPlayer().getName())));
        meta.lore(getLore());
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
