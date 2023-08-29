package me.backstabber.epicsetclans.commands.subcommands.clan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.events.ClanJoinEvent;
import me.backstabber.epicsetclans.api.events.ClanJoinEvent.JoinCause;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.data.ClanDuelData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.SubCommands;

public class AcceptCommand extends SubCommands
{
	private String name;
	public AcceptCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager)
	{
		super(plugin, clanManager, duelManager);
		this.name = "accept";
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
		if(sender.hasMetadata("EpicClan"))
		{
			String name=sender.getMetadata("EpicClan").get(0).asString();
			placeHolders.put("%clan%", clanManager.getClanData(name).getClanName());
			ClanJoinEvent event=new ClanJoinEvent(sender, clanManager.getClanData(name),JoinCause.INVITE);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return;
			clanManager.getClanData(name).addClanMember(sender);
			sender.removeMetadata("EpicClan", plugin);
			sendMessage(sender, "onaccept", placeHolders);
			for(String s:((EpicClanData)clanManager.getClanData(name)).getClanMembersName())
			{
				if(Bukkit.getPlayerExact(s)!=null)
					sendMessage(Bukkit.getPlayerExact(s), "onjoin", placeHolders);
			}
			return;
		}
		if(sender.hasMetadata("EpicClanAlly"))
		{
			if(!clanManager.isInClan(sender.getName()))
			{
				new AllyCommand(plugin, clanManager, duelManager).sendMessage(sender, "noclan", placeHolders);
				return;
			}
			EpicClanData clan=(EpicClanData) clanManager.getClanData(sender.getName());
			if(!clanManager.isLeader(sender.getName())&&!clan.getMemberData(sender.getName()).hasPermission("ally"))
			{
				new AllyCommand(plugin, clanManager, duelManager).sendMessage(sender, "notleader", placeHolders);
				return;
			}
			String leader=sender.getMetadata("EpicClanAlly").get(0).asString();
			sender.removeMetadata("EpicClanAlly", plugin);
			EpicClanData ally=(EpicClanData) clanManager.getClanData(leader);
			placeHolders.put("%clan%", clan.getClanName());
			placeHolders.put("%otherclan%", ally.getClanName());
			clanManager.makeAllies(clan, ally);
			for(String s:clan.getClanMembersName())
				if(Bukkit.getPlayerExact(s)!=null)
					new AllyCommand(plugin, clanManager, duelManager).sendMessage(Bukkit.getPlayerExact(s), "sucess", placeHolders);
			for(String s:ally.getClanMembersName())
				if(Bukkit.getPlayerExact(s)!=null)
					new AllyCommand(plugin, clanManager, duelManager).sendMessage(Bukkit.getPlayerExact(s), "sucess", placeHolders);
			//remove meta tag from all members
			for(String s:ally.getClanMembersName())
				if(Bukkit.getPlayerExact(s)!=null)
					if(Bukkit.getPlayerExact(s).hasMetadata("EpicClanAlly"))
						Bukkit.getPlayerExact(s).removeMetadata("EpicClanAlly", plugin);
			return;
		}
		if(sender.hasMetadata("EpicClanTruce"))
		{
			if(!clanManager.isInClan(sender.getName()))
			{
				new TruceCommand(plugin, clanManager, duelManager).sendMessage(sender, "noclan", placeHolders);
				return;
			}
			EpicClanData clan=(EpicClanData) clanManager.getClanData(sender.getName());
			if(!clanManager.isLeader(sender.getName())&&!clan.getMemberData(sender.getName()).hasPermission("truce"))
			{
				new TruceCommand(plugin, clanManager, duelManager).sendMessage(sender, "notleader", placeHolders);
				return;
			}
			String leader=sender.getMetadata("EpicClanTruce").get(0).asString();
			sender.removeMetadata("EpicClanTruce", plugin);
			EpicClanData truce=(EpicClanData) clanManager.getClanData(leader);
			placeHolders.put("%clan%", clan.getClanName());
			placeHolders.put("%otherclan%", truce.getClanName());
			clanManager.makeTruces(clan, truce);
			for(String s:clan.getClanMembersName())
				if(Bukkit.getPlayerExact(s)!=null)
					new TruceCommand(plugin, clanManager, duelManager).sendMessage(Bukkit.getPlayerExact(s), "sucess", placeHolders);
			for(String s:truce.getClanMembersName())
				if(Bukkit.getPlayerExact(s)!=null)
					new TruceCommand(plugin, clanManager, duelManager).sendMessage(Bukkit.getPlayerExact(s), "sucess", placeHolders);
			//remove meta tag from all members
			for(String s:truce.getClanMembersName())
				if(Bukkit.getPlayerExact(s)!=null)
					if(Bukkit.getPlayerExact(s).hasMetadata("EpicClanTruce"))
						Bukkit.getPlayerExact(s).removeMetadata("EpicClanTruce", plugin);
			return;
		}
		if(sender.hasMetadata("EpicDuel"))
		{
			sender.removeMetadata("EpicDuel",plugin);
			if(!clanManager.isInClan(sender.getName()))
			{
				new DuelCommand(plugin, clanManager, duelManager).sendMessage(sender, "noclan", placeHolders);
				return;
			}
			EpicClanData clan = (EpicClanData) clanManager.getClanData(sender.getName());
			ClanDuelData duel = duelManager.getDuel(clan);
			if(duel!=null&&duel.canAdd(sender))
			{
				duel.addPlayer(sender);
				return;
			}
			sendMessage(sender, "norequest", placeHolders);
			return;
		}
		if(sender.hasMetadata("EpicDuelRequest"))
		{
			if(!clanManager.isInClan(sender.getName()))
			{
				new DuelCommand(plugin, clanManager, duelManager).sendMessage(sender, "noclan", placeHolders);
				return;
			}
			EpicClanData otherclan=(EpicClanData) clanManager.getClanData(sender.getName());
			placeHolders.put("%otherclan%", otherclan.getClanName());
			if(!clanManager.isLeader(sender.getName())&&!otherclan.getMemberData(sender.getName()).hasPermission("duel"))
			{
				new DuelCommand(plugin, clanManager, duelManager).sendMessage(sender, "notleader", placeHolders);
				return;
			}
			sender.removeMetadata("EpicDuelRequest", plugin);
			if(duelManager.isDueling(otherclan))
			{
				ClanDuelData duel = duelManager.getDuel(otherclan);
				EpicClanData clan = duel.getClanAlpha();
				placeHolders.put("%clan%", clan.getClanName());
				for(String playerName:duel.getClanBravo().getClanMembersName())
					if(Bukkit.getPlayerExact(playerName)!=null)
					{
						Player player=Bukkit.getPlayerExact(playerName);
						new DuelCommand(plugin, clanManager, duelManager).sendMessage(player, "accepted", placeHolders);
						player.removeMetadata("EpicDuelRequest", plugin);
					}
				for(String playerName:duel.getClanAlpha().getClanMembersName())
					if(Bukkit.getPlayerExact(playerName)!=null)
					{
						Player player=Bukkit.getPlayerExact(playerName);
						new DuelCommand(plugin, clanManager, duelManager).sendMessage(player, "accepted", placeHolders);
						player.removeMetadata("EpicDuelRequest", plugin);
					}
				duelManager.acceptDuelRequest(duel);
				return;
			}
			sendMessage(sender, "norequest", placeHolders);
			return;
		}
		sendMessage(sender, "norequest", placeHolders);
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
		alias.add("yes");
		return alias;
	}
	@Override
	public List<String> getCompletor(Player sender,int length, String hint)
	{
		return new ArrayList<>();
	}

}
