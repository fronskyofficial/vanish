package com.fronsky.vanish;

import com.fronsky.vanish.logic.module.ModuleManager;
import com.fronsky.vanish.module.VanishModule;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private final ModuleManager<Main> moduleManager = new ModuleManager<>();

    @Override
    public void onLoad() {
        moduleManager.prepare(new VanishModule(this));
        try {
            moduleManager.load();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        try {
            moduleManager.enable();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        try {
            moduleManager.disable();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
