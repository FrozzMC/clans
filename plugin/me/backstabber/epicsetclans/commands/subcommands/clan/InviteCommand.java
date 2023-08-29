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
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.SubCommands;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class InviteCommand extends SubCommands
{
	private String name="invite";
	public InviteCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager)
	{
		super(plugin, clanManager, duelManager);
		this.name = "invite";
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
		if(sub.length<=1)
		{
			sendMessage(sender, "info",new HashMap<>());
			return;
		}
		HashMap<String,String> placeHolder = new HashMap<>();
		placeHolder.put("%player%", sender.getName());
		placeHolder.put("%timer%", CommonUtils.getTimeFormat(plugin.getSettings().getFile().getInt("settings.clan-create-timer")));
		if(!clanManager.isInClan(sender.getName()))
		{
			sendMessage(sender, "notclan", placeHolder);
			return;
		}
		if(!clanManager.isLeader(sender.getName())&&!clanManager.getClanData(sender.getName()).getMemberData(sender).hasPermission("invite"))
		{
			sendMessage(sender, "notleader", placeHolder);
			return;
		}
		if(Bukkit.getPlayerExact(sub[1])==null)
		{
			placeHolder.put("%inviteplayer%", sub[1]);
			sendMessage(sender, "noplayer", placeHolder);
			return;
		}
		Player member=Bukkit.getPlayerExact(sub[1]);
		placeHolder.put("%inviteplayer%", member.getName());
		if(clanManager.getClanData(sender.getName()).getClanMembers().size()>=((EpicClanData) clanManager.getClanData(sender)).getMemberUpgrade())
		{
			sendMessage(sender, "maxedout", placeHolder);
			return;
		}
		if(clanManager.isInClan(member.getName()))
		{
			sendMessage(sender, "inclan", placeHolder);
			return;
		}
		member.setMetadata("EpicClan", new FixedMetadataValue(plugin, clanManager.getClanData(sender.getName()).getClanLeader()));
		sendMessage(member, "onrecieve", placeHolder);
		new BukkitRunnable() 
		{
			@Override
			public void run() 
			{
				if(member.hasMetadata("EpicClan"))
				{
					member.removeMetadata("EpicClan", plugin);
					sendMessage(member, "ontimeout", placeHolder);
				}
			}
		}.runTaskLater(plugin, 20*20);
		sendMessage(sender, "sent", placeHolder);
	}

	@Override
	public String getName() 
	{
		return name;
	}

	@Override
	public List<String> getAliases() 
	{
		List<String> alias=new ArrayList<String>();
		alias.add(name);
		alias.add("i");
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
			if(!clanManager.isInClan(player.getName()))
				players.add(player.getName());
		StringUtil.copyPartialMatches(hint, players, result);
		Collections.sort(result);
        return result;
	}

}
