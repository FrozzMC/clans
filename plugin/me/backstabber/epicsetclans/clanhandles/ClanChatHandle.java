package me.backstabber.epicsetclans.clanhandles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.data.ClanData;
import me.backstabber.epicsetclans.api.events.ClanChatEvent;
import me.backstabber.epicsetclans.api.events.ClanGroupChatEvent;
import me.backstabber.epicsetclans.api.events.ClanChatEvent.ChatType;
import me.backstabber.epicsetclans.api.events.ClanGroupChatEvent.GroupType;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.utils.CommonUtils;
import me.clip.placeholderapi.PlaceholderAPI;

@SuppressWarnings("deprecation")
public class ClanChatHandle 
{
	private EpicSetClans handle;
	private EpicClanManager clanManager;

	public ClanChatHandle(final EpicSetClans handle, final EpicClanManager clanManager) {
		this.handle = handle;
		this.clanManager = clanManager;
	}
	
	public void handleChatEvent(PlayerChatEvent event)
	{
		Player player=event.getPlayer();
		String msg=event.getMessage();
		String id=handle.getSettings().getString("formats.clan-chat-char");
		if(player.hasMetadata("EpicSetClanChat"))
		{
			event.setCancelled(true);
			event.setMessage("");
			String type=player.getMetadata("EpicSetClanChat").get(0).asString();
			if(type==null)
				return;
			if(type.equalsIgnoreCase("clan"))
				sendClanChat(player, msg);
			if(type.equalsIgnoreCase("ally"))
				sendAllyChat(player, msg);
			if(type.equalsIgnoreCase("truce"))
				sendTruceChat(player, msg);
		}
		else if(id!=null&&id.length()>0&&msg.trim().startsWith(id.substring(0,1)))
		{
			if(!clanManager.isInClan(player.getName()))
				return;
			event.setCancelled(true);
			event.setMessage("");
			msg=msg.replaceFirst(id.substring(0,1), "");
			sendClanChat(player, msg);
		}
	}
	public void handleAsyncChatEvent(AsyncPlayerChatEvent event)
	{
		Player player=event.getPlayer();
		String msg=event.getMessage();
		String id=handle.getSettings().getString("formats.clan-chat-char");
		if(player.hasMetadata("EpicSetClanChat"))
		{
			event.setCancelled(true);
				return;
		}
		else if(id!=null&&id.length()>0&&msg.trim().startsWith(id.substring(0,1)))
		{
			if(!clanManager.isInClan(player.getName()))
				return;
			event.setCancelled(true);
			return;
		}
	}
	public void sendClanChat(Player player,String msg)
	{
		if(!clanManager.isInClan(player.getName()))
			return;
		EpicClanData data = (EpicClanData) clanManager.getClanData(player.getName());
		//for clan members
		String clanFormat=setPlaceHolders(player,handle.getSettings().getString("formats.clan-chat-format"));
		List<Player> members=new ArrayList<>();
		for(String member:data.getClanMembersName())
			if(Bukkit.getPlayerExact(member)!=null)
				members.add(Bukkit.getPlayerExact(member));
		ClanChatEvent clanEvent=new ClanChatEvent(player, members, data, clanFormat, msg, ChatType.CLAN);
		Bukkit.getPluginManager().callEvent(clanEvent);
		
		//for spies
		String spyFormat=setPlaceHolders(player,handle.getSettings().getString("formats.spy-chat-format"));
		List<Player> spies=new ArrayList<>();
		for(Player spy:Bukkit.getOnlinePlayers())
			if(spy.hasMetadata("EpicChatSpy"))
				spies.add(spy);
		ClanChatEvent spyEvent=new ClanChatEvent(player, spies, data, spyFormat, msg, ChatType.SPY);
		Bukkit.getPluginManager().callEvent(spyEvent);
		
		//grouped event
		//create recipients
		Map<Player, String> recipients=new HashMap<Player, String>();
		for(Player member:members)
			recipients.put(member, clanFormat);
		for(Player spy:spies)
			recipients.put(spy, spyFormat);
		ClanGroupChatEvent groupedEvent=new ClanGroupChatEvent(player, recipients, data, msg, GroupType.CLAN);
		Bukkit.getPluginManager().callEvent(groupedEvent);
		if(!groupedEvent.isCancelled()) {
			for(Player toSend:groupedEvent.getRecipient()) {
				toSend.sendMessage(CommonUtils.chat(groupedEvent.getFromat(toSend).replace("%msg%", groupedEvent.getMessage())));
			}
		}
		Bukkit.getConsoleSender().sendMessage(CommonUtils.chat(spyEvent.getFromat().replace("%msg%", spyEvent.getMessage())));
	}
	public void sendAllyChat(Player player,String msg)
	{
		if(!clanManager.isInClan(player.getName()))
			return;
		EpicClanData data = (EpicClanData) clanManager.getClanData(player.getName());
		//for clan members
		String clanFormat=setPlaceHolders(player,handle.getSettings().getString("formats.clan-chat-format"));
		List<Player> members=new ArrayList<>();
		for(String member:data.getClanMembersName())
			if(Bukkit.getPlayerExact(member)!=null)
				members.add(Bukkit.getPlayerExact(member));
		ClanChatEvent clanEvent=new ClanChatEvent(player, members, data, clanFormat, msg, ChatType.CLAN);
		Bukkit.getPluginManager().callEvent(clanEvent);
		//for allies
		String allyFormat=setPlaceHolders(player,handle.getSettings().getString("formats.ally-chat-format"));
		List<Player> allies=new ArrayList<>();
		for(ClanData ally:data.getClanAllies())
			for(String member:((EpicClanData) ally).getClanMembersName())
				if(Bukkit.getPlayerExact(member)!=null)
					allies.add(Bukkit.getPlayerExact(member));
		ClanChatEvent allyEvent=new ClanChatEvent(player, allies, data, allyFormat, msg, ChatType.ALLY);
		Bukkit.getPluginManager().callEvent(allyEvent);
		//for spies
		String spyFormat=setPlaceHolders(player,handle.getSettings().getString("formats.spy-chat-format"));
		List<Player> spies=new ArrayList<>();
		for(Player spy:Bukkit.getOnlinePlayers())
			if(spy.hasMetadata("EpicChatSpy"))
				spies.add(spy);
		ClanChatEvent spyEvent=new ClanChatEvent(player, spies, data, spyFormat, msg, ChatType.SPY);
		Bukkit.getPluginManager().callEvent(spyEvent);
		//grouped event
		//create recipients
		Map<Player, String> recipients=new HashMap<Player, String>();
		for(Player member:members)
			recipients.put(member, clanFormat);
		for(Player ally:allies)
			recipients.put(ally, allyFormat);
		for(Player spy:spies)
			recipients.put(spy, spyFormat);
		ClanGroupChatEvent groupedEvent=new ClanGroupChatEvent(player, recipients, data, msg, GroupType.CLAN_ALLY);
		Bukkit.getPluginManager().callEvent(groupedEvent);
		if(!groupedEvent.isCancelled()) {
			for(Player toSend:groupedEvent.getRecipient()) {
				toSend.sendMessage(CommonUtils.chat(groupedEvent.getFromat(toSend).replace("%msg%", groupedEvent.getMessage())));
			}
		}
		Bukkit.getConsoleSender().sendMessage(CommonUtils.chat(spyEvent.getFromat().replace("%msg%", spyEvent.getMessage())));
	}
	public void sendTruceChat(Player player,String msg)
	{

		if(!clanManager.isInClan(player.getName()))
			return;
		EpicClanData data = (EpicClanData) clanManager.getClanData(player.getName());
		//for clan members
		String clanFormat=setPlaceHolders(player,handle.getSettings().getString("formats.clan-chat-format"));
		List<Player> members=new ArrayList<>();
		for(String member:data.getClanMembersName())
			if(Bukkit.getPlayerExact(member)!=null)
				members.add(Bukkit.getPlayerExact(member));
		ClanChatEvent clanEvent=new ClanChatEvent(player, members, data, clanFormat, msg, ChatType.CLAN);
		Bukkit.getPluginManager().callEvent(clanEvent);

		//for allies
		String allyFormat=setPlaceHolders(player,handle.getSettings().getString("formats.ally-chat-format"));
		List<Player> allies=new ArrayList<>();
		for(ClanData ally:data.getClanAllies())
			for(String member:((EpicClanData) ally).getClanMembersName())
				if(Bukkit.getPlayerExact(member)!=null)
					allies.add(Bukkit.getPlayerExact(member));
		ClanChatEvent allyEvent=new ClanChatEvent(player, allies, data, allyFormat, msg, ChatType.ALLY);
		Bukkit.getPluginManager().callEvent(allyEvent);

		//for truces
		String truceFormat=setPlaceHolders(player,handle.getSettings().getString("formats.truce-chat-format"));
		List<Player> truces=new ArrayList<>();
		for(ClanData truce:data.getClanAllies())
			for(String member:((EpicClanData) truce).getClanMembersName())
				if(Bukkit.getPlayerExact(member)!=null)
					truces.add(Bukkit.getPlayerExact(member));
		ClanChatEvent truceEvent=new ClanChatEvent(player, truces, data, truceFormat, msg, ChatType.TRUCE);
		Bukkit.getPluginManager().callEvent(truceEvent);

		//for spies
		String spyFormat=setPlaceHolders(player,handle.getSettings().getString("formats.spy-chat-format"));
		List<Player> spies=new ArrayList<>();
		for(Player spy:Bukkit.getOnlinePlayers())
			if(spy.hasMetadata("EpicChatSpy"))
				spies.add(spy);
		ClanChatEvent spyEvent=new ClanChatEvent(player, spies, data, spyFormat, msg, ChatType.SPY);
		Bukkit.getPluginManager().callEvent(spyEvent); 
		
		//grouped event
		//create recipients
		Map<Player, String> recipients=new HashMap<Player, String>();
		for(Player member:members)
			recipients.put(member, clanFormat);
		for(Player ally:allies)
			recipients.put(ally, allyFormat);
		for(Player truce:truces)
			recipients.put(truce, truceFormat);
		for(Player spy:spies)
			recipients.put(spy, spyFormat);
		ClanGroupChatEvent groupedEvent=new ClanGroupChatEvent(player, recipients, data, msg, GroupType.CLAN_ALLY_TRUCE);
		Bukkit.getPluginManager().callEvent(groupedEvent);
		if(!groupedEvent.isCancelled()) {
			for(Player toSend:groupedEvent.getRecipient()) {
				toSend.sendMessage(CommonUtils.chat(groupedEvent.getFromat(toSend).replace("%msg%", groupedEvent.getMessage())));
			}
		}
		Bukkit.getConsoleSender().sendMessage(CommonUtils.chat(spyEvent.getFromat().replace("%msg%", spyEvent.getMessage())));
	}
	
	
	private String setPlaceHolders(Player player,String msg)
	{

		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
			msg=PlaceholderAPI.setPlaceholders(player, msg);
		msg=msg.replace("%player%", player.getName());
		if(clanManager.isInClan(player.getName()))
		{
			msg=msg.replace("%clan%",clanManager.getClanData(player.getName()).getClanName());
			msg=msg.replace("%leader%", clanManager.getClanData(player.getName()).getClanLeader());
			msg=msg.replace("%tag%", clanManager.getClanData(player.getName()).getMemberData(player).getTag());
		}
		return msg;
	}
}
