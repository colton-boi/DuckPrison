package colton.duckprisons;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static colton.duckprisons.enchants.guis.PickaxeGUI.showPickaxeMenu;

public class test implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command is for players only!");
            return false;
        }

        showPickaxeMenu(player, player.getInventory().getItemInMainHand());

        return false;
    }
}
