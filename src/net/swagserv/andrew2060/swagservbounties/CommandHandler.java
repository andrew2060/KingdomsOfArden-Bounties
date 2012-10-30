package net.swagserv.andrew2060.swagservbounties;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Rel;

//Command Handling for /bounty: faction bounties in FactionCommandHandler
public class CommandHandler implements CommandExecutor {
	private Bounties plugin;
	@SuppressWarnings("unused")
	private Player player = null;
	
	private String posterPlayerName;
	private String wantedPlayerName;
	private double temp = 0.00;
	private double accntBalance = 0.00;
	private double bountyamount = 0.00;
	
	private MySQLConnection sqlHandler;
  	
	public CommandHandler(Bounties plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("bounty")){ 
			if ((sender instanceof Player)) {
					player = (Player) sender;
		        } 
				else {
					sender.sendMessage("This command cannot be run from the console!");
					return true;
		        }
			switch (args.length){
				case 0: 
					sender.sendMessage(ChatColor.AQUA + "======Swagserv-Bounties Plugin======");
					sender.sendMessage(ChatColor.GRAY + "Developed By Jones12 and Andrew2060 of Minecraft Server www.swagserv.us");
					sender.sendMessage(ChatColor.GRAY + "For Help, use " + ChatColor.RED + "/bounty help");
					break;
				case 1: 
					if(args[0].equalsIgnoreCase("help")){
						sender.sendMessage(ChatColor.AQUA + "=======Swagserv-Bounties Help=======");
						sender.sendMessage(ChatColor.RED + "/bounty" + ChatColor.GRAY + " - Bounty Plugin Info");
						sender.sendMessage(ChatColor.RED + "/bounty help list" + ChatColor.GRAY + " - Help Viewing Currently Active Bounties");
						sender.sendMessage(ChatColor.RED + "/bounty help create " + ChatColor.GRAY + "- Help With Bounty Creation");
						sender.sendMessage(ChatColor.RED + "/bounty help delete " + ChatColor.GRAY + "- Help With Bounty Deletion");
						sender.sendMessage(ChatColor.RED + "/bounty reload " + ChatColor.GRAY + "- Reload Bounty Config");
						break;
					}
					if(args[0].equalsIgnoreCase("reload")){
						sender.sendMessage("Not Implemented Yet");
						break;
					}
					break;
				case 2: 
					if(args[0].equalsIgnoreCase("help")){
						if(args[1].equalsIgnoreCase("create")){
							sender.sendMessage(ChatColor.AQUA + "=======Swagserv-Bounties Help=======");
							sender.sendMessage(ChatColor.RED + "/bounty create [playername] [amount]");
							sender.sendMessage(ChatColor.GRAY + "Replace [playername] with desired target player.");
							sender.sendMessage(ChatColor.GRAY + "Replace [amount] with amount you are paying for the bounty");
							break;
						}
						if(args[1].equalsIgnoreCase("list")) {
							sender.sendMessage(ChatColor.RED + "/bounty list [threshold]" + ChatColor.GRAY + " - Lists all currently active bounties with a payout higher than the threshold");
						}
						if(args[1].equalsIgnoreCase("delete")){
							sender.sendMessage(ChatColor.RED + "/bounty delete [BountyID]" + ChatColor.GRAY + " - Delete Bounty of ID [BountyID]");
							break;
						}
					}
					if(args[0].equalsIgnoreCase("list")) {
						double lowerbound = Double.parseDouble(args[1]);
						try {
							ResultSet list = sqlHandler.executeQuery("SELECT * FROM bountiesplayer WHERE amount >='" + lowerbound + "'", false);
							while(list.next()) {
								String target = list.getString("target");
								Double amount = list.getDouble("amount");
								if(amount <= 0) {
									continue;
								}
								sender.sendMessage(target + " - " + ChatColor.GOLD + "$" + amount);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
					}
					break;
				case 3: 
					if(args[0].equalsIgnoreCase("delete")){
						sender.sendMessage("Not Implemented Yet");
					}
					if (args[0].equalsIgnoreCase("create")){
						bountyamount = Double.parseDouble(args[3]);
						wantedPlayerName = args[2];
						posterPlayerName = sender.getName();				
						//For Players
						if(args[1].equalsIgnoreCase ("player")) {
							if(plugin.permission.has(sender, "bounties.create")) { //Permissions Check
								//Begin checking validity of command
								//Check to ensure player exists
								if(Bukkit.getServer().getPlayer(wantedPlayerName) == null) {
									sender.sendMessage("Target Player Does Not Exist or is not online!");
								return true;
								}
								else if(Bukkit.getServer().getPlayer(wantedPlayerName) != null) {
									FPlayer fpWanted = FPlayers.i.get(Bukkit.getServer().getPlayer(wantedPlayerName));
									FPlayer fpPoster = FPlayers.i.get(Bukkit.getServer().getPlayer(sender.getName()));
									Faction fWanted = fpWanted.getFaction();
									Faction fPoster = fpPoster.getFaction();
									if(fWanted.getRelationTo(fPoster).equals(Rel.ALLY) || fWanted.getRelationTo(fPoster).equals(Rel.TRUCE)) {
										sender.sendMessage(ChatColor.GRAY + "You cannot create a bounty on an ally/truced faction member!");
										return true;
									} else if (fWanted == fPoster) {
										sender.sendMessage(ChatColor.GRAY + "You cannot create a bounty on your own factionmates! How absolutely rude!");
										return true;
									} else {
										accntBalance = plugin.economy.getBalance(posterPlayerName);
										if(accntBalance<500+bountyamount) {
											temp = (500+bountyamount)-accntBalance;
											sender.sendMessage(ChatColor.YELLOW + "You do not have enough money to place a bounty on the player " + 
													ChatColor.RED + wantedPlayerName + ChatColor.YELLOW + " for" + ChatColor.GREEN + " $" + bountyamount + 
													ChatColor.YELLOW + " in addition to the"+ ChatColor.GOLD + " $500" + ChatColor.YELLOW + " bounty posting fee");								
											sender.sendMessage(ChatColor.YELLOW + "You need $" + temp + " more.");
										}
										else {
											plugin.getServer().broadcastMessage(ChatColor.BLUE + "[Bounty]: " + 
													ChatColor.AQUA + posterPlayerName + 
													ChatColor.WHITE + " placed a hit on " + ChatColor.RED + wantedPlayerName + 
													ChatColor.WHITE + " to be killed for" + ChatColor.DARK_GREEN + " $" + 
													bountyamount + ChatColor.GRAY + ".");
											temp = 500+bountyamount;
											plugin.economy.withdrawPlayer(wantedPlayerName, temp);
											sender.sendMessage(ChatColor.YELLOW + "The bounty fee of " + ChatColor.RED + "$" + 500 +
													ChatColor.YELLOW + " and your bounty of " + ChatColor.RED + "$" + bountyamount + 
													ChatColor.YELLOW + " has been withdrawn from your account.");
											if (plugin.MySQL = true) {
												if(checkExists(wantedPlayerName)) {
													String checkExists = "SELECT * FROM bountiesplayer WHERE target = '"+wantedPlayerName + "'";
													try {
														ResultSet rs = sqlHandler.executeQuery(checkExists, false);
														rs.last();
														int id = rs.getInt("id");
														double currentbounty = rs.getDouble("amount");
														double newbounty = currentbounty + bountyamount;
														String update = "UPDATE bountiesplayer SET amount='" + newbounty + "' WHERE id='"+id+"'";
														sqlHandler.executeQuery(update, true);
													} catch (SQLException e){
														e.printStackTrace();
													}
												} else {
								 					String query = "INSERT INTO bountiesplayer (target,amount) VALUES ('" 
								 							+ wantedPlayerName + "', '" + bountyamount + "')";
								 					try {
								 						sqlHandler.executeQuery(query, true);
								 					} catch (SQLException e) {
								 						e.printStackTrace();
								 					}
												}
											}
										}
	
										//End Economy Section: Note that nothing actually happens as of yet 
									} //End Factions Check If
								}
							}
							//Insert Else Statement for no perms here (or let bukkit handle it)
						}
					}
					break;
				default:
					sender.sendMessage(ChatColor.AQUA + "======Swagserv-Bounties Plugin======");
					sender.sendMessage(ChatColor.RED + "Invalid Command");
					sender.sendMessage(ChatColor.GRAY + "For Help, use " + ChatColor.RED + "/bounty help");
					break;
			}
		}
        return true;
    }
	public boolean checkExists(String wantedPlayerName) {
		String checkExists = "SELECT * FROM bountiesplayer WHERE target = '"+wantedPlayerName + "'";
		try {
			ResultSet checkResult = sqlHandler.executeQuery(checkExists, false);
			if(checkResult.last()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
				e.printStackTrace();
		}
		return false;
		
	}
}

