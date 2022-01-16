package me.dkflab.influence;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

public class Utils {

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&',s);
    }

    public static void error(CommandSender se, String s) {
        sendMessage(se, "&c&lError! &7" + s);
    }

    public static void success(CommandSender se, String s) {
        sendMessage(se, "&a&lSuccess! &7" + s);
    }

    public static void notAPlayer(CommandSender s) {
        error(s, "You need to be a player to execute that command!");
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }
}
