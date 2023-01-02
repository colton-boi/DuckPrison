package colton.duckprisons.enchants.guis;

import colton.duckprisons.enchants.pickaxe.PickaxeEnchants;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.type.ChestMenu;
import org.jetbrains.annotations.NotNull;

public class PickaxeGUI {

    public static void showPickaxeMenu(@NotNull Player player, @NotNull ItemStack pickaxe) {
        int rows = (int) (Math.ceil(PickaxeEnchants.values().length/7.0)+2);

        Menu menu = ChestMenu.builder(rows)
                .title(ChatColor.translateAlternateColorCodes('&', "&eUpgrade Pickaxe"))
                .redraw(false)
                .build();

        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        glass.editMeta(itemMeta -> itemMeta.displayName(Component.text("")));
        for (int i = 1; i < rows; i++) {
            menu.getSlot(i*9-1).setItem(glass);
            menu.getSlot(i*9).setItem(glass);
        }

        for (int i = 0; i < 9; i++) {
            menu.getSlot(i).setItem(glass);
            menu.getSlot(i+9*(rows-1)).setItem(glass);
        }

        for (PickaxeEnchants enchant : PickaxeEnchants.values()) {
            ItemStack enchantItem = enchant.getItemStack(enchant.getLevel(pickaxe));

            menu.getSlot(enchant.getSlot()).setItem(enchantItem);
            menu.getSlot(enchant.getSlot()).setClickHandler((p, info) -> enchant.showUpgradeMenu(p, pickaxe));
        }

        menu.open(player);
    }

}
