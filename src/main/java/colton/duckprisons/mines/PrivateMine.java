package colton.duckprisons.mines;

import colton.duckprisons.DuckPrisons;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrivateMine implements Mine {

    private static final @NotNull HashMap<Player, PrivateMine> privateMines = new HashMap<>();
    public static final @NotNull YamlConfiguration privateMineData = loadPrivateMineData();

    public static @NotNull HashMap<Player, PrivateMine> getMines() {
        return privateMines;
    }

    public static @Nullable PrivateMine get(@NotNull Player player) {
        if (privateMines.get(player) != null) {
            return privateMines.get(player);
        }

        ConfigurationSection data = privateMineData.getConfigurationSection(player.getUniqueId().toString());

        if (data == null) {
            return null;
        }

        @SuppressWarnings("unchecked")
        List<Player> members = (List<Player>) data.getList("members");
        Material material = (Material) data.get("material");
        Location center = data.getLocation("center");
        long apothem = data.getLong("apothem");
        long height = data.getLong("height");
        if (center != null && members != null && material != null && apothem != 0 && height != 0) {
            return new PrivateMine(player, members, material, center, apothem, height);
        }
        return null;
    }

    public static @NotNull PrivateMine getOrCreate(@NotNull Player player) {
        PrivateMine mine = get(player);
        if (mine != null) {
            return mine;
        }
        Location center = privateMineData.getLocation("lastLocation");
        if (center != null) {
            return new PrivateMine(player, new ArrayList<>(), Material.STONE,
                    center.add(500, 0, 0), 50, 100);
        } else {
            center = new Location(Bukkit.getWorld("privateMines"), 0, 0, 0);
            privateMineData.set("lastLocation", center);
            return new PrivateMine(player, new ArrayList<>(), Material.STONE,
                    center, 50, 100);
        }
    }

    public static @Nullable Location getCenter(@NotNull Player player) {
        return (privateMines.containsKey(player)) ? privateMines.get(player).getCenter() : null;
    }


    private static @NotNull YamlConfiguration loadPrivateMineData() {
        File dataFile = new File(DuckPrisons.getInstance().getDataFolder(), "privateMines.yml");

        try {
            if (!dataFile.exists() && !dataFile.createNewFile()) {
                throw new RuntimeException("Unable to create privateMines.yml!");
            }
        } catch (Exception ignored) {
        }

        return YamlConfiguration.loadConfiguration(dataFile);
    }

    private final @NotNull Player owner;
    private final @NotNull List<Player> members = new ArrayList<>();
    private final @NotNull Material material;
    private final @NotNull Location center;
    public final @NotNull Location topCorner;
    public final @NotNull Location bottomCorner;

    public PrivateMine(@NotNull Player owner, @NotNull List<Player> members,
                       @NotNull Material material, @NotNull Location center,
                       long apothem, long height) {
        this.owner = owner;
        this.members.addAll(members);
        this.material = material;
        this.center = center;

        topCorner = center.clone().add(apothem, height, apothem);
        bottomCorner = center.clone().subtract(apothem, 0, apothem);

        privateMines.put(owner, this);
    }

    public boolean isMember(@NotNull Player player) {
        return (player == owner || members.contains(player));
    }

    public boolean isOwner(@NotNull Player player) {
        return (player == owner);
    }

    public @NotNull List<Player> getMembers() {
        return members;
    }

    public @NotNull Player getOwner() {
        return owner;
    }

    public @NotNull Location getCenter() {
        return center;
    }

    public void reset() {
        reset(topCorner, bottomCorner, List.of(material));
    }
}
