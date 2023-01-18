package me.colton.duckprisons.events;

import me.colton.duckprisons.PrisonPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent e) {
        PrisonPlayer.getPlayer(e.getPlayer()); // Creates new PrisonPlayer
    }
}
