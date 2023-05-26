package com.fronsky.vanish.logic.utils;

import org.bukkit.Bukkit;

public class MinecraftVersion {
    /**
     * Retrieves the current version of the server.
     *
     * @return the current version as a string.
     */
    public static String getCurrentVersion() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        String[] split = bukkitVersion.split("-");
        return split[0];
    }
}
