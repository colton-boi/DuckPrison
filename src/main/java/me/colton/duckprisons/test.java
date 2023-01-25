package me.colton.duckprisons;

import me.colton.duckprisons.mines.PrivateMine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class test implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command is for players only!");
            return false;
        }

        PrivateMine pMine = PrivateMine.getOrCreate(player);

        pMine.reset();
        pMine.teleportToSpawn(player);

        return false;
    }
}
