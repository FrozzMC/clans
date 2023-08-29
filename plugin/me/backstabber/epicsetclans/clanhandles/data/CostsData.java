package me.backstabber.epicsetclans.clanhandles.data;


import java.util.List;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.manager.YMLManager;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class CostsData 
{
	private EpicSetClans plugin;
	private int balance;
	private int xp;
	private int kills;
	private String permission;

	public CostsData(EpicSetClans plugin, YMLManager file,String node)
	{
		this.plugin = plugin;
		this.balance=file.getInt("creation-costs.groups."+node+".balance");
		this.xp=file.getInt("creation-costs.groups."+node+".experience");
		this.kills=file.getInt("creation-costs.groups."+node+".kills");
		this.permission=file.getString("creation-costs.groups."+node+".permission");
	}
	public boolean isGreater(CostsData data) {
		if(this.balance>data.balance)
			return true;
		if(this.kills>data.kills)
			return true;
		if(this.xp>data.xp)
			return true;
		return false;
	}
	public boolean isSmaller(CostsData data) {
		if(this.balance<data.balance)
			return true;
		if(this.kills<data.kills)
			return true;
		if(this.xp<data.xp)
			return true;
		return false;
	}
	public boolean isApplicable(Player player)
	{
		if(permission.equalsIgnoreCase("none"))
			return true;
		return player.hasPermission(permission);
	}
	public boolean apply(Player player) 
	{
		int pkills=player.getStatistic(Statistic.PLAYER_KILLS);
		double pbal=plugin.getEconomy().getBalance(player);
		float pxp=CommonUtils.getExperience(player);
		if(pkills<kills&&pbal<balance&&pxp<xp)
		{
			sendMessage(player,"balance , experience & kills");
			return false;
		}
		else if(pbal<balance&&pkills<kills)
		{
			sendMessage(player,"balance & kills");
			return false;
		}
		else if(pbal<balance&&pxp<xp)
		{
			sendMessage(player,"balance & experience");
			return false;
		}
		else if(pkills<kills&&pxp<xp)
		{
			sendMessage(player,"kills & experience");
			return false;
		}
		else if(pkills<kills)
		{
			sendMessage(player,"kills");
			return false;
		}
		else if(pbal<balance)
		{
			sendMessage(player,"balance");
			return false;
		}
		else if(pxp<xp)
		{
			sendMessage(player,"experience");
			return false;
		}
		CommonUtils.setTotalExperience(player, (int) (pxp-xp));
		plugin.getEconomy().withdrawPlayer(player, balance);
		return true;
	}
	private void sendMessage(Player player, String costs) 
	{
		String prefix=plugin.getSettings().getString("settings.prefix");
		List<String> message=plugin.getSettings().getStringList("creation-costs.message-on-fail");
		for(String msg:message)
			player.sendMessage(CommonUtils.chat(msg.replace("%kills%", CommonUtils.getDecimalFormatted(kills)).replace("%experience%", CommonUtils.getDecimalFormatted(xp)).replace("%balance%", CommonUtils.getDecimalFormatted(balance)).replace("%cost%", costs).replace("%prefix%", prefix)));
	}
}
