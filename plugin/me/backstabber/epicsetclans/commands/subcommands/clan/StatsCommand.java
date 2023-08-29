package me.backstabber.epicsetclans.commands.subcommands.clan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.StringUtil;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.data.ClanData;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.SubCommands;

public class StatsCommand extends SubCommands
{
	private String name;
	public StatsCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager)
	{
		super(plugin, clanManager, duelManager);
		this.name = "stats";
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
			if(!clanManager.isInClan(sender.getName()))
			{
				sendMessage(sender, "noclan", placeHolders);
				return;
				
			}
			EpicClanData clan=(EpicClanData) clanManager.getClanData(sender.getName());
			placeHolders.put("%clan%", clan.getClanName());
			Inventory stats = plugin.getGuis().get("stats").getMainInventory(clan,sender);
			sender.openInventory(stats);
			sendMessage(sender, "sucess", placeHolders);
			return;
			
		}
		String clanName = sub[1];
		for (int i = 2; i < sub.length; i++)
			clanName = clanName.concat(" " + sub[i]);
        if(clanManager.getClanData(clanName)==null)
        {
        	placeHolders.put("%clan%", clanName);
        	sendMessage(sender, "notclan", placeHolders);
        }
        EpicClanData clan=(EpicClanData) clanManager.getClanData(clanName);
		placeHolders.put("%clan%", clan.getClanName());
		Inventory stats = plugin.getGuis().get("stats").getMainInventory(clan,sender);
		sender.openInventory(stats);
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
		if(length==1)
			return new ArrayList<>();
		if(!clanManager.isInClan(sender.getName()))
			return new ArrayList<>();
		List<String> result=new ArrayList<>();
		List<String> players=new ArrayList<>();
		for(Player player:Bukkit.getOnlinePlayers())
			if(clanManager.isInClan(player.getName()))
				players.add(player.getName());
		StringUtil.copyPartialMatches(hint, players, result);
		List<String> clans=new ArrayList<>();
		for(ClanData clan : clanManager.getAllClans())
			clans.add(clan.getClanNameRaw());
		StringUtil.copyPartialMatches(hint, clans, result);
		Collections.sort(result);
        return result;
	}

}
