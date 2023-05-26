package com.fronsky.vanish.module.players;

import org.bukkit.Location;
import org.bukkit.GameMode;
import java.util.UUID;

public class PlayerState {
    private final UUID uuid;
    private final GameMode gameMode;
    private final boolean allowFlying;
    private final boolean isFlying;
    private final boolean isSneaking;
    private final Location location;

    public PlayerState(final VanishPlayer vplayer) {
        this.uuid = vplayer.getUuid();
        this.gameMode = vplayer.getPlayer().getGameMode();
        this.allowFlying = vplayer.getPlayer().getAllowFlight();
        this.isFlying = vplayer.getPlayer().isFlying();
        this.isSneaking = vplayer.getPlayer().isSneaking();
        this.location = vplayer.getPlayer().getLocation();
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public GameMode getGameMode() {
        return this.gameMode;
    }

    public boolean isAllowFlying() {
        return this.allowFlying;
    }

    public boolean isFlying() {
        return this.isFlying;
    }

    public boolean isSneaking() {
        return this.isSneaking;
    }

    public Location getLocation() {
        return this.location;
    }
}
