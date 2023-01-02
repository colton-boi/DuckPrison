package colton.duckprisons;

import colton.duckprisons.events.BlockBreak;
import colton.duckprisons.mines.PublicMines;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.ipvp.canvas.MenuFunctionListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        PublicMines.B.reset();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
