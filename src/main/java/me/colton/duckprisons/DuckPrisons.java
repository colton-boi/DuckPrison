package me.colton.duckprisons;

import me.colton.duckprisons.events.BlockBreak;
import me.colton.duckprisons.mines.PrivateMine;
import me.colton.duckprisons.mines.PublicMines;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.ipvp.canvas.MenuFunctionListener;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

public final class DuckPrisons extends JavaPlugin {

    private static DuckPrisons instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("test").setExecutor(new test());
        getCommand("getPickaxe").setExecutor(new GetPickaxe());
        Bukkit.getPluginManager().registerEvents(new MenuFunctionListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreak(), this);
        instance = this;
        Arrays.stream(PublicMines.values()).forEach(PublicMines::reset);
        PrivateMine.getPrivateMines().values().forEach(PrivateMine::reset);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (!PrivateMine.savePrivateMineData()) {
            getLogger().severe("Failed to save private mine data! Report this immediately.");
        }
    }

    public static DuckPrisons getInstance() {
        return instance;
    }

    public Object getConfigOption(String path) {
        return getConfig().get(path, "");
    }

    public @NotNull String getConfigOption(String path, Map<String, String> variables) {
        String value = getConfigOption(path).toString();
        if (value != null) {
            for (String key : variables.keySet()) {
                value = value.replace(key, variables.get(key));
            }
            return value;
        }
        return "";
    }

}
