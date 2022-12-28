package colton.duckprisons;

import colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GetPickaxe implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command is for players only!");
            return false;
        }

        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = pickaxe.getItemMeta();
        List<Component> lore = new ArrayList<>();

        meta.displayName(Component.text(player.getName(), NamedTextColor.AQUA)
                .append(Component.text(" • ", NamedTextColor.DARK_GRAY))
                .append(Component.text("Mining Tool", NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false));

        meta.lore(lore);
        pickaxe.setItemMeta(meta);
        pickaxe.addUnsafeEnchantment(Enchantment.DIG_SPEED, 25);
        pickaxe.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        for (PickaxeEnchants pickaxeEnchants : PickaxeEnchants.values()) {
            if (pickaxeEnchants == PickaxeEnchants.EFFICIENCY) {
                pickaxeEnchants.setLevel(pickaxe, 25);
            } else if (pickaxeEnchants == PickaxeEnchants.FORTUNE) {
                pickaxeEnchants.setLevel(pickaxe, 10);
            } else {
                pickaxeEnchants.setLevel(pickaxe, 0);
            }
        }

        player.getInventory().addItem(pickaxe);

        return false;
    }

}
