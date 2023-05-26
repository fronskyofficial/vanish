package com.fronsky.vanish.logic.module;

import com.fronsky.vanish.formats.logging.BasicLogger;
import com.fronsky.vanish.logic.command.CommandHandler;
import com.fronsky.vanish.logic.logging.ILogger;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import com.fronsky.vanish.logic.task.ITask;
import com.fronsky.vanish.logic.utils.Result;
import com.fronsky.vanish.logic.utils.Status;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class Module<M extends JavaPlugin> implements IModule {
    private final M mainClass;
    private final String moduleName;
    private final List<Listener> events;
    private final List<CommandHandler> commands;
    private final List<BukkitTask> tasks;
    private final CommandMap commandMap;
    private final ILogger logger;
    private Status moduleStatus;

    protected Module(M mainClass) {
        this.mainClass = mainClass;
        moduleName = this.getClass().getSimpleName();
        logger = new BasicLogger();

        CommandMap tempCommandMap = null;
        try {
            tempCommandMap = (CommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            logger.severe(e.getMessage());
        }

        if (tempCommandMap == null) {
            moduleStatus = Status.DISABLED;
            Bukkit.shutdown();
        }

        commandMap = tempCommandMap;
        events = new ArrayList<>();
        commands = new ArrayList<>();
        tasks = new ArrayList<>();
        moduleStatus = Status.IDLE;
    }

    /**
     * Loads the module and returns the result.
     *
     * @return The result of the load operation.
     */
    public Result<String> load() {
        if (!moduleStatus.equals(Status.IDLE)) {
            return new Result<>(null, new Exception("An attempt was made to load the " + moduleName + " while it was not idle."));
        }

        moduleStatus = Status.LOADING;
        logger.info("Loading " + moduleName + "...");
        onLoad();
        moduleStatus = Status.LOADED;
        return new Result<>("Module has been successfully loaded.", null);
    }

    /**
     * Enables the module and returns the result.
     *
     * @return The result of the enable operation.
     */
    public Result<String> enable() {
        if (!moduleStatus.equals(Status.LOADED)) {
            return new Result<>(null, new Exception("An attempt was made to enable the " + moduleName + " while it was not loaded."));
        }

        moduleStatus = Status.ENABLING;
        logger.info("Enabling " + moduleName + "...");
        try {
            onEnable();
            moduleStatus = Status.ENABLED;
            return new Result<>("Module has been successfully enabled.", null);
        } catch (Exception e) {
            moduleStatus = Status.DISABLING;
            logger.severe(e.getMessage());
            Bukkit.shutdown();
            moduleStatus = Status.DISABLED;
            return new Result<>(null, e);
        }
    }

    /**
     * Disables the module and returns the result.
     *
     * @return The result of the disable operation.
     */
    public Result<String> disable() {
        if (!moduleStatus.equals(Status.ENABLED)) {
            return new Result<>(null, new Exception("An attempt was made to disable the " + moduleName + " while it was not enabled."));
        }

        moduleStatus = Status.DISABLING;
        int amountOfComponents = events.size() + commands.size();
        logger.info("Disabling " + moduleName + ", removing " + amountOfComponents + " components...");

        events.forEach(HandlerList::unregisterAll);
        for (CommandHandler commandHandler : commands) {
            PluginCommand pluginCommand = mainClass.getCommand(commandHandler.getName());
            if (pluginCommand != null) {
                pluginCommand.unregister(commandMap);
            }
        }

        events.clear();
        commands.clear();
        tasks.clear();
        onDisable();
        moduleStatus = Status.DISABLED;
        return new Result<>("Module has been successfully disabled.", null);
    }

    /**
     * Registers an event listener supplied by the given supplier.
     *
     * @param supplier the supplier of the event listener.
     * @throws RuntimeException if the module is not in the enabling state.
     */
    protected void event(@NonNull Supplier<? extends Listener> supplier) {
        if (!moduleStatus.equals(Status.ENABLING)) {
            throw new RuntimeException("The " + moduleName + " is not enabling.");
        }

        Listener listener = supplier.get();
        Bukkit.getServer().getPluginManager().registerEvents(listener, mainClass);
        events.add(listener);
    }

    /**
     * Registers a command handler supplied by the given supplier.
     *
     * @param supplier the supplier of the command handler.
     * @throws RuntimeException if the module is not in the enabling state or if the plugin command is null.
     */
    protected void command(@NonNull Supplier<? extends CommandHandler> supplier) {
        if (!moduleStatus.equals(Status.ENABLING)) {
            throw new RuntimeException("The " + moduleName + " is not enabling.");
        }

        CommandHandler commandHandler = supplier.get();
        PluginCommand pluginCommand = mainClass.getCommand(commandHandler.getName());
        if (pluginCommand == null) {
            throw new RuntimeException("The plugin command is null.");
        }

        pluginCommand.setExecutor(commandHandler);
        pluginCommand.setTabCompleter(commandHandler);
        commands.add(commandHandler);
    }

    /**
     * Schedules a task provided by the given supplier.
     *
     * @param supplier the supplier of the task.
     * @throws RuntimeException if the module is not in the enabling state.
     */
    protected void task(@NonNull Supplier<? extends ITask> supplier) {
        if (!moduleStatus.equals(Status.ENABLING)) {
            throw new RuntimeException("The module is not enabling.");
        }

        ITask task = supplier.get();
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                task.run();

                if (task.getPeriod() == 0) {
                    tasks.remove(getTaskId());
                    cancel();
                }
            }
        };

        long delay = task.getDelay();
        long period = task.getPeriod();
        boolean isAsync = task.isAsync();

        BukkitTask bukkitTask;
        if (period == 0) {
            if (isAsync) {
                bukkitTask = runnable.runTaskAsynchronously(mainClass);
            } else {
                bukkitTask = runnable.runTask(mainClass);
            }
        } else {
            if (isAsync) {
                bukkitTask = runnable.runTaskTimerAsynchronously(mainClass, delay, period);
            } else {
                bukkitTask = runnable.runTaskTimer(mainClass, delay, period);
            }
        }

        tasks.add(bukkitTask);
    }
}
