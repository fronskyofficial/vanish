package com.fronsky.vanish.logic.command;

import com.fronsky.vanish.formats.logging.BasicLogger;
import com.fronsky.vanish.logic.logging.ILogger;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import com.fronsky.vanish.logic.utils.Language;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandHandler implements TabCompleter, CommandExecutor, ICommandExecutor {
    private final String name;
    private final String permission;
    private final ILogger logger;
    private List<String> subcommands;
    private Player player = null;

    protected CommandHandler(String name, String permission) {
        this.name = name;
        this.permission = permission;
        logger = new BasicLogger();
        subcommands = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public void setSubcommands(List<String> subcommands) {
        this.subcommands = subcommands;
    }

    public void addSubcommand(String subcommand) {
        subcommands.add(subcommand);
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (!subcommands.isEmpty()) {
            String subcommand = getSubcommand(args);
            if (!subcommand.isEmpty() && hasPermission(player, permission + "." + subcommand)) {
                try {
                    Method method = this.getClass().getMethod(subcommand, CommandSender.class, String.class, String[].class);
                    method.invoke(this, sender, label, getSubcommandArgs(args));
                    return true;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    logger.warning("An error occurred while invoking subcommand method");
                }
            }
        }

        if (player == null || !hasPermission(player, permission)) {
            return false;
        }
        return onCommand(sender, label, args);
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String alias, @NonNull String[] args) {
        List<String> completions = new ArrayList<>();
        player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (args.length == 1) {
            subcommands.stream()
                    .filter(subcommand -> subcommand.startsWith(args[0]) && (player != null && hasPermission(player, permission + "." + subcommand)))
                    .forEach(completions::add);
        } else if (player != null) {
            String subcommand = getSubcommand(args);
            if (!subcommand.isEmpty() && hasPermission(player, permission + "." + subcommand)) {
                try {
                    Method method = this.getClass().getMethod(subcommand + "TabComplete", CommandSender.class, String.class, String[].class);
                    List<String> tabCompletions = (List<String>) method.invoke(this, sender, alias, getSubcommandArgs(args));
                    completions.addAll(tabCompletions);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    return new ArrayList<>();
                }
            }
        }

        return completions;
    }

    /**
     * Checks if the specified player has the given permission.
     *
     * @param player     the Player object to check for permission.
     * @param permission the permission string to check.
     * @return true if the player has the permission, false otherwise.
     */
    protected boolean hasPermission(Player player, String permission) {
        if (player == null) {
            return true;
        }
        if (permission.isEmpty()) {
            logger.severe("Permissions haven't been set. Make sure to initialize them correctly.");
            return false;
        }

        if (! player.hasPermission(permission)) {
            player.sendMessage(Language.NO_PERMISSION.getMessageWithColor());
            return false;
        }
        return true;
    }

    /**
     * Retrieves the subcommand from the given arguments.
     *
     * @param args the array of arguments.
     * @return the subcommand as a string.
     */
    private String getSubcommand(String[] args) {
        if (args.length == 0 || subcommands.isEmpty()) {
            return "";
        }

        for (String subcommand : subcommands) {
            if (subcommand.equalsIgnoreCase(args[0])) {
                return subcommand;
            }
        }
        return "";
    }

    /**
     * Retrieves the arguments for the subcommand from the given arguments.
     *
     * @param args the array of arguments.
     * @return the subcommand arguments as a string array.
     */
    private String[] getSubcommandArgs(String[] args) {
        if (args.length == 0 || subcommands.isEmpty()) {
            return new String[0];
        }

        String[] subcommandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subcommandArgs, 0, subcommandArgs.length);
        return subcommandArgs;
    }
}
