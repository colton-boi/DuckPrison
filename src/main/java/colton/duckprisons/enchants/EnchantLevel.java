package colton.duckprisons.enchants;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

public enum EnchantLevel {
    ONE(NamedTextColor.GREEN),
    TWO(NamedTextColor.YELLOW),
    THREE(NamedTextColor.DARK_RED);

    final @NotNull TextColor color;

    EnchantLevel(@NotNull TextColor color) {
        this.color = color;
    }

    public @NotNull TextColor getColor() {
        return this.color;
    }
}
