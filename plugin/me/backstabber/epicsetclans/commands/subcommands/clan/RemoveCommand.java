package me.backstabber.epicsetclans.commands.subcommands.clan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.data.ClanData;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.SubCommands;

public class RemoveCommand extends SubCommands
{
	private String name;
	public RemoveCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager)
	{
		super(plugin, clanManager, duelManager);
		this.name = "remove";
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
		if(!clanManager.isLeader(sender.getName())&&!clanManager.getClanData(sender.getName()).getMemberData(sender).hasPermission(this.name))
		{
			sendMessage(sender, "notleader", placeHolders);
			return;
		}
		String allyName = sub[1];
		for (int i = 2; i < sub.length; i++)
			allyName = allyName.concat(" " + sub[i]); 
		if(!clanManager.isClanName(allyName))
		{
			placeHolders.put("%otherclan%", allyName);
			sendMessage(sender, "notclan", placeHolders);
			return;
		}
		EpicClanData ally=(EpicClanData) clanManager.getClanData(allyName);
		placeHolders.put("%otherclan%", ally.getClanName());
		if(!clanManager.areAllies(clan, ally)&&!clanManager.areTruces(clan, ally))
		{
			sendMessage(sender, "notclan", placeHolders);
			return;
		}
		clanManager.removeAllies(clan, ally);
		clanManager.removeTruce(clan, ally);
		for(String s:clan.getClanMembersName())
			if(Bukkit.getPlayerExact(s)!=null)
				sendMessage(Bukkit.getPlayerExact(s), "toother", placeHolders);
		for(String s:ally.getClanMembersName())
			if(Bukkit.getPlayerExact(s)!=null)
			sendMessage(Bukkit.getPlayerExact(s), "toother", placeHolders);
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
		alias.add("betray");
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
		List<String> clans=new ArrayList<>();
		EpicClanData clan = (EpicClanData) clanManager.getClanData(sender.getName());
		for(ClanData ally : clan.getClanAllies())
			clans.add(ally.getClanNameRaw());
		for(ClanData ally : clan.getClanTruce())
			clans.add(ally.getClanNameRaw());
		StringUtil.copyPartialMatches(hint, clans, result);
		Collections.sort(result);
        return result;
	}

}
