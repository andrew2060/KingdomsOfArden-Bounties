package net.swagserv.andrew2060.swagservbounties;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathHandler implements Listener {
	private MySQLConnection sqlHandler;
	Bounties plugin;
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPLayerDeath(PlayerDeathEvent event) throws SQLException {
		Player p = event.getEntity();
		if(p.getKiller() == null) {
			return;
		}
		if(!checkExists(p.getName())) {
			return;
		}
		Player k = p.getKiller();
		String getResult = "Select * FROM bountiesplayer WHERE target = '" + p.getName() + "'";
		ResultSet rs = sqlHandler.executeQuery(getResult, false);
		rs.last();
		double payout = rs.getDouble("amount");
		if(payout == 0) {
			return;
		}
		plugin.economy.depositPlayer(p.getName(), payout);
		k.sendMessage(ChatColor.AQUA + "[Bounties]: " + ChatColor.GRAY + "You have collected the " + ChatColor.GOLD + "$" + payout + ChatColor.GRAY + " bounty on " + ChatColor.GREEN + p.getName());
		int id = rs.getInt("id");
		String update = "DELETE FROM bountiesplayer WHERE id='" + id + "'";
		sqlHandler.executeQuery(update, true);
	}

	private boolean checkExists(String wantedPlayerName) {
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
