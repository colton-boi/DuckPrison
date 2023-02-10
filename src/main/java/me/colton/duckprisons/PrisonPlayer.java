package me.colton.duckprisons;

import me.colton.duckprisons.backpack.Backpack;
import me.colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import me.colton.duckprisons.mines.MineBlock;
import me.colton.duckprisons.mines.PrivateMine;
import me.colton.duckprisons.mines.PublicMines;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;

public class PrisonPlayer {

    private static final @NotNull HashMap<OfflinePlayer, PrisonPlayer> players = new HashMap<>();
    private static final @NotNull FileConfiguration allPlayerData = loadData();

    private static @NotNull FileConfiguration loadData() {

        File dataFile = new File(DuckPrisons.getInstance().getDataFolder(), "playerData.yml");

        try {
            if (!dataFile.exists() && !dataFile.createNewFile()) {
                throw new RuntimeException("Unable to create playerData.yml!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return YamlConfiguration.loadConfiguration(dataFile);
    }

    public static @NotNull PrisonPlayer getOfflinePlayer(@NotNull OfflinePlayer player) {
        return players.getOrDefault(player, new PrisonPlayer(player));
    }

    private static void removePlayer(@NotNull OfflinePlayer player) {
        players.remove(player);
    }

    //////////////////
    // ECONOMY CODE //
    //////////////////

    public static long getBalance(@NotNull OfflinePlayer player) {
        return getOfflinePlayer(player).getBalance();
    }

    public static long setBalance(@NotNull OfflinePlayer player, long amount) {
        return getOfflinePlayer(player).setBalance(amount);
    }

    public static long addBalance(@NotNull OfflinePlayer player, long amount) {
        return getOfflinePlayer(player).addBalance(amount);
    }

    public static long removeBalance(@NotNull OfflinePlayer player, long amount) {
        return getOfflinePlayer(player).removeBalance(amount);
    }

    public static long getMultiplier(@NotNull OfflinePlayer player) {
        return getOfflinePlayer(player).getMultiplier();
    }

    public static long setMultiplier(@NotNull OfflinePlayer player, long amount) {
        return getOfflinePlayer(player).setMultiplier(amount);
    }

    public static long addMultiplier(@NotNull OfflinePlayer player, long amount) {
        return getOfflinePlayer(player).addMultiplier(amount);
    }

    public static long removeMultiplier(@NotNull OfflinePlayer player, long amount) {
        return getOfflinePlayer(player).removeMultiplier(amount);
    }

    public static long getTokens(@NotNull OfflinePlayer player) {
        return getOfflinePlayer(player).getTokens();
    }

    public static long setTokens(@NotNull OfflinePlayer player, long amount) {
        return getOfflinePlayer(player).setTokens(amount);
    }

    public static long addTokens(@NotNull OfflinePlayer player, long amount) {
        return getOfflinePlayer(player).addTokens(amount);
    }

    public static long removeTokens(@NotNull OfflinePlayer player, long amount) {
        return getOfflinePlayer(player).removeTokens(amount);
    }

    //////////////
    // SETTINGS //
    //////////////

    public static boolean getBooleanSetting(@NotNull OfflinePlayer player, @NotNull String setting, boolean defaultValue) {
        return getOfflinePlayer(player).getBooleanSetting(setting, defaultValue);
    }

    public static boolean getBooleanSetting(@NotNull OfflinePlayer player, String setting) {
        return getBooleanSetting(player, setting, false);
    }

    ////////////
    // MINING //
    ////////////

    public static @NotNull Backpack getBackpack(@NotNull OfflinePlayer player) {
        return getOfflinePlayer(player).getBackpack();
    }

    public static boolean isMineUnlocked(@NotNull OfflinePlayer player, @NotNull PublicMines mine) {
        return getOfflinePlayer(player).isMineUnlocked(mine);
    }

    private final @NotNull OfflinePlayer player;
    private final @NotNull Backpack backpack = new Backpack(this, 100);
    private final @NotNull List<PublicMines> unlockedMines = new ArrayList<>();
    private final @NotNull Map<PickaxeEnchants, Long> enchantLevels = new HashMap<>();
    private long balance = 0;
    private long tokens = 0;
    private long multiplier = 1;
    private ConfigurationSection data;

    public PrisonPlayer(@NotNull OfflinePlayer player) {
        this.player = player;
        players.put(player, this);

        loadPlayerData();
    }

    public @NotNull OfflinePlayer getOfflinePlayer() {
        return player;
    }

    public @NotNull Player getPlayer() {
        if (player.getPlayer() == null) {
            throw new RuntimeException("getPlayer() was called for an offline player");
        }
        return player.getPlayer();
    }

    public long getLevel(@NotNull PickaxeEnchants enchant) {
        if (enchantLevels.containsKey(enchant)) {
            return enchantLevels.get(enchant);
        } else {
            if (player instanceof Player player && player.getInventory().getItemInMainHand().getType().toString().contains("PICKAXE")) {
                enchantLevels.put(enchant, enchant.getLevel(player.getInventory().getItemInMainHand()));
                return enchantLevels.get(enchant);
            } else {
                return 0;
            }
        }
    }

    //////////////////
    // ECONOMY CODE //
    //////////////////

    public long getBalance() {
        return balance;
    }

    public long setBalance(long amount) {
        return balance = amount;
    }

    public long addBalance(long amount) {
        return balance += amount;
    }

    public long removeBalance(long amount) {
        return balance -= amount;
    }

    public long getMultiplier() {
        return multiplier;
    }

    public long setMultiplier(long amount) {
        return multiplier = amount;
    }

    public long addMultiplier(long amount) {
        return multiplier += amount;
    }

    public long removeMultiplier(long amount) {
        return multiplier -= amount;
    }


    public long getTokens() {
        return tokens;
    }

    public long setTokens(long amount) {
        return tokens = amount;
    }

    public long addTokens(long amount) {
        return tokens += amount;
    }

    public long removeTokens(long amount) {
        return tokens -= amount;
    }

    //////////////
    // SETTINGS //
    //////////////

    public boolean getBooleanSetting(@NotNull String setting, boolean defaultValue) {
        return getPlayerData().getBoolean(setting, defaultValue);
    }

    ////////////
    // MINING //
    ////////////

    public @NotNull Backpack getBackpack() {
        return backpack;
    }

    public boolean isMineUnlocked(PublicMines mine) {
        return unlockedMines.contains(mine);
    }

    public void save() {
        if (saveData()) {
            removePlayer(player);
        } else {
            getServer().getScheduler().runTaskLater(DuckPrisons.getInstance(), this::save, 20);
        }
    }

    private boolean saveData() {
        // Backpacks
        ConfigurationSection backpackSection = getPlayerData().getConfigurationSection("backpackStorage");
        if (backpackSection == null) {
            backpackSection = getPlayerData().createSection("backpackStorage");
        }
        for (MineBlock block : MineBlock.values()) {
            backpackSection.set(block.getDropMaterial().toString(),
                    backpack.getStoredMaterials().getOrDefault(block.getDropMaterial(), 0L));
        }
        backpackSection.set("maxStorage", backpack.maxStored);

        // Mine Ranks
        getPlayerData().set("unlockedMines", unlockedMines.stream().map(Enum::name).toList());

        // Private Mine Info
        PrivateMine privateMine = PrivateMine.get(player);
        if (privateMine != null) {
            privateMine.save();
        } else {
            Bukkit.getLogger().severe("HUH?");
        }

        return true;
    }

    public @NotNull ConfigurationSection getPlayerData() {

        if (data != null) {
            return data;
        }
        ConfigurationSection playerData = allPlayerData.getConfigurationSection(player.getUniqueId().toString());

        return playerData != null ? (data = playerData) : (data = allPlayerData.createSection(player.getUniqueId().toString()));
    }

    private void loadPlayerData() {
        // Backpacks
        ConfigurationSection backpackInfo = getPlayerData().getConfigurationSection("backpackStorage");
        if (backpackInfo != null) {
            for (MineBlock mineBlock : MineBlock.values()) {
                long amount = backpackInfo.getLong(mineBlock.getDropMaterial().toString());
                backpack.addBlocks(mineBlock, amount);
            }
            backpack.maxStored = backpackInfo.getLong("maxStorage", backpack.maxStored);
        }

        // Mine Ranks
        List<?> mineRankInfo = getPlayerData().getList("unlockedMines");
        if (mineRankInfo == null) {
            unlockedMines.add(PublicMines.A);
        } else {
            for (Object mine : mineRankInfo) {
                unlockedMines.add(PublicMines.valueOf(mine.toString()));
            }
        }

        tokens = getPlayerData().getLong("tokens");
        balance = getPlayerData().getLong("balance");
        multiplier = getPlayerData().getLong("multiplier", 1);
    }
}
