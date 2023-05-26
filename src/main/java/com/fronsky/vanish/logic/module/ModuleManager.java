package com.fronsky.vanish.logic.module;

import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;
import com.fronsky.vanish.logic.utils.Result;
import com.fronsky.vanish.logic.utils.Status;

import java.util.HashMap;
import java.util.Map;

public class ModuleManager<M extends JavaPlugin> {
    private final Map<Class<? extends IModule>, Module<M>> modules = new HashMap<>();
    private Status moduleStatus = Status.IDLE;

    /**
     * Prepares the given module for use.
     *
     * @param module the module to prepare.
     */
    public void prepare(@NonNull Module<M> module) {
        if (!modules.containsKey(module.getClass())) {
            modules.put(module.getClass(), module);
        }
    }

    /**
     * Loads the module.
     *
     * @throws Exception if an error occurs while loading the module.
     */
    public void load() throws Exception {
        if (!moduleStatus.equals(Status.IDLE)) {
            throw new Exception("The modules can't be loaded because the ModuleManager is not idle.");
        }

        moduleStatus = Status.LOADING;
        for (Module<M> module : modules.values()) {
            Result<String> result = module.load();
            if (!result.Success()) {
                throw result.Exception();
            }
        }
        moduleStatus = Status.LOADED;
    }

    /**
     * Enables the module.
     *
     * @throws Exception if an error occurs while enabling the module.
     */
    public void enable() throws Exception {
        if (!moduleStatus.equals(Status.LOADED)) {
            throw new Exception("The modules can't be enabled because the ModuleManager is not loaded.");
        }

        moduleStatus = Status.ENABLING;
        for (Module<M> module : modules.values()) {
            Result<String> result = module.enable();
            if (!result.Success()) {
                throw result.Exception();
            }
        }
        moduleStatus = Status.ENABLED;
    }

    /**
     * Enables the module.
     *
     * @throws Exception if an error occurs while enabling the module.
     */
    public void disable() throws Exception {
        if (!moduleStatus.equals(Status.ENABLED)) {
            throw new Exception("The modules can't be disabled because the ModuleManager is not enabled.");
        }

        moduleStatus = Status.DISABLING;
        for (Module<M> module : modules.values()) {
            Result<String> result = module.disable();
            if (!result.Success()) {
                throw result.Exception();
            }
        }
        moduleStatus = Status.DISABLED;
    }
}
