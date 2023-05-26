package com.fronsky.vanish.module.events;

import org.bukkit.event.Listener;
import com.fronsky.vanish.module.players.VanishPlayer;
import org.bukkit.event.player.PlayerJoinEvent;
import com.fronsky.vanish.module.VanishModule;
import com.fronsky.vanish.module.data.Data;

public class Join implements Listener {
    private final Data data;

    public Join() {
        this.data = VanishModule.getData();
    }

    @org.bukkit.event.EventHandler
    public void playerJoinEvent(final PlayerJoinEvent event) {
        final VanishPlayer player = new VanishPlayer(event.getPlayer());
        if (!player.hasPermission("vanish.join")) {
            if (!player.hasPermission("vanish.see")) {
                for (final VanishPlayer vanishedPlayer : this.data.getVanishedPlayers().values()) {
                    player.getPlayer().hidePlayer(this.data.getPlugin(), vanishedPlayer.getPlayer());
                }
            }
            return;
        }
        event.setJoinMessage("");
        if (!this.data.getVanishedPlayers().containsKey(player.getUuid())) {
            player.hide(true);
        }
    }
}
