package colton.duckprisons;

import colton.duckprisons.backpack.Backpack;
import colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import colton.duckprisons.mines.MineBlocks;
import colton.duckprisons.mines.PublicMines;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;

public class PrisonPlayer {

    private static final HashMap<Player, PrisonPlayer> players = new HashMap<>();
    private static FileConfiguration allPlayerData;

    public static PrisonPlayer getPlayer(Player p) {
        return players.getOrDefault(p, new PrisonPlayer(p));
    }

    private static void removePlayer(Player p) {
        players.remove(p);
    }

    //////////////////
    // ECONOMY CODE //
    //////////////////

    public static long getBalance(Player p) {
        return getPlayer(p).getBalance();
    }

    public static long setBalance(Player p, long amount) {
        return getPlayer(p).setBalance(amount);
    }

    public static long addBalance(Player p, long amount) {
        return getPlayer(p).addBalance(amount);
    }

    public static long removeBalance(Player p, long amount) {
        return getPlayer(p).removeBalance(amount);
    }



    public static long getTokens(Player p) {
        return getPlayer(p).getTokens();
    }

    public static long setTokens(Player p, long amount) {
        return getPlayer(p).setTokens(amount);
    }

    public static long addTokens(Player p, long amount) {
        return getPlayer(p).addTokens(amount);
    }

    public static long removeTokens(Player p, long amount) {
        return getPlayer(p).removeTokens(amount);
    }

    //////////////
    // SETTINGS //
    //////////////

    public static boolean getBooleanSetting(Player p, String setting, boolean defaultValue) {
        return getPlayer(p).getBooleanSetting(setting, defaultValue);
    }

    public static boolean getBooleanSetting(Player p, String setting) {
        return getBooleanSetting(p, setting, false);
    }

    ////////////
    // MINING //
    ////////////

    public static Backpack getBackpack(Player p) {
        return getPlayer(p).getBackpack();
    }

    public static boolean isMineUnlocked(Player p, PublicMines mine) {
        return getPlayer(p).isMineUnlocked(mine);
    }

    private final @NotNull Player player;
    private final @NotNull Backpack backpack = new Backpack(this);
    private final @NotNull List<PublicMines> unlockedMines = new ArrayList<>();
    private final @NotNull Map<PickaxeEnchants, Long> enchantLevels = new HashMap<>();
    private long balance = 0;
    private long tokens = 0;

    public PrisonPlayer(@NotNull Player player) {
        this.player = player;
        players.put(player, this);

        loadPlayerData();
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public long getLevel(PickaxeEnchants enchant) {
        if (enchantLevels.containsKey(enchant)) {
            return enchantLevels.get(enchant);
        } else {
            if (player.getInventory().getItemInMainHand().getType().toString().contains("PICKAXE")) {
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

    public boolean getBooleanSetting(String setting, boolean defaultValue) {
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
            throw new RuntimeException("getPlayerData() failed to create new Configuration Section for player " + player);
        }

        // Backpacks
        ConfigurationSection backpackSection = getPlayerData().getConfigurationSection("backpackStorage");
        if (backpackSection == null) {
            backpackSection = getPlayerData().createSection("backpackStorage");
        }
        for (MineBlocks block : MineBlocks.values()) {
            backpackSection.set(block.getDropMaterial().toString(),
                    backpack.getStoredItems().getOrDefault(block.getDropMaterial(), 0L));
        }

        // Mine Ranks
        getPlayerData().set("unlockedMines", unlockedMines.stream().map(Enum::name).toList());

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

        if (allPlayerData.contains(player.getUniqueId().toString())) {
            playerData = allPlayerData.getConfigurationSection(player.getUniqueId().toString());
        } else {
            playerData = allPlayerData.createSection(player.getUniqueId().toString());
        }

        return playerData;
    }

    private void loadPlayerData() {
        // Backpacks
        ConfigurationSection backpackInfo = getPlayerData().getConfigurationSection("backpackStorage");
        if (backpackInfo != null) {
            for (MineBlocks mineBlock : MineBlocks.values()) {
                ItemStack itemStack = backpackInfo.getItemStack(mineBlock.toString());
                if (itemStack != null) {
                    backpack.addItems(itemStack);
                }
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
