package colton.duckprisons.enchants;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public enum EnchantLevel {
    ONE(NamedTextColor.GREEN),
    TWO(NamedTextColor.YELLOW),
    THREE(NamedTextColor.DARK_RED);

    final TextColor color;

    EnchantLevel(TextColor color) {
        this.color = color;
    }

    public TextColor getColor() {
        return this.color;
    }
}
