package me.backstabber.epicsetclans.commands.subcommands.clan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.SubCommands;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class TagCommand extends SubCommands
{
	private String name;
	public TagCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager)
	{
		super(plugin, clanManager, duelManager);
		this.name = "tag";
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
		if(sub.length<=2)
		{
			sendMessage(sender, "info", placeHolders);
			return;
		}
		if(!clanManager.isInClan(sender.getName()))
		{
			sendMessage(sender, "noclan", placeHolders);
			return;
		}
		EpicClanData data = (EpicClanData) clanManager.getClanData(sender.getName());
		placeHolders.put("%clan%", data.getClanName());
		if(!clanManager.isLeader(sender.getName())&&!data.getMemberData(sender.getName()).hasPermission(this.name))
		{
			sendMessage(sender, "notleader", placeHolders);
			return;
		}
		placeHolders.put("%otherplayer%", sub[1]);
		if(!data.getClanMembersName().contains(sub[1]))
		{
			sendMessage(sender, "noplayer", placeHolders);
			return;
		}
		placeHolders.put("%tag%", CommonUtils.chat(sub[2]));
		if(ChatColor.stripColor(placeHolders.get("%tag%")).toCharArray().length>plugin.getSettings().getFile().getInt("settings.max-tag"))
		{
			sendMessage(sender, "toobig", placeHolders);
			return;
		}
		data.getMemberData(sub[1]).setTag(sub[2]);
		sendMessage(sender, "success", placeHolders);
		if(Bukkit.getPlayer(sub[1])!=null)
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
		alias.add("role");
		return alias;
	}
	@Override
	public List<String> getCompletor(Player sender, int length, String hint) 
	{
		return new ArrayList<>();
	}

}
