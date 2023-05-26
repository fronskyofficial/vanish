package com.fronsky.vanish.module.data;

import com.fronsky.vanish.formats.logging.BasicLogger;
import com.fronsky.vanish.logic.file.IFile;
import com.fronsky.vanish.logic.logging.ILogger;
import com.fronsky.vanish.logic.utils.Language;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import com.fronsky.vanish.module.players.VanishPlayer;
import java.util.UUID;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class Data {
    private final Plugin plugin;
    private final IFile<FileConfiguration> messages;
    private final IFile<FileConfiguration> config;
    private final HashMap<UUID, VanishPlayer> vanishedPlayers;
    private final BossBar vanishedBossBar;
    private final Language language;
    private final ILogger logger;

    public Data(final Plugin plugin, final IFile<FileConfiguration> messages, final IFile<FileConfiguration> config) {
        this.plugin = plugin;
        this.messages = messages;
        this.config = config;
        this.vanishedPlayers = new HashMap<UUID, VanishPlayer>();
        this.vanishedBossBar = Bukkit.createBossBar("Vanish", BarColor.PURPLE, BarStyle.SOLID);
        this.language = Language.DEFAULT;
        this.logger = new BasicLogger();
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public IFile<FileConfiguration> getMessages() {
        return this.messages;
    }

    public IFile<FileConfiguration> getConfig() {
        return this.config;
    }

    public HashMap<UUID, VanishPlayer> getVanishedPlayers() {
        return this.vanishedPlayers;
    }

    public BossBar getVanishedBossBar() {
        return this.vanishedBossBar;
    }

    public Language getLanguage() {
        return this.language;
    }

    public ILogger getLogger() {
        return logger;
    }
}
