package colton.duckprisons.mines;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PrivateMines implements Mine {

    public static @NotNull List<PrivateMines> privateMines = new ArrayList<>();
    private final @NotNull Player owner;
    private final @NotNull List<Player> members;
    private @NotNull Material material;

    public PrivateMines(@NotNull Player owner, @NotNull List<Player> members,
                        @NotNull Material material, long apothem) {
        this.owner = owner;
        this.members = members;
        this.material = material;
        privateMines.add(this);
    }

    public boolean isMember(@NotNull Player player) {
        return (player == owner || members.contains(player));
    }

    public boolean isOwner(@NotNull Player player) {
        return (player == owner);
    }

    public List<Player> getMembers() {
        return members;
    }

    public Player getOwner() {
        return owner;
    }
}
