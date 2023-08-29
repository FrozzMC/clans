package me.backstabber.epicsetclans.commands.subcommands.clan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.SubCommands;

public class MakeLeaderCommand extends SubCommands
{
	private String name;
	public MakeLeaderCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager)
	{
		super(plugin, clanManager, duelManager);
		this.name = "makeleader";
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
			sendMessage(sender, "info",placeHolders);
			return;
		}
		if(!clanManager.isInClan(sender.getName()))
		{
			sendMessage(sender, "noclan", placeHolders);
			return;
		}
		placeHolders.put("%clan%", clanManager.getClanData(sender.getName()).getClanName());
		placeHolders.put("%otherplayer%", sub[1]);
		if(!clanManager.isLeader(sender.getName()))
		{
			sendMessage(sender, "notleader", placeHolders);
			return;
		}
		if(!((EpicClanData) clanManager.getClanData(sender.getName())).getClanMembersName().contains(sub[1]))
		{
			sendMessage(sender, "noplayer", placeHolders);
			return;
		}
		if(clanManager.isLeader(sub[1]))
		{
			sendMessage(sender, "alreadyleader", placeHolders);
			return;
		}
		clanManager.makeLeader(sub[1]);
		sendMessage(sender, "sent", placeHolders);
		if(Bukkit.getPlayerExact(sub[1])!=null)
			sendMessage(Bukkit.getPlayerExact(sub[1]), "toplayer", placeHolders);
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
		alias.add("ml");
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
		List<String> players=((EpicClanData) clanManager.getClanData(sender.getName())).getClanMembersName();
		StringUtil.copyPartialMatches(hint, players, result);
		Collections.sort(result);
        return result;
	}

}
