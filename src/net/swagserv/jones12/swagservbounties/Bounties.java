package net.swagserv.jones12.swagservbounties;
//Class Imports
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy; //Vault Economy Support
import net.milkbowl.vault.permission.Permission; //Vault Permissions Support
import net.swagserv.andrew2060.swagservbounties.CommandHandler; //Command Handler for /bounty
//Begin Bukkit Class Imports
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
//End Bukkit Class Imports


@SuppressWarnings("unused")
public class Bounties extends JavaPlugin {
	Logger log;
	private CommandHandler commandHandler;
	public Economy economy;
	public Permission permission;
    public boolean factionisEnabled = false;
    //Begin External Plugin Detection Setup
	private void setupFactions()
	{
		Plugin factions = getServer().getPluginManager().getPlugin("Factions");
        if (factions != null) {            
        	this.factionisEnabled = true;
        	log.info("Successfully Hooked Into Factions");
        }
	}
	private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
            log.info("Economy Plugin Hooked Through Vault");
        }

        return (economy != null);
    }	
	private Boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
            log.info("Permissions Plugin Hooked Through Vault");
        }
        return (permission != null);
    }
	public void onEnable() {
		log = this.getLogger();
		//Enable Status Logging
		log.info("Initializing...");
		//Initialize Vault Hooks
		setupEconomy();
		setupPermissions();
		setupFactions();
		//Load Config.yml
		getConfig();
		commandHandler = new CommandHandler(this);
		getCommand("bounty").setExecutor(commandHandler);
		log.info("Plugin Hooks Successful");
		//-----------------//
	}
}
