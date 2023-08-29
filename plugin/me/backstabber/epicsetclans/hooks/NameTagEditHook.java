package me.backstabber.epicsetclans.hooks;


import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import com.nametagedit.plugin.NametagEdit;
import com.nametagedit.plugin.api.INametagApi;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.events.ClanCeateEvent;
import me.backstabber.epicsetclans.api.events.ClanDeleteEvent;
import me.backstabber.epicsetclans.api.events.ClanJoinEvent;
import me.backstabber.epicsetclans.api.events.ClanKickEvent;
import me.backstabber.epicsetclans.api.events.ClanLeaveEvent;
import me.backstabber.epicsetclans.api.events.ClanRenameEvent;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.utils.CommonUtils;


public class NameTagEditHook implements Listener
{
	private EpicSetClans plugin;
	public NameTagEditHook(final EpicSetClans plugin)  
	{
		this.plugin = plugin;
		ConsoleCommandSender console = Bukkit.getConsoleSender();
		console.sendMessage(CommonUtils.chat("&a[EpicSet-Clans] >&eFound NametagEdit. &rHooking into it."));
	}
	@EventHandler
	public void onCreate(ClanCeateEvent event)
	{
		INametagApi api = NametagEdit.getApi();
		new BukkitRunnable() 
		{
			@Override
			public void run() 
			{
				api.reloadNametag(event.getCreator());
			}
		}.runTaskLater(plugin, 2);
	}
	@EventHandler
	public void onRename(ClanRenameEvent event)
	{
		INametagApi api = NametagEdit.getApi();
		new BukkitRunnable() 
		{
			@Override
			public void run() 
			{
				for(String s:((EpicClanData) event.getClan()).getClanMembersName())
				{
					if(Bukkit.getPlayerExact(s)!=null)
						api.reloadNametag(Bukkit.getPlayerExact(s));
				}
			}
		}.runTaskLater(plugin, 2);
	}
	@EventHandler
	public void onJoin(ClanJoinEvent event)
	{
		INametagApi api = NametagEdit.getApi();
		new BukkitRunnable() 
		{
			@Override
			public void run() 
			{
				api.reloadNametag(event.getJoiner());
			}
		}.runTaskLater(plugin, 2);
	}
	@EventHandler
	public void onLeave(ClanLeaveEvent event)
	{
		INametagApi api = NametagEdit.getApi();
		new BukkitRunnable() 
		{
			@Override
			public void run() 
			{
				if(event.getLeaver()!=null)
					api.reloadNametag(event.getLeaver());
				for(String s:((EpicClanData) event.getClan()).getClanMembersName())
				{
					if(Bukkit.getPlayerExact(s)!=null)
						api.reloadNametag(Bukkit.getPlayerExact(s));
				}
			}
		}.runTaskLater(plugin, 2);
	}
	@EventHandler
	public void onKick(ClanKickEvent event)
	{
		INametagApi api = NametagEdit.getApi();
		new BukkitRunnable() 
		{
			@Override
			public void run() 
			{
				if(Bukkit.getPlayer(event.getKickedPlayer())!=null)
					api.reloadNametag(Bukkit.getPlayer(event.getKickedPlayer()));
			}
		}.runTaskLater(plugin, 2);
	}
	@EventHandler
	public void onDelete(ClanDeleteEvent event)
	{
		INametagApi api = NametagEdit.getApi();
		new BukkitRunnable() 
		{
			@Override
			public void run() 
			{
				for(String s:((EpicClanData) event.getClan()).getClanMembersName())
				{
					if(Bukkit.getPlayerExact(s)!=null)
						api.reloadNametag(Bukkit.getPlayerExact(s));
				}
			}
		}.runTaskLater(plugin, 2);
	}

}
