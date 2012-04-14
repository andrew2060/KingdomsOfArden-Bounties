package edu.berkeley.andwen.swagservbounties;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Bounties extends JavaPlugin {
	private SignListener signListener;

	public Economy economy;
	private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }	
	public void onEnable() {
		signListener = new SignListener(this);
		setupEconomy();
		getServer().getPluginManager().registerEvents(signListener, this);
		Player[] p = getServer().getOnlinePlayers();
		String[] playerNames = new String[p.length];
		for(int i = 0; i < p.length; i++) {
		playerNames[i] = p[i].getName();
		getServer().getConsoleSender().sendMessage(playerNames[i]);
		}
	}
	

	
}
