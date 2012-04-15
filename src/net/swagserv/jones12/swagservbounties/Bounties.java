package net.swagserv.jones12.swagservbounties;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.swagserv.andrew2060.swagservbounties.CommandHandler;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.swagserv.andrew2060.swagservbounties.*;


public class Bounties extends JavaPlugin {
	private CommandHandler commandHandler;
	public Economy economy;
	public Permission permission;
	private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }	
	private Boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
	public void onEnable() {
		setupEconomy();
		setupPermissions();
		commandHandler = new CommandHandler(this);
		getCommand("bounty").setExecutor(commandHandler);
		Player[] p = getServer().getOnlinePlayers();
		String[] playerNames = new String[p.length];
		for(int i = 0; i < p.length; i++) {
		playerNames[i] = p[i].getName();
		getServer().getConsoleSender().sendMessage(playerNames[i]);
		//-----------------//
		}
	}
	

	
}
