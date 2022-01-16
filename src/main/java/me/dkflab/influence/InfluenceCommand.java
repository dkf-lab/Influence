package me.dkflab.influence;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static me.dkflab.influence.Utils.*;

public class InfluenceCommand implements CommandExecutor, TabCompleter {

    private Influence main;
    public InfluenceCommand(Influence main) {
        this.main = main;
        main.getCommand("influence").setExecutor(this);
        main.getCommand("influence").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                balance((Player)sender);
            } else {
                help(sender);
            }
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("balance")) {
                if (!(sender instanceof Player)) {
                    notAPlayer(sender);
                    return true;
                }
                Player p = (Player)sender;
                balance(p);
                return true;
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                if (!(sender instanceof Player)) {
                    notAPlayer(sender);
                    return true;
                }
                Player p = (Player) sender;
                if (main.getBalance(p.getUniqueId()) >= main.getPriceOfFaction()) {
                    p.setOp(true);
                    p.performCommand(main.getCommandForFaction().replace("%faction%",args[1]));
                    p.setOp(false);
                    main.setBalance(p.getUniqueId(),main.getBalance(p.getUniqueId())-main.getPriceOfFaction());
                    success(p, "Faction has been successfully created.");
                } else {
                    sendMessage(sender, "&7You need at least &c" + main.getPriceOfFaction() + " influence&7.");
                }
                return true;
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("give")) {
                if (!sender.hasPermission("influence.give")) {
                    error(sender, "You need permission &einfluence.give &7to access this command.");
                    return true;
                }
                Player p = null;
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (all.getName().equalsIgnoreCase(args[1])) {
                        p = all;
                    }
                }
                if (p == null) {
                    error(sender, "&e" + args[1] +"&7 is not a player.");
                    return true;
                }
                int amount = 0;
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    error(sender, "&e" + args[2] + "&7 is not an integer.");
                    return true;
                }
                main.setBalance(p.getUniqueId(), main.getBalance(p.getUniqueId()) + amount);
                success(sender, "Gave player &e" + p.getName()+ ' ' + amount + "influence&7!");
                return true;
            }
        }
        help(sender);
        return true;
    }

    private void balance(Player p) {
        sendMessage(p, "&7Your balance is &a" + main.getBalance(p.getUniqueId()) + " Influence&7!");

    }

    private void help(CommandSender s) {
        sendMessage(s, "&d&lInfluence Help");
        if (s.hasPermission("influence.give")) {
            sendMessage(s, "&8/influence &egive <player> <amount> &7- Give a player influence");
        }
        sendMessage(s, "&8/influence &ebalance &7- View your balance");
        sendMessage(s, "&8/influence &ecreate <factionName> &7- Create a faction using influence");
    }

    private List<String> arguments = new ArrayList<>();
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        List<String> result = new ArrayList<>();
        if (arguments.isEmpty()) {
            arguments.add("balance");
            arguments.add("create");
            arguments.add("give");
        }
        if (args.length == 1) {
            for (String a : arguments) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
                    result.add(a);
                }
            }
            return result;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("give")) {
                result.add("<player>");
            }
            if (args[0].equalsIgnoreCase("create")) {
                result.add("<factionName>");
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("give")) {
                result.add("<amount>");
            }
        }
        return result;
    }
}
