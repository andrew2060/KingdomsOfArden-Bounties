package edu.berkeley.andwen.swagservbounties;

import com.gmail.brandonjones1212.swagservbounties.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class CommandHandler implements CommandExecutor {

	private Bounties plugin;
	public CommandHandler(Bounties plugin) {
		this.plugin = plugin;
	}
	Player player = null;
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("bounty")){ 
			if ((sender instanceof Player)) {
					player = (Player) sender;
		        } 
				else {
					sender.sendMessage("You must be a player!");
					return false;
		        }
			if (args.length > 3) {
		           return false;
		        } 
			if (args.length < 3) {
		           return false;
		        }
			if(player.hasPermission("swagserv.bountycreate")) {
			//Handle Actual Action Here, (might want to switch to vault for better permission support)
			}
			return true;
		} //If this has happened the function will break and return true. if this hasn't happened the a value of false will be returned.
		return false; 
	}
}
