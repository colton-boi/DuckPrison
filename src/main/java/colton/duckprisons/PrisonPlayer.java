package colton.duckprisons;

import colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
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

    public static boolean getBooleanSetting(Player p, String setting) {
        return getPlayer(p).getBooleanSetting(setting);
    }

    private final Player p;
    private final Map<PickaxeEnchants, Long> enchantLevels;
    private long balance = 0;
    private long tokens = 0;

    public PrisonPlayer(Player p) {
        this.p = p;
        enchantLevels = new HashMap<>();
        players.put(p, this);
    }

    public Player getPlayer() {
        return p;
    }

    public long getLevel(PickaxeEnchants enchant) {
        if (enchantLevels.containsKey(enchant)) {
            return enchantLevels.get(enchant);
        } else {
            if (p.getInventory().getItemInMainHand().getType().toString().contains("PICKAXE")) {
                enchantLevels.put(enchant, enchant.getLevel(p.getInventory().getItemInMainHand()));
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
        return balance+=amount;
    }

    public long removeBalance(long amount) {
        return balance-=amount;
    }


    public long getTokens() {
        return tokens;
    }

    public long setTokens(long amount) {
        return tokens = amount;
    }

    public long addTokens(long amount) {
        return tokens+=amount;
    }

    public long removeTokens(long amount) {
        return tokens-=amount;
    }

    //////////////
    // SETTINGS //
    //////////////

    public boolean getBooleanSetting(String setting) {
        return getPlayerData().getBoolean(setting);
    }



    public void save() {
        if (saveData()) {
            removePlayer(p);
        } else {
            getServer().getScheduler().runTaskLater(DuckPrisons.getInstance(), this::save, 20);
        }
    }

    private boolean saveData() {
        ConfigurationSection playerData = getPlayerData();

        if (playerData == null) {
            return false;
        }

        playerData.get("test");

        return false;
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

        if (allPlayerData.contains(p.getUniqueId().toString())) {
            playerData = allPlayerData.getConfigurationSection(p.getUniqueId().toString());
        } else {
            playerData = allPlayerData.createSection(p.getUniqueId().toString());
        }

        return playerData;
    }
}
