package colton.duckprisons.enchants;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public interface Enchant {
    /**
     * Attempt to trigger an enchant from an event.
     *
     * Cases:
     * Player is not holding the correct tool: false
     * Tool doesn't have the enchant: false
     * Event is cancelled: false
     * Else: true
     *
     * @param e     Event
     * @return      Whether the enchant was successfully used.
     */
    boolean use(@NotNull Event e);
}
