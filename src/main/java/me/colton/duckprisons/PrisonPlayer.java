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
    private static FileConfiguration allPlayerData;

    public static @NotNull PrisonPlayer getPlayer(@NotNull OfflinePlayer player) {
        return players.getOrDefault(player, new PrisonPlayer(player));
    }

    private static void removePlayer(@NotNull OfflinePlayer player) {
        players.remove(player);
    }

    //////////////////
    // ECONOMY CODE //
    //////////////////

    public static long getBalance(@NotNull OfflinePlayer player) {
        return getPlayer(player).getBalance();
    }

    public static long setBalance(@NotNull OfflinePlayer player, long amount) {
        return getPlayer(player).setBalance(amount);
    }

    public static long addBalance(@NotNull OfflinePlayer player, long amount) {
        return getPlayer(player).addBalance(amount);
    }

    public static long removeBalance(@NotNull OfflinePlayer player, long amount) {
        return getPlayer(player).removeBalance(amount);
    }



    public static long getTokens(@NotNull OfflinePlayer player) {
        return getPlayer(player).getTokens();
    }

    public static long setTokens(@NotNull OfflinePlayer player, long amount) {
        return getPlayer(player).setTokens(amount);
    }

    public static long addTokens(@NotNull OfflinePlayer player, long amount) {
        return getPlayer(player).addTokens(amount);
    }

    public static long removeTokens(@NotNull OfflinePlayer player, long amount) {
        return getPlayer(player).removeTokens(amount);
    }

    //////////////
    // SETTINGS //
    //////////////

    public static boolean getBooleanSetting(@NotNull OfflinePlayer player, @NotNull String setting, boolean defaultValue) {
        return getPlayer(player).getBooleanSetting(setting, defaultValue);
    }

    public static boolean getBooleanSetting(Player player, String setting) {
        return getBooleanSetting(player, setting, false);
    }

    ////////////
    // MINING //
    ////////////

    public static @NotNull Backpack getBackpack(@NotNull OfflinePlayer player) {
        return getPlayer(player).getBackpack();
    }

    public static boolean isMineUnlocked(@NotNull OfflinePlayer player, @NotNull PublicMines mine) {
        return getPlayer(player).isMineUnlocked(mine);
    }

    private final @NotNull OfflinePlayer player;
    private final @NotNull Backpack backpack = new Backpack(this);
    private final @NotNull List<PublicMines> unlockedMines = new ArrayList<>();
    private final @NotNull Map<PickaxeEnchants, Long> enchantLevels = new HashMap<>();
    private long balance = 0;
    private long tokens = 0;

    public PrisonPlayer(@NotNull OfflinePlayer player) {
        this.player = player;
        players.put(player, this);

        loadPlayerData();
    }

    public @NotNull OfflinePlayer getPlayer() {
        return player;
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
        ConfigurationSection playerData = getPlayerData();

        if (playerData == null) {
            throw new RuntimeException("getPlayerData() failed to create new Configuration Section for OfflinePlayer " + player);
        }

        // Backpacks
        ConfigurationSection backpackSection = playerData.getConfigurationSection("backpackStorage");
        if (backpackSection == null) {
            backpackSection = playerData.createSection("backpackStorage");
        }
        for (MineBlock block : MineBlock.values()) {
            backpackSection.set(block.getDropMaterial().toString(),
                    backpack.getStoredMaterials().getOrDefault(block.getDropMaterial(), 0L));
        }

        // Mine Ranks
        playerData.set("unlockedMines", unlockedMines.stream().map(Enum::name).toList());

        // Private Mine Info
        PrivateMine privateMine = PrivateMine.get(player);
        if (privateMine != null) {
            privateMine.save();
        } else {
            Bukkit.getLogger().severe("HUH?");
        }

        return true;
    }

    public ConfigurationSection getPlayerData() {
        if (allPlayerData == null) {
            File dataFile = new File(DuckPrisons.getInstance().getDataFolder(), "playerData.yml");

            try {
                if (!dataFile.exists() && !dataFile.createNewFile()) {
                    throw new RuntimeException("Unable to create playerData.yml!");
                }
            } catch (Exception e) {
                return null;
            }

            allPlayerData = YamlConfiguration.loadConfiguration(dataFile);
        }

        ConfigurationSection playerData;

        playerData = allPlayerData.getConfigurationSection(player.getUniqueId().toString());

        return playerData;
    }

    private void loadPlayerData() {
        // Backpacks
        ConfigurationSection backpackInfo = getPlayerData().getConfigurationSection("backpackStorage");
        if (backpackInfo != null) {
            for (MineBlock mineBlock : MineBlock.values()) {
                long amount = backpackInfo.getLong(mineBlock.getDropMaterial().toString());
                backpack.addBlocks(mineBlock, amount);
            }
        }

        // Mine Ranks
        List<?> mineRankInfo = getPlayerData().getList("unlockedMines");
        if (mineRankInfo == null) {
            getPlayerData().set("unlockedMines", List.of(PublicMines.A));
            unlockedMines.add(PublicMines.A);
        } else {
            for (Object mine : mineRankInfo) {
                unlockedMines.add(PublicMines.valueOf(mine.toString()));
            }
        }


    }
}
