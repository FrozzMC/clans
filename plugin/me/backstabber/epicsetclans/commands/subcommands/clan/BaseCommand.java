package me.backstabber.epicsetclans.commands.subcommands.clan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.data.ClanData;
import me.backstabber.epicsetclans.clanhandles.data.ClanBasesData;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.SubCommands;
import me.backstabber.epicsetclans.messager.JSONMessage;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class BaseCommand extends SubCommands
{
	private String name;
	public BaseCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager)
	{
		super(plugin, clanManager, duelManager);
		this.name = "base";
		this.permission=plugin.isCommandOpen(name);
		if(plugin.getMessages().getFile().isSet(this.name))
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
			sendMessage(sender, "clanbase", placeHolders);
			int index=1;
			for(Integer baseNumber:((ClanBasesData) clan.getBases()).getAllBases().keySet())
			{
				JSONMessage.create(CommonUtils.chat("&7"+index+"&f) "+((ClanBasesData) clan.getBases()).getBaseName(baseNumber))).tooltip(CommonUtils.chat("&7Click to teleport.")).runCommand("/clan base "+((ClanBasesData) clan.getBases()).getBaseName(baseNumber)).send(sender);
				index++;
			}
			sender.sendMessage(CommonUtils.chat("&eAllies Bases:"));
			index=1;
			for(ClanData ally:clan.getClanAllies())
			{
				for(Integer baseNumber:((ClanBasesData) ally.getBases()).getAllBases().keySet())
				{
					JSONMessage.create(CommonUtils.chat("&7"+index+"&f) "+((ClanBasesData) ally.getBases()).getBaseName(baseNumber))).tooltip(CommonUtils.chat("&7Click to teleport.")).runCommand("/clan base "+((ClanBasesData) ally.getBases()).getBaseName(baseNumber)).send(sender);
					index++;
				}
			}
			return;
		}
		if(!clanManager.isInClan(sender.getName()))
		{
			sendMessage(sender, "noclan", placeHolders);
			return;
		}
		EpicClanData clan=(EpicClanData) clanManager.getClanData(sender.getName());
		placeHolders.put("%clan%", clan.getClanName());
		String baseName=sub[1];
		placeHolders.put("%base%", baseName);
		if(!clan.getBases().isBase(baseName))
		{
			for(ClanData ally:clan.getClanAllies())
			{
				if(ally.getBases().isBase(baseName))
				{
					placeHolders.put("%clan%", ally.getClanName());
					Location base = ally.getBases().getBase(baseName);
					sender.teleport(base);
					sendMessage(sender, "sucess", placeHolders);
					return;
				}
			}
			sendMessage(sender,"nobase", placeHolders);
			return;
		}
		Location base = clan.getBases().getBase(baseName);
		sender.teleport(base);
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
	public List<String> getCompletor(Player sender,int length, String hint) 
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
		for(ClanData ally:clan.getClanAllies())
			for(Integer baseNumber:((ClanBasesData) ally.getBases()).getAllBases().keySet())
				bases.add(((ClanBasesData) ally.getBases()).getBaseName(baseNumber));
		StringUtil.copyPartialMatches(hint, bases, result);
		Collections.sort(result);
		return result;
	}

}
