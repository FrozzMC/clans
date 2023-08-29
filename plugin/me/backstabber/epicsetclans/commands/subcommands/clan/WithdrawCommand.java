package me.backstabber.epicsetclans.commands.subcommands.clan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.SubCommands;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class WithdrawCommand extends SubCommands
{
	private String name;
	public WithdrawCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager)
	{
		super(plugin, clanManager, duelManager);
		this.name = "withdraw";
		this.permission=plugin.isCommandOpen(name);
		if(plugin.getMessages().isSet(this.name))
		{
			for(String key:plugin.getMessages().getFile().getConfigurationSection(this.name).getKeys(false))
			{
				messages.put(key, this.name+"."+key);
			}
		}
	}
	@Override
	public void onCommandByPlayer(Player sender, String[] sub) 
	{
		HashMap<String,String> placeHolders = new HashMap<>();
		placeHolders.put("%player%", sender.getName());
		if(sub.length<=1)
		{
			sendMessage(sender, "info", placeHolders);
			return;
		}
		if(!clanManager.isInClan(sender.getName()))
		{
			sendMessage(sender, "noclan", placeHolders);
			return;
		}
		EpicClanData clan=(EpicClanData) clanManager.getClanData(sender.getName());
		placeHolders.put("%clan%", clan.getClanName());
		if(!clanManager.isLeader(sender.getName())&&!clan.getMemberData(sender.getName()).hasPermission("bank"))
		{
			sendMessage(sender, "notleader", placeHolders);
			return;
		}
		int amount=0;
		try 
		{
			amount=Integer.valueOf(sub[1]);
		}
		catch(NullPointerException | NumberFormatException e)
		{
			sendMessage(sender, "correctamount", placeHolders);
			return;
		}
		amount=Math.abs(amount);
		double balance = clan.getClanBalance();
		placeHolders.put("%amount%", CommonUtils.getDecimalFormatted(amount));
		if(balance<amount)
		{
			sendMessage(sender, "nobal", placeHolders);
			return;
		}
		int max = plugin.getSettings().getFile().getInt("settings.clan-bank-max-withdraw");
		if(amount>max)
		{

			sendMessage(sender, "limit", placeHolders);
			return;
		}
		plugin.getEconomy().depositPlayer(sender, amount);
		clan.setClanBalance(clan.getClanBalance()-amount);
		sendMessage(sender, "sucess", placeHolders);
		return;
	}

	@Override
	public String getName() 
	{
		return this.name;
	}

	@Override
	public List<String> getAliases() 
	{
		List<String> alias=new ArrayList<String>();
		alias.add(name);
		return alias;
	}
	@Override
	public List<String> getCompletor(Player sender, int length, String hint) 
	{
		return new ArrayList<>();
	}

}
