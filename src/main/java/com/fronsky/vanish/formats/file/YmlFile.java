package com.fronsky.vanish.formats.file;

import com.fronsky.vanish.formats.logging.BasicLogger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import com.fronsky.vanish.logic.file.IFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class YmlFile implements IFile<FileConfiguration> {
    private final Plugin plugin;
    private final String fileName;
    private final BasicLogger logger = new BasicLogger();
    private FileConfiguration configuration = null;
    private File file = null;

    public YmlFile(String fileName, Plugin plugin) {
        this.fileName = fileName + ".yml";
        this.plugin = plugin;

        saveDefaultConfig();
    }

    /**
     * Saves the default configuration file if it does not exist.
     */
    private void saveDefaultConfig() {
        if (this.file == null) {
            this.file = new File(plugin.getDataFolder(), fileName);
        }

        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
    }

    @Override
    public boolean load() {
        if (this.file == null) {
            this.file = new File(plugin.getDataFolder(), fileName);
        }

        if (!file.exists()) {
            return false;
        }

        this.configuration = YamlConfiguration.loadConfiguration(file);
        return true;
    }

    @Override
    public void save() {
        if (this.configuration == null || this.file == null) {
            return;
        }

        try {
            configuration.save(file);
        } catch (IOException e) {
            logger.severe(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void reload() {
        if (this.file == null) {
            this.file = new File(plugin.getDataFolder(), fileName);
        }

        this.configuration = YamlConfiguration.loadConfiguration(file);

        this.configuration = YamlConfiguration.loadConfiguration(this.file);
        final InputStream stream = this.plugin.getResource(this.fileName);
        if (stream != null) {
            final InputStreamReader streamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            final YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(streamReader);
            this.configuration.setDefaults(yamlConfiguration);
        }
    }

    @Override
    public FileConfiguration get() {
        if (this.configuration == null) {
            reload();
        }

        return configuration;
    }
}
