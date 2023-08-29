package me.backstabber.epicsetclans.commands.subcommands.clan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.data.ClanData;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.data.ClanDuelData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.SubCommands;

public class DuelCommand extends SubCommands
{
	private String name;
	public DuelCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager)
	{
		super(plugin, clanManager, duelManager);
		this.name = "duel";
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
		if(sub.length == 1)
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
		if(!clanManager.isLeader(sender.getName())&&!clan.getMemberData(sender.getName()).hasPermission("duel"))
		{
			sendMessage(sender, "notleader", placeHolders);
			return;
		}

		String clanName=sub[1];
		for (int i = 2; i < sub.length; i++)
			clanName = clanName.concat(" " + sub[i]);
		if(!clanManager.isClanName(clanName))
		{
			placeHolders.put("%otherclan%", clanName);
			sendMessage(sender, "notclan", placeHolders);
			return;
		}

		EpicClanData otherclan=(EpicClanData) clanManager.getClanData(clanName);
		placeHolders.put("%otherclan%", otherclan.getClanName());
		if(clan.equals(otherclan)||clanManager.areAllies(clan, otherclan)||clanManager.areTruces(clan, otherclan))
		{
			sendMessage(sender, "allied", placeHolders);
			return;
		}

		if(duelManager.isDueling(clan)||duelManager.isDueling(otherclan))
		{
			sendMessage(sender, "dueling", placeHolders);
			return;
		}

		if(!duelManager.canDuel(clan)||!duelManager.canDuel(otherclan))
		{
			sendMessage(sender, "nomore", placeHolders);
			return;
		}

		ClanDuelData duel = duelManager.createNewDuel(clan, otherclan);
		if(duel == null) {
			sendMessage(sender, "busy", placeHolders);
			return;
		}
		
		if (!duel.isSent()) {
			sendMessage(sender, "fail", placeHolders);
			duelManager.removeDuel(duel.getClanAlpha().getClanName());
			return;
		}
		
		sender.openInventory(plugin.getGuis().get("duel").getMainInventory(clan,sender));
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
			if(!clanManager.areAllies(clan, clanManager.getClanData(sender.getName())))
				if(!clanManager.areTruces(clan, clanManager.getClanData(sender.getName())))
				clans.add(clan.getClanNameRaw());
		StringUtil.copyPartialMatches(hint, clans, result);
		Collections.sort(result);
        return result;
	}

}
