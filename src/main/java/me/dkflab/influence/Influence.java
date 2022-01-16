package me.dkflab.influence;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class Influence extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerCommands();
        registerListeners();
    }

    private void registerCommands() {
        new InfluenceCommand(this);
    }

    private void registerListeners() {

    }

    public int getBalance(UUID uuid) {
        return getConfig().getInt("balances." + uuid.toString());
    }

    public void setBalance(UUID uuid, int balance) {
        getConfig().set("balances." + uuid.toString(), balance);
        saveConfig();
    }

    public void addBalance(UUID uuid, int amountToBeAdded) {
        setBalance(uuid, getBalance(uuid)+amountToBeAdded);
    }

    public int getPriceOfFaction() {
        return getConfig().getInt("costForFaction");
    }

    public String getCommandForFaction() {
        return getConfig().getString("command");
    }
}
