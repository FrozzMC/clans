package me.backstabber.epicsetclans.commands.subcommands.clan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.events.ClanLeaveEvent;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.SubCommands;

public class LeaveCommand extends SubCommands
{
	private String name;
	public LeaveCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager)
	{
		super(plugin, clanManager, duelManager);
		this.name = "leave";
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
		if(sender.hasMetadata("EpicClanLeave"))
		{
			sender.removeMetadata("EpicClanLeave", plugin);
			ClanLeaveEvent event=new ClanLeaveEvent(sender, clanManager.getClanData(sender.getName()));
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return;
			placeHolders.put("%clan%", clanManager.getClanData(sender.getName()).getClanName());
			for(String s:((EpicClanData) clanManager.getClanData(sender.getName())).getClanMembersName())
			{
				if(Bukkit.getPlayerExact(s)!=null)
					sendMessage(Bukkit.getPlayerExact(s), "others", placeHolders);
			}
			if(clanManager.isLeader(sender)) 
				plugin.getClanTopManager().removeClan(clanManager.getClanData(sender));
			clanManager.getClanData(sender.getName()).removeClanMember(sender);
			sendMessage(sender, "sucess", placeHolders);
			return;
		}
		if(!clanManager.isInClan(sender.getName()))
		{
			sendMessage(sender, "noclan", placeHolders);
			return;
		}
		if(clanManager.isLeader(sender.getName()))
			sendMessage(sender, "isleader", placeHolders);
		else
			sendMessage(sender, "pre", placeHolders);
		sender.setMetadata("EpicClanLeave", new FixedMetadataValue(plugin, true));
		new BukkitRunnable() 
		{
			
			@Override
			public void run() 
			{
				sender.removeMetadata("EpicClanLeave", plugin);
			}
		}.runTaskLater(plugin, 20*5);
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
		alias.add("abandon");
		return alias;
	}
	@Override
	public List<String> getCompletor(Player sender, int length, String hint) 
	{
		return new ArrayList<>();
	}

}
