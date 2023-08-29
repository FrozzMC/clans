package me.backstabber.epicsetclans.commands.subcommands.clan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.data.ClanBasesData;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.SubCommands;

public class DelBaseCommand extends SubCommands
{
	private String name;
	public DelBaseCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager)
	{
		super(plugin, clanManager, duelManager);
		this.name = "delbase";
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
		if(!clanManager.isLeader(sender.getName())&&!clan.getMemberData(sender.getName()).hasPermission("delbase"))
		{
			sendMessage(sender,"notleader", placeHolders);
			return;
		}
		String baseName=sub[1];
		placeHolders.put("%base%", baseName);
		if(!clan.getBases().isBase(baseName))
		{
			sendMessage(sender,"notset", placeHolders);
			return;
		}
		clan.getBases().removeBase(baseName);
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
		EpicClanData clan = (EpicClanData) clanManager.getClanData(sender.getName());
		List<String> result=new ArrayList<>();
		List<String> bases=new ArrayList<>();
		for(Integer baseNumber:((ClanBasesData) clan.getBases()).getAllBases().keySet())
			bases.add(((ClanBasesData) clan.getBases()).getBaseName(baseNumber));
		StringUtil.copyPartialMatches(hint, bases, result);
		Collections.sort(result);
		return result;
	}

}
