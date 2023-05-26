package com.fronsky.vanish.module.players;

import com.fronsky.vanish.logic.utils.Result;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import com.fronsky.vanish.module.VanishModule;
import com.fronsky.vanish.module.data.Data;
import com.fronsky.vanish.module.enums.VanishState;
import java.util.UUID;
import org.bukkit.entity.Player;

public class VanishPlayer {
    private final Player player;
    private final UUID uuid;
    private VanishState state;
    private final Data data;
    private final boolean disabledActionsEnabled;

    public VanishPlayer(@NonNull final Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.data = VanishModule.getData();
        this.disabledActionsEnabled = this.data.getConfig().get().getBoolean("disabledActionsEnabled");
        if (this.data.getVanishedPlayers().containsKey(this.uuid)) {
            this.state = VanishState.HIDDEN;
        }
        else {
            this.state = VanishState.VISIBLE;
        }
    }

    public static Result<VanishPlayer> getVPlayer(final UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return Result.Fail(new Exception("We were unable to find a player in the server based on the given data."));
        }
        return Result.Ok(new VanishPlayer(player));
    }

    public static Result<VanishPlayer> getVPlayer(final String name) {
        final Player player = Bukkit.getPlayer(name);
        if (player == null) {
            return Result.Fail(new Exception("We were unable to find a player in the server based on the given data."));
        }
        return Result.Ok(new VanishPlayer(player));
    }

    public void sendMessage(final String message) {
        if (message == null) {
            data.getLogger().severe("The message sent to " + this.player.getDisplayName() + " was not valid.");
            return;
        }
        this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public boolean hasPermission(final String permission) {
        return this.player.hasPermission(permission);
    }

    public boolean hide(final boolean join) {
        if (this.player == null || this.uuid == null || this.state.equals(VanishState.HIDDEN) || this.data.getVanishedPlayers().containsKey(this.uuid)) {
            return false;
        }
        final String messageKey = join ? "playerJoinedVanished" : "playerVanished";
        final String selfMessageKey = join ? "selfJoinedVanished" : "selfVanished";
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            final VanishPlayer onlineVPlayer = new VanishPlayer(onlinePlayer);
            if (onlineVPlayer.getUuid().equals(this.uuid)) {
                continue;
            }
            if (!onlineVPlayer.hasPermission("vanish.see")) {
                onlineVPlayer.getPlayer().hidePlayer(this.data.getPlugin(), this.player);
            }
            else {
                onlineVPlayer.getPlayer().showPlayer(this.data.getPlugin(), this.player);
                String message = this.data.getLanguage().getLanguage(messageKey).getMessage();
                message = message.replace("<player>", this.player.getDisplayName());
                onlineVPlayer.sendMessage(message);
            }
        }
        this.state = VanishState.HIDDEN;
        this.data.getVanishedPlayers().put(this.uuid, this);
        this.data.getVanishedBossBar().addPlayer(this.player);
        if (this.disabledActionsEnabled) {
            this.player.setCollidable(false);
            this.player.setCanPickupItems(false);
        }
        this.sendMessage(this.data.getLanguage().getLanguage(selfMessageKey).getMessage());
        return true;
    }

    public boolean show(final boolean quit) {
        if (this.player == null || this.uuid == null || this.state.equals(VanishState.VISIBLE) || !this.data.getVanishedPlayers().containsKey(this.uuid)) {
            return false;
        }
        final String messageKey = quit ? "playerQuitVanished" : "playerVisible";
        final String selfMessageKey = "selfVisible";
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            final VanishPlayer onlineVPlayer = new VanishPlayer(onlinePlayer);
            if (onlineVPlayer.getUuid().equals(this.uuid)) {
                continue;
            }
            if (!quit) {
                onlineVPlayer.getPlayer().showPlayer(this.data.getPlugin(), this.player);
            }
            if (!onlineVPlayer.hasPermission("vanish.see")) {
                continue;
            }
            String message = this.data.getLanguage().getLanguage(messageKey).getMessage();
            message = message.replace("<player>", this.player.getDisplayName());
            onlineVPlayer.sendMessage(message);
        }
        this.state = VanishState.VISIBLE;
        this.data.getVanishedPlayers().remove(this.uuid);
        this.data.getVanishedBossBar().removePlayer(this.player);
        this.player.setCollidable(true);
        this.player.setCanPickupItems(true);
        this.sendMessage(this.data.getLanguage().getLanguage(selfMessageKey).getMessage());
        return true;
    }

    public Player getPlayer() {
        return this.player;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public VanishState getState() {
        return this.state;
    }

    public void setState(final VanishState state) {
        this.state = state;
    }
}
