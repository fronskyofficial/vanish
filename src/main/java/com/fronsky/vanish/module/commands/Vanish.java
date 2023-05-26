package com.fronsky.vanish.module.commands;

import com.fronsky.vanish.logic.command.CommandHandler;
import com.fronsky.vanish.logic.utils.Result;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import com.fronsky.vanish.module.commands.help.HelpMessage;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import com.fronsky.vanish.module.players.VanishPlayer;
import org.bukkit.entity.Player;
import com.fronsky.vanish.module.VanishModule;
import java.util.Arrays;
import com.fronsky.vanish.module.data.Data;

public class Vanish extends CommandHandler {
    private final Data data;
    private boolean soundEnabled;

    public Vanish() {
        super("vanish", "vanish.cmd.vanish");
        this.setSubcommands(Arrays.asList("help", "sound", "reload", "info"));
        this.data = VanishModule.getData();
        this.soundEnabled = this.data.getConfig().get().getBoolean("sound_enabled");
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull String label, @NonNull String[] args) {
        VanishPlayer vplayer = null;
        if (sender instanceof Player) {
            vplayer = new VanishPlayer((Player) sender);
        }
        if (args.length > 0) {
            final Result<VanishPlayer> result = VanishPlayer.getVPlayer(args[0]);
            if (result.Success()) {
                final VanishPlayer target = result.Value();
                if (vplayer != null && vplayer.hasPermission("vanish.cmd.vanish.others")) {
                    return true;
                }
                this.others(sender, target, label, args);
                return true;
            }
        }
        if (vplayer == null) {
            data.getLogger().severe("This command can only be executed by players.");
            return true;
        }
        if (this.data.getVanishedPlayers().containsKey(vplayer.getUuid())) {
            vplayer.show(false);
        }
        else {
            vplayer.hide(false);
            if (this.soundEnabled) {
                vplayer.getPlayer().playSound(vplayer.getPlayer().getLocation(), Sound.AMBIENT_CAVE, 1.0f, 1.0f);
            }
        }
        return true;
    }

    private void others(@NonNull final CommandSender sender, @NonNull final VanishPlayer target, @NonNull final String commandLabel, @NonNull final String[] args) {
        final boolean isVanished = this.data.getVanishedPlayers().containsKey(target.getUuid());
        final String messageKey = isVanished ? "self_player_visible" : "self_player_vanished";
        String message = this.data.getLanguage().getLanguage(messageKey).getMessage();
        message = message.replace("<player>", target.getPlayer().getDisplayName());
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        if (isVanished) {
            target.show(false);
        }
        else {
            target.hide(false);
            if (this.soundEnabled) {
                target.getPlayer().playSound(target.getPlayer().getLocation(), Sound.AMBIENT_CAVE, 1.0f, 1.0f);
            }
        }
    }

    public void help(@NonNull final CommandSender sender, @NonNull final String commandLabel, @NonNull final String[] args) {
        sender.sendMessage(ChatColor.WHITE + "<---------------" + ChatColor.DARK_PURPLE + "Vanish Help" + ChatColor.WHITE + "--------------->");
        sender.sendMessage(ChatColor.YELLOW + "Aliases: " + ChatColor.GRAY + "/v");
        sender.sendMessage(ChatColor.YELLOW + "Commands: ");
        if (sender instanceof Player) {
            final Player player = (Player)sender;
            HelpMessage.helpCommand(player);
            HelpMessage.vanishCommand(player);
            HelpMessage.vanishOthersCommand(player);
            HelpMessage.soundCommand(player);
            HelpMessage.reloadCommand(player);
            HelpMessage.infoCommand(player);
        }
        else {
            sender.sendMessage(ChatColor.GRAY + "- /vanish help");
            sender.sendMessage(ChatColor.GRAY + "- /vanish");
            sender.sendMessage(ChatColor.GRAY + "- /vanish <player>");
            sender.sendMessage(ChatColor.GRAY + "- /vanish sound [on:off]");
            sender.sendMessage(ChatColor.GRAY + "- /vanish reload");
            sender.sendMessage(ChatColor.GRAY + "- /vanish info");
        }
        sender.sendMessage(ChatColor.WHITE + "<---------------------------------------->");
    }

    public void sound(@NonNull final CommandSender sender, @NonNull final String commandLabel, @NonNull final String[] args) {
        final String argsError = ChatColor.RED + "Please provide the required argument [on or off] before running this command.";
        if (args.length == 0) {
            sender.sendMessage(argsError);
            return;
        }
        String message;
        if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("true")) {
            this.soundEnabled = true;
            message = this.data.getLanguage().getLanguage("sound_enable").getMessage();
        }
        else {
            if (!args[0].equalsIgnoreCase("off") && !args[0].equalsIgnoreCase("false")) {
                sender.sendMessage(argsError);
                return;
            }
            this.soundEnabled = false;
            message = this.data.getLanguage().getLanguage("sound_disable").getMessage();
        }
        this.data.getConfig().get().set("sound_enabled", (Object)this.soundEnabled);
        this.data.getMessages().save();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void reload(@NonNull final CommandSender sender, @NonNull final String commandLabel, @NonNull final String[] args) {
        this.data.getMessages().reload();
        this.data.getConfig().reload();
        this.soundEnabled = this.data.getConfig().get().getBoolean("sound_enabled");
        final String message = this.data.getLanguage().getLanguage("reload").getMessage();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void info(@NonNull final CommandSender sender, @NonNull final String commandLabel, @NonNull final String[] args) {
        sender.sendMessage(ChatColor.WHITE + "<---------------" + ChatColor.DARK_PURPLE + "Vanish Info" + ChatColor.WHITE + "--------------->");
        sender.sendMessage(ChatColor.YELLOW + "Name: " + ChatColor.WHITE + this.data.getPlugin().getDescription().getName());
        sender.sendMessage(ChatColor.YELLOW + "Author: " + ChatColor.GRAY + "Fronsky Inc");
        if (sender instanceof Player) {
            final Player player = (Player)sender;
            final TextComponent plugin = new TextComponent(ChatColor.YELLOW + "Plugin: " + ChatColor.GRAY + "www.fronsky.com/resources/vanish");
            plugin.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.fronsky.com/resources/vanish"));
            final TextComponent website = new TextComponent(ChatColor.YELLOW + "Website: " + ChatColor.GRAY + "www.fronsky.com");
            website.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.fronsky.com"));
            player.spigot().sendMessage(plugin);
            player.spigot().sendMessage(website);
        }
        else {
            sender.sendMessage(ChatColor.YELLOW + "Plugin: " + ChatColor.GRAY + "www.fronsky.com/resources/vanish");
            sender.sendMessage(ChatColor.YELLOW + "Website: " + ChatColor.GRAY + "www.fronsky.com");
        }
        sender.sendMessage(ChatColor.YELLOW + "Version: " + ChatColor.RED + this.data.getPlugin().getDescription().getVersion());
        sender.sendMessage(ChatColor.WHITE + "<---------------------------------------->");
    }
}
