package colton.duckprisons.events;

import colton.duckprisons.PrisonPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        PrisonPlayer player = PrisonPlayer.getPlayer(e.getPlayer());

        if (player != null) {
            player.save();
        }
    }
}
