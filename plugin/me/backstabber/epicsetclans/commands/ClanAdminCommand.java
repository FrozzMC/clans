package me.backstabber.epicsetclans.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.AdminCommands;
import me.backstabber.epicsetclans.commands.subcommands.admin.*;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class ClanAdminCommand implements CommandExecutor, TabCompleter
{
	@SuppressWarnings("unused")
	private ArenaCommand arenaCommand;
	private EpicSetClans plugin;
	private EpicClanManager clanManager;
	private ClanDuelManager duelManager;
	private List<AdminCommands> subCommands;
	public ClanAdminCommand(
		final ArenaCommand arenaCommand, 
		final EpicSetClans plugin,
		final EpicClanManager clanManager,
		final ClanDuelManager duelManager
	) {
		this.arenaCommand=arenaCommand;
		this.plugin = plugin;
		this.clanManager = clanManager;
		this.duelManager = duelManager;
		subCommands = new ArrayList<AdminCommands>();
	}
	public void setup()
	{
		subCommands.add(new BankCommand(plugin, clanManager, duelManager));
		subCommands.add(new DeleteCommand(plugin, clanManager, duelManager));
		subCommands.add(new HelpCommand(plugin, clanManager, duelManager));
		subCommands.add(new PointsCommand(plugin, clanManager, duelManager));
		subCommands.add(new RefreshCommand(plugin, clanManager, duelManager));
		subCommands.add(new ReloadCommand(plugin, clanManager, duelManager));
		subCommands.add(new RenameCommand(plugin, clanManager, duelManager));
		subCommands.add(new ResetCommand(plugin, clanManager, duelManager));
		subCommands.add(new RespectCommand(plugin, clanManager, duelManager));
		subCommands.add(new SpyCommand(plugin, clanManager, duelManager));
		subCommands.add(new TeamCommand(plugin, clanManager, duelManager));
		subCommands.add(new VaultCommand(plugin, clanManager, duelManager));
	  	subCommands.add(arenaCommand);
		subCommands.add(new KitCommand(plugin, clanManager, duelManager));
		subCommands.add(new RewardCommand(plugin, clanManager, duelManager));
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String a, String[] sub) 
	{
		if(!sender.hasPermission("epicsetclans.admin"))
		{
			sender.sendMessage(CommonUtils.chat("&cYou dont have permission."));
			return true;
		}
		if(sub.length==0)
		{
			sender.sendMessage(CommonUtils.chat("&bClanAdmin &7:&fIncorrect command. Type /clanadmin help."));
			return true;
		}
		for(AdminCommands subs:subCommands)
			if(sub[0].equalsIgnoreCase(subs.getName())||subs.getAliases().contains(sub[0].toLowerCase()))
			{
				if(subs.hasPermission(sender))
				{
					if(sender instanceof Player)
						subs.onCommandByPlayer((Player) sender, sub);
					else
						subs.onCommandByConsole(sender, sub);
				}
				else
				{
					sender.sendMessage(CommonUtils.chat("&cYou dont have permission."));
				}
				return true;
			}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7:&fIncorrect command. Type /clanadmin help."));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String a, String[] sub) 
	{
		if(sub.length==1)
		{
			List<String> subs=new ArrayList<String>();
			List<String> result=new ArrayList<>();
			for(AdminCommands command:subCommands)
			{
				subs.addAll(command.getAliases());
			}
			StringUtil.copyPartialMatches(sub[0], subs, result);
			Collections.sort(result);
			return result;
		}
		else if(sub.length>1)
		{
			for(AdminCommands command:subCommands)
			{
				if(command.getAliases().contains(sub[0].toLowerCase()))
				{
					return command.getCompletor(sub.length, sub[sub.length-1]);
				}
			}
		}
		return new ArrayList<>();
	}
	
}