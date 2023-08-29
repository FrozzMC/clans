package me.backstabber.epicsetclans.commands.subcommands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;


import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.messager.MessageFormatter;
import me.backstabber.epicsetclans.utils.CommonUtils;
import me.clip.placeholderapi.PlaceholderAPI;

public abstract class SubCommands 
{
	protected EpicSetClans plugin;
	protected EpicClanManager clanManager;
	protected ClanDuelManager duelManager;
	public SubCommands(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager)
	{
		this.plugin=plugin;
		this.clanManager=clanManager;
		this.duelManager=duelManager;
	}
	protected Map<String, String> messages=new HashMap<String, String>();
	protected boolean permission=false;
	public abstract void onCommandByPlayer(Player sender ,String[] sub);
	public abstract String getName();
	public abstract List<String> getAliases();
	public abstract List<String> getCompletor(Player sender,int length, String hint);
	public boolean hasPermission(Permissible sender)
	{
		if(this.permission)
			return true;
		if(sender.hasPermission("epicsetclans.admin"))
			return true;
		if(sender.hasPermission("epicsetclans."+getName()))
			return true;
		return false;
	}
	public List<String> getInfoMessage()
	{
		if(messages.containsKey("info"))
			return addPrefix(plugin.getMessages().getFile().getStringList(messages.get("info")));
		return new ArrayList<>();
	}
	public void sendMessage(CommandSender sender,String node,Map<String, String> placeHolders)
	{
		List<String> message=new ArrayList<String>();
		for(String s:getNode(node))
		{
			for(String key:placeHolders.keySet())
				s=s.replace(key, placeHolders.get(key));
			if(sender instanceof Player)
			{
				Player player=(Player) sender;
				if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
					s=PlaceholderAPI.setPlaceholders(player, s);
				if(clanManager.isInClan(player.getName()))
				{
					s=s.replace("%clan%", clanManager.getClanData(player.getName()).getClanName());
					s=s.replace("%leader%", clanManager.getClanData(player.getName()).getClanLeader());
				}
			}
			message.add(s);
		}
		if(sender instanceof Player)
			MessageFormatter.sendJSONMessage((Player) sender, plugin, message);
		else
			for(String s:message)
				sender.sendMessage(CommonUtils.chat(s));
	}
	public void sendMessageNormal(CommandSender sender,String node,Map<String, String> placeHolders)
	{
		List<String> message=new ArrayList<String>();
		for(String s:getNode(node))
		{
			for(String key:placeHolders.keySet())
				s=s.replace(key, placeHolders.get(key));
			if(sender instanceof Player)
			{
				Player player=(Player) sender;
				if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
					s=PlaceholderAPI.setPlaceholders(player, s);
				if(clanManager.isInClan(player.getName()))
				{
					s=s.replace("%clan%", clanManager.getClanData(player.getName()).getClanName());
					s=s.replace("%leader%", clanManager.getClanData(player.getName()).getClanLeader());
				}
			}
			message.add(s);
		}
		for(String s:message)
			sender.sendMessage(CommonUtils.chat(s));
	}
	protected void broadcastMessage(CommandSender sender,String node,Map<String, String> placeHolders)
	{
		for(String s:getNode(node))
		{
			if(sender instanceof Player)
			{
				Player player=(Player) sender;
				if(clanManager.isInClan(player.getName()))
				{
					s=s.replace("%clan%", clanManager.getClanData(player.getName()).getClanName());
					s=s.replace("%leader%", clanManager.getClanData(player.getName()).getClanLeader());
				}
			}
			for(String key:placeHolders.keySet())
				s=s.replace(key, placeHolders.get(key));
			Bukkit.broadcastMessage(s);
		}
	}
	protected List<String> addPrefix(List<String> msg)
	{
		String prefix=plugin.getSettings().getFile().getString("settings.prefix");
		List<String> added=new ArrayList<>();
		for(String s:msg)
		{
			s=s.replace("%prefix%", prefix);
			added.add(CommonUtils.chat(s));
		}
		return added;
	}
	private List<String> getNode(String node)
	{
		if(!messages.containsKey(node))
			return new ArrayList<>();
		if(plugin.getMessages().isSet(messages.get(node)))
			return addPrefix(plugin.getMessages().getFile().getStringList(messages.get(node)));
		return new ArrayList<>();
	}
}
