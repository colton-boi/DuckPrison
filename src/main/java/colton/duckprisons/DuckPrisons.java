package colton.duckprisons;

import colton.duckprisons.events.BlockBreak;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.ipvp.canvas.MenuFunctionListener;

import java.util.Map;

public final class DuckPrisons extends JavaPlugin {

    private static Economy econ;
    private static DuckPrisons instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("test").setExecutor(new test());
        getCommand("getPickaxe").setExecutor(new GetPickaxe());
        Bukkit.getPluginManager().registerEvents(new MenuFunctionListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreak(), this);
        instance = this;
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

    public String getConfigOption(String path, Map<String, String> variables) {
        String value = getConfigOption(path).toString();
        for (String key : variables.keySet()) {
            value = value.replace(key, variables.get(key));
        }
        return value;
    }

}
