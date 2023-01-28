package me.colton.duckprisons.mines;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockType;
import me.colton.duckprisons.DuckPrisons;
import me.colton.duckprisons.PrisonPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.structure.Structure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PrivateMine implements Mine {

    private static final @NotNull HashMap<UUID, PrivateMine> privateMines = new HashMap<>();
    public static final @NotNull Structure privateMineStructure = Bukkit.getStructureManager().createStructure();
    public static final @NotNull YamlConfiguration privateMineData = loadPrivateMineData();

    public static @NotNull HashMap<UUID, PrivateMine> getPrivateMines() {
        return privateMines;
    }

    public static @Nullable PrivateMine get(@NotNull OfflinePlayer player) {
        if (privateMines.get(player.getUniqueId()) != null) {
            return privateMines.get(player.getUniqueId());
        }

        ConfigurationSection data = privateMineData.getConfigurationSection(player.getUniqueId().toString());

        if (data == null || data.getLocation("center") == null) {
            return null;
        }

        Location center = data.getLocation("center");
        Material material = (Material) data.get("material");
        @SuppressWarnings("unchecked") List<UUID> members = (List<UUID>) data.getList("members");
        long apothem = data.getLong("apothem");
        long height = data.getLong("height");
        if (center != null && members != null && material != null && apothem != 0 && height != 0) {
            return new PrivateMine(player, center, material, members, apothem, height);
        }
        return null;
    }

    public static @NotNull PrivateMine getOrCreate(@NotNull OfflinePlayer player) {
        PrivateMine mine = get(player);
        if (mine != null) {
            return mine;
        }
        Location center = privateMineData.getLocation("lastLocation");
        if (center != null) {
            privateMineData.set("lastLocation", center.add(500, 0, 0));
        } else {
            center = new Location(Bukkit.getWorld("privateMines"), 0, 0, 0);
            privateMineData.set("lastLocation", center);
        }
        return new PrivateMine(player, center);
    }

    private static @NotNull YamlConfiguration loadPrivateMineData() {
        privateMineStructure.fill(new Location(Bukkit.getWorld("mines"), 500, 0, 0),
                new Location(Bukkit.getWorld("mines"), 650, 200, 150), false);

        File dataFile = new File(DuckPrisons.getInstance().getDataFolder(), "privateMines.yml");

        try {
            if (!dataFile.exists() && !dataFile.createNewFile()) {
                throw new RuntimeException("Unable to create privateMines.yml!");
            }
        } catch (IOException ignored) {

        }

        return YamlConfiguration.loadConfiguration(dataFile);
    }

    public static boolean savePrivateMineData() {

        privateMines.values().forEach(PrivateMine::save);

        File dataFile = new File(DuckPrisons.getInstance().getDataFolder(), "privateMines.yml");
        try {
            privateMineData.save(dataFile);
        } catch (IOException exception) {
            return false;
        }
        return true;
    }

    private final @NotNull OfflinePlayer owner;
    private final @NotNull UUID uuid;
    private final @NotNull List<UUID> members = new ArrayList<>();
    private final @NotNull Material material;
    private final @NotNull Location center;
    public final @NotNull Location spawnLocation;
    public final @NotNull Location topCorner;
    public final @NotNull Location bottomCorner;
    public long apothem;
    public long height;
    public long upgradePrice;

    public PrivateMine(@NotNull OfflinePlayer owner, @NotNull Location center) {
        this(owner, center, false);

    }

    public PrivateMine(@NotNull OfflinePlayer owner, @NotNull Location center, boolean structure) {
        this(owner, center, Material.STONE);

        if (structure) {
            // Place the private mine structure
            privateMineStructure.place(center.clone().subtract(75, 2, 75), false,
                    StructureRotation.NONE, Mirror.NONE, 0, 1, new Random());
        }
    }

    public PrivateMine(@NotNull OfflinePlayer owner, @NotNull Location center,
                       @NotNull Material material) {
        this(owner, center, material, new ArrayList<>());
    }

    public PrivateMine(@NotNull OfflinePlayer owner, @NotNull Location center,
                       @NotNull Material material, @NotNull List<UUID> members) {
        this(owner, center, material, members, 5, 100);
    }

    public PrivateMine(@NotNull OfflinePlayer owner, @NotNull Location center,
                       @NotNull Material material, @NotNull List<UUID> members,
                       long apothem, long height) {
        this.owner = owner;
        this.uuid = owner.getUniqueId();
        this.center = center;
        this.material = material;
        this.members.addAll(members);
        this.apothem = apothem;
        this.height = height;
        upgradePrice = Math.round(Math.pow(50000*(1.25), (apothem-5)));

        spawnLocation = center.clone().add(apothem+4.5, height+4, 0.5);

        topCorner = center.clone().add(apothem, height, apothem);
        bottomCorner = center.clone().subtract(apothem, 0, apothem);

        privateMines.put(uuid, this);
    }

    public boolean isMember(@NotNull Player player) {
        return (isOwner(player) || members.contains(player.getUniqueId()));
    }

    public boolean isOwner(@NotNull Player player) {
        return (player == owner);
    }

    public void teleportToSpawn(@NotNull Player player) {
        player.teleport(getSpawn());
    }

    public @NotNull List<UUID> getMembers() {
        return members;
    }

    public @NotNull OfflinePlayer getOwner() {
        return owner;
    }

    public @NotNull Location getCenter() {
        return center;
    }

    public @NotNull Location getSpawn() {
        return spawnLocation;
    }

    public void save() {
        // Make it automatically save:
        ConfigurationSection data = privateMineData.getConfigurationSection(uuid.toString());
        assert data != null;
        data.set("center", center);
        data.set("material", material.getKey());
        data.set("members", members);
        data.set("apothem", apothem);
        data.set("height", height);
    }

    public boolean upgradeMine() {

        if (!(owner instanceof Player owner)) {
            return false;
        }

        if (apothem >= 50) {
            return false; // Max mine size of 100x100
        }

        if (PrisonPlayer.getBalance(owner) < upgradePrice) {
            return false;
        }

        apothem++;
        PrisonPlayer.removeBalance(owner, upgradePrice);
        upgradePrice = Math.round(Math.pow(50000*(1.25), (apothem-5)));

        topCorner.add(1, 0, 1);
        bottomCorner.subtract(1, 0, 1);
        spawnLocation.add(1, 0, 0);

        // move bedrock wall surrounding
        BukkitWorld world = new BukkitWorld(topCorner.getWorld());
        EditSession session = WorldEdit.getInstance().newEditSession(world);

        BlockVector3 topVector = BlockVector3.at(topCorner.getX()+1, topCorner.getY(), topCorner.getZ()+1);
        BlockVector3 bottomVector = BlockVector3.at(bottomCorner.getX()-1, bottomCorner.getY(), bottomCorner.getZ()-1);
        CuboidRegion region = new CuboidRegion(world, topVector, bottomVector);

        BlockVector3 newTopVector = BlockVector3.at(topCorner.getX()+2, topCorner.getY(), topCorner.getZ()+2);
        BlockVector3 newBottomVector = BlockVector3.at(bottomCorner.getX()-2, bottomCorner.getY(), bottomCorner.getZ()-2);
        CuboidRegion newRegion = new CuboidRegion(world, newTopVector, newBottomVector);

        session.makeWalls(region, new BaseBlock(Objects.requireNonNull(BlockType.REGISTRY.get("air")).getDefaultState()));
        session.makeWalls(newRegion, new BaseBlock(Objects.requireNonNull(BlockType.REGISTRY.get("bedrock")).getDefaultState()));

        session.flushQueue();

        // Reset mine blocks with new corners
        reset();

        return true;
    }

    public void reset() {
        reset(topCorner, bottomCorner, List.of(material));
    }
}
