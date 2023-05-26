package com.fronsky.vanish.module.test.yml;

import com.fronsky.vanish.module.VanishModule;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class Messages {
    private final FileConfiguration file;

    public Messages() {
        this.file = VanishModule.getData().getMessages().get();
    }

    public boolean test() {
        boolean result = this.file.contains("version");
        if (!Objects.equals(this.file.getString("version"), "1.0")) {
            result = false;
        }
        if (!this.file.contains("player_joined_vanished")) {
            result = false;
        }
        if (!this.file.contains("player_quit_vanished")) {
            result = false;
        }
        if (!this.file.contains("player_vanished")) {
            result = false;
        }
        if (!this.file.contains("player_visible")) {
            result = false;
        }
        if (!this.file.contains("self_player_vanished")) {
            result = false;
        }
        if (!this.file.contains("self_player_visible")) {
            result = false;
        }
        if (!this.file.contains("self_joined_vanished")) {
            result = false;
        }
        if (!this.file.contains("self_vanished")) {
            result = false;
        }
        if (!this.file.contains("self_visible")) {
            result = false;
        }
        if (!this.file.contains("sound_enable")) {
            result = false;
        }
        if (!this.file.contains("sound_disable")) {
            result = false;
        }
        if (!this.file.contains("reload")) {
            result = false;
        }
        return result;
    }
}
