package com.fronsky.vanish.module.events;

import org.bukkit.event.Listener;
import com.fronsky.vanish.module.players.VanishPlayer;
import org.bukkit.event.player.PlayerQuitEvent;
import com.fronsky.vanish.module.VanishModule;
import com.fronsky.vanish.module.data.Data;

public class Quit implements Listener {
    private final Data data;

    public Quit() {
        this.data = VanishModule.getData();
    }

    @org.bukkit.event.EventHandler
    public void playerQuitEvent(final PlayerQuitEvent event) {
        final VanishPlayer player = new VanishPlayer(event.getPlayer());
        if (this.data.getVanishedPlayers().containsKey(player.getUuid())) {
            event.setQuitMessage("");
            player.show(true);
        }
    }
}
