package colton.duckprisons.events;

import colton.duckprisons.PrisonPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (PrisonPlayer.getPlayer(e.getPlayer()) == null) {
            new PrisonPlayer(e.getPlayer());
        }
    }
}
