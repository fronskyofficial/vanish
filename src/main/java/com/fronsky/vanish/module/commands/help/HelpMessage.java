package com.fronsky.vanish.module.commands.help;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpMessage {
    public static void helpCommand(final Player player) {
        final String command = "/vanish help";
        final BaseComponent[] hoverText = new ComponentBuilder(ChatColor.WHITE + command + "\n").append("\n").append(ChatColor.GRAY + "Displays vanish command help message\n").append(ChatColor.RED + "vanish.cmd.vanish.help").create();
        final TextComponent message = new TextComponent(ChatColor.GRAY + "- " + command);
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
        player.spigot().sendMessage(message);
    }

    public static void vanishCommand(final Player player) {
        final String command = "/vanish";
        final BaseComponent[] hoverText = new ComponentBuilder(ChatColor.WHITE + command + "\n").append("\n").append(ChatColor.GRAY + "Vanish yourself\n").append(ChatColor.RED + "vanish.cmd.vanish").create();
        final TextComponent message = new TextComponent(ChatColor.GRAY + "- " + command);
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
        player.spigot().sendMessage(message);
    }

    public static void vanishOthersCommand(final Player player) {
        final String command = "/vanish <player>";
        final BaseComponent[] hoverText = new ComponentBuilder(ChatColor.WHITE + command + "\n").append("\n").append(ChatColor.GRAY + "Vanish another player\n").append(ChatColor.RED + "vanish.cmd.vanish.others").create();
        final TextComponent message = new TextComponent(ChatColor.GRAY + "- " + command);
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
        player.spigot().sendMessage(message);
    }

    public static void soundCommand(final Player player) {
        final String command = "/vanish sound [on:off]";
        final BaseComponent[] hoverText = new ComponentBuilder(ChatColor.WHITE + command + "\n").append("\n").append(ChatColor.GRAY + "Toggles sound on/off while going in vanish\n").append(ChatColor.RED + "vanish.cmd.vanish.sound").create();
        final TextComponent message = new TextComponent(ChatColor.GRAY + "- " + command);
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
        player.spigot().sendMessage(message);
    }

    public static void reloadCommand(final Player player) {
        final String command = "/vanish reload";
        final BaseComponent[] hoverText = new ComponentBuilder(ChatColor.WHITE + command + "\n").append("\n").append(ChatColor.GRAY + "Reloads all vanish plugin files\n").append(ChatColor.RED + "vanish.cmd.vanish.reload").create();
        final TextComponent message = new TextComponent(ChatColor.GRAY + "- " + command);
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
        player.spigot().sendMessage(message);
    }

    public static void infoCommand(final Player player) {
        final String command = "/vanish info";
        final BaseComponent[] hoverText = new ComponentBuilder(ChatColor.WHITE + command + "\n").append("\n").append(ChatColor.GRAY + "Displays information about this vanish version\n").append(ChatColor.RED + "vanish.cmd.vanish.info").create();
        final TextComponent message = new TextComponent(ChatColor.GRAY + "- " + command);
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
        player.spigot().sendMessage(message);
    }
}
