package me.colton.duckprisons.enchants.pickaxe;


import me.colton.duckprisons.PrisonPlayer;
import me.colton.duckprisons.enchants.EnchantLevel;
import me.colton.duckprisons.enchants.pickaxe.levelone.*;
import me.colton.duckprisons.enchants.pickaxe.leveltwo.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.type.HopperMenu;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static me.colton.duckprisons.DuckPrisons.getInstance;

public enum PickaxeEnchants {

    // Increases the dig speed of a pickaxe
    EFFICIENCY(EnchantLevel.ONE, "Efficiency", "efficiency",
            500, 5000, 1.5, 10, Material.NETHERITE_PICKAXE, new Efficiency()),

    // Multiplies the blocks given to the player by the level
    FORTUNE(EnchantLevel.ONE, "Fortune", "fortune",
            5000, 5000, 1.5, 11, Material.DIAMOND_PICKAXE, new Fortune()),

    // Applies haste %level% to player
    HASTE(EnchantLevel.ONE, "Haste", "haste",
            3, 15000, 100, 12, Material.GOLDEN_PICKAXE, new Haste()),

    // Applies speed %level% to player
    SPEED(EnchantLevel.ONE, "Speed", "speed",
            3, 15000, 100, 13, Material.IRON_BOOTS, new Speed()),

    // Allows the player to fly
    FLIGHT(EnchantLevel.ONE, "Flight", "flight",
            1, 5000000, 0, 14, Material.ELYTRA, new Flight()),

    // Chance to get paid while mining
    SALARY(EnchantLevel.ONE, "Salary", "salary",
            2500, 5000, 1, 15, Material.SUNFLOWER, new Salary()),

    // Chance to find tokens while mining
    TOKEN_FINDER(EnchantLevel.ONE, "Token Finder", "token_finder",
            2500, 5000, 1, 16, Material.AMETHYST_SHARD, new TokenFinder()),


    // Explodes a cube of size 2x2x2, 3x3x3, or 5x5x5 based on level
    EXPLOSIVE(EnchantLevel.TWO, "Explosive", "explosive", 10000,
            5000, 1, 20, Material.TNT, new Explosive(), true),

    // Mines an entire layer of a mine
    JACKHAMMER(EnchantLevel.TWO, "Jackhammer", "jackhammer", 10000,
            5000, 1, 21, Material.BEDROCK, new Jackhammer(), true),

    // Chance to find keys while mining
    KEY_FINDER(EnchantLevel.TWO, "Key Finder", "key_finder", 2500,
            5000, 1, 22, Material.TRIPWIRE_HOOK, new KeyFinder()),

    // Chance to give money to all players
    CHARITY(EnchantLevel.TWO, "Charity", "charity", 2500,
            5000, 1, 23, Material.BEDROCK, new Charity()),

    // Chance to find levels while mining
    LEVEL_FINDER(EnchantLevel.TWO, "Level Finder", "level_finder", 2500,
            500, 1, 24, Material.EXPERIENCE_BOTTLE, new LevelFinder())
    ;

    final @NotNull EnchantLevel level;
    final @NotNull String displayName;
    final @NotNull String name;
    final @NotNull ItemStack itemStack;
    final @NotNull NamespacedKey key;
    final @NotNull PickaxeEnchant instance;
    final int maxLevel;
    final int startPrice;
    final double factor;
    final int slot;
    final boolean dangerous;

    /**
     * Register a new enchant
     *
     * @param level         The EnchantLevel used to coloring
     * @param displayName   The display name of the enchant
     * @param name          The name to be used backend
     * @param maxLevel      The max level for the enchant
     * @param startPrice    The price for the first level
     * @param factor        The speed at which the enchant's price should increase
     * @param slot          The slot that the enchant will show up in for GUIS
     * @param item          The item used to represent the enchant
     * @param instance      The instance for the enchant
     */
    PickaxeEnchants(@NotNull EnchantLevel level, @NotNull String displayName, @NotNull String name,
                    int maxLevel, int startPrice, double factor, int slot,
                    @NotNull Material item, @NotNull PickaxeEnchant instance) {
        this(level, displayName, name, maxLevel, startPrice, factor, slot, item, instance, false);
    }

    /**
     * Register a new enchant
     *
     * @param level         The EnchantLevel used to coloring
     * @param displayName   The display name of the enchant
     * @param name          The name to be used backend
     * @param maxLevel      The max level for the enchant
     * @param startPrice    The price for the first level
     * @param factor        The speed at which the enchant's price should increase
     * @param slot          The slot that the enchant will show up in for GUIS
     * @param item          The item used to represent the enchant
     * @param instance      The instance for the enchant
     * @param dangerous     Whether this enchant could cause and infinite loop (Ex: Explosive)
     */
    PickaxeEnchants(@NotNull EnchantLevel level, @NotNull String displayName, @NotNull String name,
                    int maxLevel, int startPrice, double factor, int slot,
                    @NotNull Material item, @NotNull PickaxeEnchant instance, boolean dangerous) {
        this.level = level;
        this.displayName = displayName;
        this.name = name;
        this.maxLevel = maxLevel;
        this.startPrice = startPrice;
        this.factor = factor;
        this.itemStack = new ItemStack(item);
        this.slot = slot;
        this.instance = instance;
        this.dangerous = dangerous;
        this.key = Objects.requireNonNull(NamespacedKey.fromString(getName() + "level", getInstance()));
    }

    public @NotNull EnchantLevel getEnchantLevel() {
        return level;
    }

    public @NotNull String getDisplayName() {
        return displayName;
    }

    public @NotNull String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getSlot() {
        return slot;
    }

    public boolean isDangerous() {
        return dangerous;
    }

    public boolean use(@NotNull Event e) {
        return instance.use(e);
    }

    public void use(@NotNull Player player, @NotNull Block block) {
        instance.use(player, block);
    }

    public long getPrice(long startLevel, long endLevel) {
        long value = 0;
        double toPower = 0.1*startLevel*factor;
        double add = 0.1*factor;
        for (long i = startLevel; i <= endLevel; i++) {
            value+=Math.pow(1.025, toPower);
            toPower+=add;
        }
        value*=startPrice;
        return value;
    }

    public long getMaxBuy(@NotNull Player player, long startLevel, boolean endLevel) {
        long value = 0;
        double toPower = 0.1*startLevel*factor;
        double add = 0.1*factor;
        for (long i = startLevel; i <= maxLevel; i++) {
            value+=startPrice*Math.pow(1.025, toPower);
            if (value > PrisonPlayer.getBalance(player)) {
                return (endLevel) ? i - 1 : i - 1 - startLevel;
            }
            toPower+=add;
        }
        return (endLevel) ? maxLevel : maxLevel - startLevel;
    }

    public @NotNull ItemStack getItemStack(long level) {
        ItemStack item = itemStack.clone();

        List<Component> lore = new ArrayList<>();
        // Have to disable ITALICS due to 1.19+ clients being spEcIAl!.
        lore.add(Component.text(" Upgrade ", NamedTextColor.GRAY)
                .append(Component.text(displayName, NamedTextColor.YELLOW))
                .append(Component.text(".", NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text(" Current Level: ", NamedTextColor.GRAY)
                .append(Component.text(level, NamedTextColor.YELLOW))
                .append(Component.text("/", NamedTextColor.GRAY))
                .append(Component.text(getMaxLevel(), NamedTextColor.YELLOW)).decoration(TextDecoration.ITALIC, false));

        item.editMeta(itemMeta -> {
            itemMeta.displayName(Component.text(getDisplayName(), getEnchantLevel().getColor())
                    .decoration(TextDecoration.ITALIC, false));
            itemMeta.lore(lore);
        });

        return item;
    }

    public void showUpgradeMenu(@NotNull Player player, @NotNull ItemStack pickaxe) {
        Menu menu = HopperMenu.builder()
                .title(ChatColor.translateAlternateColorCodes('&', "&7Upgrade &e" + getDisplayName() + "&7:"))
                .build();

        long level = getLevel(pickaxe);

        List<Long> upgradeBy = List.of(1L, 10L, 100L, 1000L, getMaxBuy(player, level, true));

        for (int i = 0; i < 5; i++) {
            ItemStack item = itemStack.clone();
            long upgradeCost = getPrice(level, level+upgradeBy.get(i));

            List<Component> lore = List.of(Component.text("Cost: ", NamedTextColor.DARK_AQUA)
                    .append(Component.text(upgradeCost, NamedTextColor.YELLOW))
                    .decoration(TextDecoration.ITALIC, false));

            int finalI = i;
            item.editMeta(itemMeta -> {
                itemMeta.displayName(Component.text("Upgrade ", NamedTextColor.DARK_AQUA)
                        .append(Component.text(getDisplayName(), NamedTextColor.AQUA))
                        .append(Component.text(" " + upgradeBy.get(finalI), NamedTextColor.YELLOW))
                        .append(Component.text(" Times", NamedTextColor.DARK_AQUA))
                        .decoration(TextDecoration.ITALIC, false));
                itemMeta.lore(lore);
            });

            menu.getSlot(i).setItem(item);

            menu.getSlot(i).setClickHandler(((p, info) -> attemptUpgrade(p, pickaxe, upgradeBy.get(finalI))));
        }

        menu.open(player);
    }

    public void attemptUpgrade(@NotNull Player p, @NotNull ItemStack pickaxe, long levels) {
        long currentLevel = getLevel(pickaxe);
        long upgradeCost = getPrice(currentLevel, currentLevel+levels);

        if (PrisonPlayer.getBalance(p) < upgradeCost) {
            p.sendMessage(getInstance().getConfigOption("messages.cantAffordUpgrade",
                    Map.of("%cost%", String.valueOf(upgradeCost),
                            "%balance%", String.valueOf(PrisonPlayer.getTokens(p)),
                            "%missing%", String.valueOf(upgradeCost-PrisonPlayer.getTokens(p)))));
            return;
        }
        if ((currentLevel+levels) > getMaxLevel()) {
            p.sendMessage(getInstance().getConfigOption("messages.exceedMaxLevel",
                    Map.of("%max%", String.valueOf(getMaxLevel()),
                            "%enchant%", getDisplayName())));
            return;
        }

        PrisonPlayer.removeBalance(p, (int) upgradeCost);
        addLevels(pickaxe, levels);
        p.sendMessage(getInstance().getConfigOption("messages.upgradeEnchant",
                Map.of("%amount%", String.valueOf(levels),
                        "%enchant%", getDisplayName())));
        showUpgradeMenu(p, pickaxe);
    }

    public void addLevels(@NotNull ItemStack pickaxe, long levels) {
        if (levels < 0) {
            removeLevels(pickaxe, -levels);
        }

        if (getLevel(pickaxe) + levels > maxLevel) {
            setLevel(pickaxe, maxLevel);
        } else {
            setLevel(pickaxe, (getLevel(pickaxe) + levels));
        }

    }

    public void removeLevels(@NotNull ItemStack pickaxe, long levels) {
        if (levels < 0) {
            addLevels(pickaxe, -levels);
        }

        if (getLevel(pickaxe) - levels < 0) {
            setLevel(pickaxe, 0);
        } else {
            setLevel(pickaxe, (getLevel(pickaxe) - levels));
        }
    }

    public long getLevel(@NotNull ItemStack pickaxe) {
        Long level = pickaxe.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.LONG);

        if (level == null) {
            return 0L;
        }
        return level;
    }

    public void setLevel(@NotNull ItemStack pickaxe, long level) {

        List<Component> lines = pickaxe.lore();
        if (lines == null) {
            rebuildLore(pickaxe);
            lines = pickaxe.lore();
        }
        pickaxe.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().set(key, PersistentDataType.LONG, level));
        assert lines != null;
        if (lines.stream().noneMatch(component -> component.toString().contains(getDisplayName()))) {
            lines.add(Component.text(getDisplayName(), getEnchantLevel().getColor())
                    .append(Component.text(" • ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(level, NamedTextColor.GRAY))
                    .decoration(TextDecoration.ITALIC, false));
            pickaxe.lore(lines);
        } else {
            rebuildLore(pickaxe);
        }
        if (getName().equals("efficiency")) {
            pickaxe.addUnsafeEnchantment(Enchantment.DIG_SPEED, (int) level);
        }
    }

    public void rebuildLore(@NotNull ItemStack pickaxe) {

        List<Component> lore = new ArrayList<>();

        lore.add(Component.text("Default Mining Tool", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text(""));
        lore.add(Component.text("Enchants", NamedTextColor.AQUA).decoration(TextDecoration.UNDERLINED, true)
                .decoration(TextDecoration.ITALIC, false));

        EnchantLevel lastLevel = EnchantLevel.ONE;

        for (PickaxeEnchants enchant : PickaxeEnchants.values()) {
            if (lastLevel != enchant.getEnchantLevel()) {
                lore.add(Component.text(" "));
                lastLevel = enchant.getEnchantLevel();
            }

            lore.add(Component.text(enchant.getDisplayName(), enchant.getEnchantLevel().getColor())
                    .append(Component.text(" • ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(enchant.getLevel(pickaxe), NamedTextColor.GRAY))
                    .decoration(TextDecoration.ITALIC, false));
        }

        pickaxe.lore(lore);
    }
}
