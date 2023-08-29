package me.backstabber.epicsetclans.commands.subcommands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;


import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;


public abstract class AdminCommands 
{
	protected EpicSetClans plugin;
	protected EpicClanManager clanManager;
	protected ClanDuelManager duelManager;
	public AdminCommands(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager) 
	{
		this.plugin=plugin;
		this.clanManager=clanManager;
		this.duelManager=duelManager;
	}
	public abstract void onCommandByPlayer(Player sender ,String[] sub);
	public abstract void onCommandByConsole(CommandSender sender ,String[] sub);
	public abstract String getName();
	public abstract List<String> getAliases();
	public abstract List<String> getCompletor(int length, String hint);
	public boolean hasPermission(Permissible sender)
	{
		if(sender.hasPermission("epicsetclans.admin.*"))
			return true;
		if(sender.hasPermission("epicsetclans.admin."+getName()))
			return true;
		return false;
	}
}
