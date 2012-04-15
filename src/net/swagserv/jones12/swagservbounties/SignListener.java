package net.swagserv.jones12.swagservbounties;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;


public class SignListener implements Listener{
	private Bounties plugin;
	
	private double bountyCashNum;
	private int killCountNum;
	private int killedCount;
	
	private String bountyCash;
	private String killCount;
	private String bountyName;
	private String requestName;
	
	private Player appPlayer;
	private Player bountyPlayer;
	private Player killer;
	private Player killed;
	private Player player;
	
	public SignListener(Bounties instance) {
		this.plugin = instance;
		
		bountyCash = "";
		killCount = "";
		bountyName = "";
		requestName = "";
		
		bountyCashNum = 0.0;
		killCountNum = 0;

	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onSignChange(SignChangeEvent event) {
		
		String[] sign;
		double accntBalance = 0.00;
		double temp = 0.00;
		appPlayer = event.getPlayer();
		accntBalance = plugin.economy.getBalance(appPlayer.getName());		
		if(!event.getLines().equals(null) && !event.getLines().equals("")) {
			sign = event.getLines();
		}
		else {
			sign = null;
		}
		
		String[] args = new String[4];
		
		for(int i = 0; i < 4; i++){
			if(sign[0].equals("[Bounty]")) {
				if(!sign[i].equals(null) && !sign[i].equals("") && accntBalance >=1000) {
					switch(i) {
					case 1:
						args[i] = sign[i];
						bountyName = args[i];
						break;
					case 2:
						args[i] = sign[i];
						bountyCash = args[i];
						bountyCashNum = Double.parseDouble(bountyCash);
						break;
					case 3:
						args[i] = sign[i];
						killCount = args[i];
						killCountNum = Integer.parseInt(killCount);
						break;
					}
					requestName = appPlayer.getName();
				}
				else if(accntBalance<1000) {
					appPlayer.sendMessage("You need at least $1000 to post a bounty.");
				}
			}
		}
		if(accntBalance<=1000+bountyCashNum) {
			temp = (1000+bountyCashNum)-accntBalance;
			
			appPlayer.sendMessage("you do not have enough to place a bounty on " + bountyName + 
					" for $" + bountyCashNum);
			appPlayer.sendMessage("You need $" + temp + " more.");
		}
		else {
			temp = 1000+bountyCashNum;
			bountyPlayer = plugin.getServer().getPlayer(bountyName);
			plugin.getServer().broadcastMessage("New Bounty Added: \n" + 
															"Kill: " + bountyName + " \n" +
															"Number of Times: " + killCount + " \n" +
															"Reward: " + bountyCash + " \n" +
															"Posted By: " + appPlayer.getName());
			plugin.economy.withdrawPlayer(appPlayer.getName(), temp);
			appPlayer.sendMessage("$" + temp + "withdrawn from your account.");
		}
	}
}

