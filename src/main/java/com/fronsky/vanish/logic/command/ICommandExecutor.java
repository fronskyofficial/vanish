package com.fronsky.vanish.logic.command;

import lombok.NonNull;
import org.bukkit.command.CommandSender;

public interface ICommandExecutor {
    boolean onCommand(@NonNull CommandSender sender, @NonNull String label, @NonNull String[] args);
}
