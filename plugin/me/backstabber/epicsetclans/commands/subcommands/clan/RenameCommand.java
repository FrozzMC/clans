package me.backstabber.epicsetclans.commands.subcommands.clan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.events.ClanRenameEvent;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.SubCommands;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class RenameCommand extends SubCommands
{
	private String name;
	public RenameCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager)
	{
		super(plugin, clanManager, duelManager);
		this.name = "rename";
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
		if(!clan.canRename())
		{
			placeHolders.put("%time%", clan.getRenameTimeLeft());
			sendMessage(sender, "timer", placeHolders);
			return;
		}
		if(!clanManager.isLeader(sender.getName()))
		{
			sendMessage(sender, "notleader", placeHolders);
			return;
		}
		String newName = sub[1];
		for (int i = 2; i < sub.length; i++)
			newName = newName.concat(" " + sub[i]); 
		placeHolders.put("%newname%", CommonUtils.chat(newName));
		if(clanManager.isClanName(newName)&&!clan.getClanNameRaw().equals(ChatColor.stripColor(CommonUtils.chat(newName))))
		{
			sendMessage(sender, "nametaken", placeHolders);
			return;
		}
		if(ChatColor.stripColor(CommonUtils.chat(newName)).equalsIgnoreCase(""))
		{
			sendMessage(sender, "noname", placeHolders);
			return;
		}
		if(newName.toCharArray().length>plugin.getSettings().getFile().getInt("settings.max-clan-name"))
		{
			sendMessage(sender, "bigname", placeHolders);
			return;
		}
		newName=newName.replace("&k", "");
		ClanRenameEvent event=new ClanRenameEvent(sender, clan);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		clan.setClanName(newName);
		clan.updateRenameTimer();
		sendMessage(sender, "sucess", placeHolders);
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
