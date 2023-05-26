package com.fronsky.vanish.module.test.yml;

import com.fronsky.vanish.module.VanishModule;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class Config {
    private final FileConfiguration file;

    public Config() {
        this.file = VanishModule.getData().getConfig().get();
    }

    public boolean test() {
        boolean result = this.file.contains("version");
        if (!Objects.equals(this.file.getString("version"), "1.0")) {
            result = false;
        }
        if (!this.file.contains("sound_enabled")) {
            result = false;
        }
        if (!this.file.contains("disabled_actions_enabled")) {
            result = false;
        }
        return result;
    }
}
