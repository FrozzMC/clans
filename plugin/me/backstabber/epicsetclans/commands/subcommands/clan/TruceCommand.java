package me.backstabber.epicsetclans.commands.subcommands.clan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.data.ClanData;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.SubCommands;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class TruceCommand extends SubCommands
{
	private String name;
	public TruceCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager)
	{
		super(plugin, clanManager, duelManager);
		this.name = "truce";
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
			sendMessageNormal(sender, "info", placeHolders);
			if(clanManager.isInClan(sender.getName()))
			{
				sender.sendMessage(" ");
				sendMessageNormal(sender, "list", placeHolders);
				int i=1;
				for(ClanData truce:clanManager.getClanData(sender.getName()).getClanTruce())
				{
					sender.sendMessage(CommonUtils.chat("&f"+i+"&7) &f"+truce.getClanName()));
					i++;
				}
			}
			return;
		}
		String truceName = sub[1];
        for (int i = 2; i < sub.length; i++)
        	truceName = truceName.concat(" " + sub[i]); 
		if(!clanManager.isInClan(sender.getName()))
		{
			sendMessage(sender, "noclan", placeHolders);
			return;
		}
		EpicClanData clan = (EpicClanData) clanManager.getClanData(sender.getName());
		placeHolders.put("%clan%", clan.getClanName());
		if(sub[1].equalsIgnoreCase("list"))
		{
			sender.sendMessage(" ");
			sendMessageNormal(sender, "list", placeHolders);
			int i=1;
			for(ClanData truce:clanManager.getClanData(sender.getName()).getClanTruce())
			{
				sender.sendMessage(CommonUtils.chat("&f"+i+"&7) &f"+truce.getClanName()));
				i++;
			}
			return;
		}
		if(!clanManager.isLeader(sender.getName())&&!clan.getMemberData(sender.getName()).hasPermission(this.name))
		{
			sendMessage(sender, "notleader", placeHolders);
			return;
		}
		if(!clanManager.isClanName(truceName))
		{
			placeHolders.put("%otherclan%", truceName);
			sendMessage(sender, "notclan", placeHolders);
			return;
		}
		EpicClanData truce=(EpicClanData) clanManager.getClanData(truceName);
		placeHolders.put("%otherclan%", truce.getClanName());
		if(clanManager.areTruces(clan, truce))
		{
			sendMessage(sender, "already", placeHolders);
			return;
		}
		//check allies limits
		if(clan.getClanTruce().size()>=clan.getAlliesUpgrade())
		{
			sendMessage(sender, "limitself", placeHolders);
			return;
		}
		if(truce.getClanTruce().size()>=truce.getAlliesUpgrade())
		{
			sendMessage(sender, "limit", placeHolders);
			return;
		}
		for(String members:truce.getClanMembersName())
			if(Bukkit.getPlayerExact(members)!=null)
			{
				sendMessage(Bukkit.getPlayerExact(members), "toclan", placeHolders);
				Bukkit.getPlayerExact(members).setMetadata("EpicClanTruce", new FixedMetadataValue(plugin, clan.getClanLeader()));
			}
		new BukkitRunnable() 
		{
			
			@Override
			public void run() 
			{
				for(String members:truce.getClanMembersName())
					if(Bukkit.getPlayerExact(members)!=null)
					{
						if(Bukkit.getPlayerExact(members).hasMetadata("EpicClanTruce"))
						{
							sendMessage(Bukkit.getPlayerExact(members), "timed", placeHolders);
							Bukkit.getPlayerExact(members).removeMetadata("EpicClanTruce", plugin);
						}
					}
			}
		}.runTaskLater(plugin, 20*20);
		sendMessage(sender, "sent", placeHolders);
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
		List<String> clans=new ArrayList<>();
		for(ClanData clan : clanManager.getAllClans())
			if(!clanManager.areTruces(clan, clanManager.getClanData(sender.getName())))
				clans.add(clan.getClanNameRaw());
		StringUtil.copyPartialMatches(hint, clans, result);
		Collections.sort(result);
        return result;
	}

}
