package me.colton.duckprisons.events;

import me.colton.duckprisons.PrisonPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent e) {
        PrisonPlayer.getOfflinePlayer(e.getPlayer()).save();
    }
}
