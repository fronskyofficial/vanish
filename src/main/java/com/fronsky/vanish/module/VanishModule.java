package com.fronsky.vanish.module;

import com.fronsky.vanish.Main;
import com.fronsky.vanish.formats.file.YmlFile;
import com.fronsky.vanish.logic.file.IFile;
import com.fronsky.vanish.logic.module.Module;
import com.fronsky.vanish.module.commands.Vanish;
import com.fronsky.vanish.module.data.Data;
import com.fronsky.vanish.module.enums.VanishState;
import com.fronsky.vanish.module.events.DisabledActions;
import com.fronsky.vanish.module.events.Join;
import com.fronsky.vanish.module.events.Quit;
import com.fronsky.vanish.module.test.Test;
import org.bukkit.configuration.file.FileConfiguration;
import com.fronsky.vanish.module.players.VanishPlayer;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

public class VanishModule extends Module<Main> {
    private static Data data;
    private final boolean testResult;

    public VanishModule(Main mainClass) {
        super(mainClass);
        final IFile<FileConfiguration> message = new YmlFile("messages", mainClass);
        final IFile<FileConfiguration> config = new YmlFile("config", mainClass);
        setData(new Data(mainClass, message, config));
        if (!(testResult = Test.executeTests())) {
            data.getLogger().severe("An error has occurred in a file. To resolve the issue, please delete all files and restart or reload the server.");
        }
    }

    public static Data getData() {
        return VanishModule.data;
    }

    public static void setData(final Data data) {
        VanishModule.data = data;
    }

    @Override
    public void onLoad() {}

    @Override
    public void onEnable() {
        if (!testResult) {
            return;
        }
        command(Vanish::new);
        event(DisabledActions::new);
        event(Join::new);
        event(Quit::new);
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            final VanishPlayer vplayer = new VanishPlayer(onlinePlayer);
            if (!vplayer.hasPermission("vanish.join")) {
                continue;
            }
            vplayer.setState(VanishState.HIDDEN);
            VanishModule.data.getVanishedPlayers().put(vplayer.getUuid(), vplayer);
            VanishModule.data.getVanishedBossBar().addPlayer(vplayer.getPlayer());
            vplayer.getPlayer().setCollidable(false);
            vplayer.getPlayer().setCanPickupItems(false);
            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (!player.hasPermission("vanish.see")) {
                    player.hidePlayer(VanishModule.data.getPlugin(), vplayer.getPlayer());
                }
            }
        }
    }

    @Override
    public void onDisable() {
        if (!this.testResult) {
            return;
        }
        VanishModule.data.getVanishedBossBar().setVisible(false);
        VanishModule.data.getVanishedBossBar().removeAll();
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            for (final VanishPlayer vplayer : VanishModule.data.getVanishedPlayers().values()) {
                if (!vplayer.hasPermission("vanish.join")) {
                    onlinePlayer.showPlayer(VanishModule.data.getPlugin(), vplayer.getPlayer());
                }
            }
        }
        for (final VanishPlayer vplayer2 : VanishModule.data.getVanishedPlayers().values()) {
            vplayer2.setState(VanishState.VISIBLE);
            VanishModule.data.getVanishedPlayers().remove(vplayer2.getUuid());
            VanishModule.data.getVanishedBossBar().removePlayer(vplayer2.getPlayer());
            vplayer2.getPlayer().setCollidable(true);
            vplayer2.getPlayer().setCanPickupItems(true);
        }
        VanishModule.data.getVanishedPlayers().clear();
    }
}
