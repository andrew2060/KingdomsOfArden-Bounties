package net.swagserv.andrew2060.swagservbounties;

import net.swagserv.jones12.swagservbounties.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

//import com.massivecraft.factions.Faction;
//import com.massivecraft.factions.P;
//Command Handling for /bounty: faction bounties in FactionCommandHandler
public class CommandHandler implements CommandExecutor {
	private Bounties plugin;
	//private P factions;
	public CommandHandler(Bounties plugin) {
		this.plugin = plugin;
	}
	Player player = null;
	public String targetplayer;
	public String bountyplayer;
	//private Player senderfactionsplayer;
	//private Player targetfactionsplayer;
	//private String senderFactionID;
	//private String targetFactionID;
	public double bountyamount;
	public double killcount;
	double temp=0.00;
	double accntBalance = 0.00;
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
		if(cmd.getName().equalsIgnoreCase("bounty")){ 
			if ((sender instanceof Player)) {
					player = (Player) sender;
		        } 
				else {
					sender.sendMessage("This command cannot be run from the console!");
					return true;
		        }
			if (args.length > 5) {
		           return false;
		        } 
			if (args.length < 5) {
		           return false;
		        }
			//Function of /bounty create
			if (args[0].equalsIgnoreCase("create")){
				bountyplayer = sender.getName();
				targetplayer = args[2];
				//Check if Factions is enabled
				//if (Bounties.factionisEnabled = true);
				//Begin Factions Variable section- remove if no desire to tie in with factions//
				//Player senderfactionsplayer = Bukkit.getServer().getPlayer(sender.getName());
				//Player targetfactionsplayer = Bukkit.getServer().getPlayer(args[3]);
				//DEBUG VARIABLE CONFIRM: REMOVE IN FINAL RELEASE
				//sender.sendMessage("senderfactionsplayer ==" + senderfactionsplayer);
				//sender.sendMessage("targetfactionsplayer ==" + targetfactionsplayer);
				//
				//senderFactionID = factions.getPlayerFactionTag(Bukkit.getServer().getPlayer(sender.getName())); //Non Functional
				//targetFactionID = factions.getPlayerFactionTag(Bukkit.getServer().getPlayer(args[2])); //Non Functional
				//FACTION ID DEBUG--TO BE REMOVED--
				//sender.sendMessage("senderFactionTag ==" + senderFactionID);
				//sender.sendMessage("targetFactionTag ==" + targetFactionID);
				//
				//End Factions Variable Defining//
				double bountyamount = Integer.parseInt(args[3]); //This Needs Exception Handling for non-integer inputs
				double killcount = Integer.parseInt(args[4]);
				//For Players
				if(args[1].equalsIgnoreCase ("player")) {
					if(plugin.permission.has(sender, "swagserv.bountycreate.player")) { //Permissions Check
						//Begin checking validity of command
						//Check to ensure player exists
						if(Bukkit.getServer().getPlayer(targetplayer) == null) {
							//No need to include factions check, since this is being cancelled anyways
							sender.sendMessage("Target Player Does Not Exist or is not Online!");
							return true;
						}
					
						if(Bukkit.getServer().getPlayer(targetplayer) != null) {
							//if(InsertFactionAllianceCheckHere)
							//Begin Economy Section (Derived from com.gmail.brandonjones1212.swagservbounties.SignListener.java (deprecated))
							accntBalance = plugin.economy.getBalance(bountyplayer);
							if(accntBalance < 500+bountyamount) {
								temp = (500+bountyamount)-accntBalance;
					
								sender.sendMessage(ChatColor.YELLOW + "You do not have enough money to place a bounty on " + targetplayer + " for $" + bountyamount + " as well as the $500 bounty posting fee");
								sender.sendMessage(ChatColor.YELLOW + "You need $" + temp + " more.");
							}	
							else {
								plugin.getServer().broadcastMessage(ChatColor.YELLOW + "[Bounty]:" + ChatColor.WHITE + " " + ChatColor.AQUA + bountyplayer + ChatColor.WHITE + " placed a hit on " + ChatColor.RED + targetplayer + ChatColor.WHITE + " to be killed " + ChatColor.GOLD + killcount + ChatColor.WHITE + " times for" + ChatColor.DARK_GREEN + " $" + bountyamount + ChatColor.WHITE + ".");
								temp = 500+bountyamount;
								plugin.economy.withdrawPlayer(bountyplayer, temp);
								sender.sendMessage(ChatColor.YELLOW + "The bounty fee of $" + 500 + " as well as your bounty of" + bountyamount + " has been withdrawn from your account.");
							}
							//End Economy Section: Note that nothing actually happens as of yet 
							//} End Factions Check If
							return true;
						}
					}
				//Insert Else Statement for no perms here (or let bukkit handle it)
				}
				if(args[1].equalsIgnoreCase ("faction")) {
					if(plugin.permission.has(sender, "swagserv.bountycreate.faction")) {
						//Check if faction exists 
						
					}
				}
			}
			//If this has happened the function will break and return true. if this hasn't happened the a value of false will be returned.
		}
		return false; 
	}
}
