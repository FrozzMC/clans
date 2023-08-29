package me.backstabber.epicsetclans.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.SubCommands;
import me.backstabber.epicsetclans.commands.subcommands.clan.*;
import me.backstabber.epicsetclans.messager.MessageFormatter;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class ClanCommand implements CommandExecutor, TabCompleter
{
	private EpicSetClans plugin;
	private EpicClanManager clanManager;
	private ClanDuelManager duelManager;
	private List<SubCommands> subCommands=new ArrayList<SubCommands>();
	public ClanCommand(final EpicSetClans plugin, final EpicClanManager clanManager, final ClanDuelManager duelManager)
	{
		this.plugin = plugin;
		this.clanManager = clanManager;
		this.duelManager = duelManager;
	}

	public void setup() {
		subCommands.add(new AcceptCommand(plugin, clanManager, duelManager));
		subCommands.add(new AllyCommand(plugin, clanManager, duelManager));
		subCommands.add(new BankCommand(plugin, clanManager, duelManager));
		subCommands.add(new BaseCommand(plugin, clanManager, duelManager));
		subCommands.add(new ChatCommand(plugin, clanManager, duelManager));
		subCommands.add(new CommentCommand(plugin, clanManager, duelManager));
		subCommands.add(new CreateCommand(plugin, clanManager, duelManager));
		subCommands.add(new DelBaseCommand(plugin, clanManager, duelManager));
		subCommands.add(new DepositCommand(plugin, clanManager, duelManager));
		subCommands.add(new DuelCommand(plugin, clanManager, duelManager));
		subCommands.add(new HelpCommand(plugin, clanManager, duelManager));
		subCommands.add(new InviteCommand(plugin, clanManager, duelManager));
		subCommands.add(new KickCommand(plugin, clanManager, duelManager));
		subCommands.add(new LeaveCommand(plugin, clanManager, duelManager));
		subCommands.add(new MakeLeaderCommand(plugin, clanManager, duelManager));
		subCommands.add(new ManageCommand(plugin, clanManager, duelManager));
		subCommands.add(new PointsCommand(plugin, clanManager, duelManager));
		subCommands.add(new RemoveCommand(plugin, clanManager, duelManager));
		subCommands.add(new RenameCommand(plugin, clanManager, duelManager));
		subCommands.add(new RespectCommand(plugin, clanManager, duelManager));
		subCommands.add(new SetBaseCommand(plugin, clanManager, duelManager));
		subCommands.add(new ShopCommand(plugin, clanManager, duelManager));
		subCommands.add(new StatsCommand(plugin, clanManager, duelManager));
		subCommands.add(new TagCommand(plugin, clanManager, duelManager));
		subCommands.add(new TopCommand(plugin, clanManager, duelManager));
		subCommands.add(new TruceCommand(plugin, clanManager, duelManager));
		subCommands.add(new VaultsCommand(plugin, clanManager, duelManager));
		subCommands.add(new WithdrawCommand(plugin, clanManager, duelManager));
		subCommands.add(new SetTagCommand(plugin, clanManager, duelManager));
		subCommands.add(new DeleteCommand(plugin, clanManager, duelManager));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String a, String[] sub) 
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage(CommonUtils.chat("&cCant use this command from console.&aTry /clanadmin instead."));
			return true;
		}
		
		if(sub.length==0)
		{
			List<String> message=new ArrayList<>();
			for(String s:plugin.getMessages().getFile().getStringList("help.incorrect"))
			{
				message.add(CommonUtils.chat(s.replace("%prefix%", plugin.getSettings().getFile().getString("settings.prefix"))));
			}
			MessageFormatter.sendJSONMessage((Player) sender, plugin, message);
			new BukkitRunnable() {
				
				@Override
				public void run() {
					new HelpCommand(plugin, clanManager, duelManager).sendMessage(sender, "detailed", new HashMap<String, String>());
					
				}
			}.runTaskLater(plugin, 10);
			return true;
		}
		
		for(SubCommands subs:subCommands)
			if(sub[0].equalsIgnoreCase(subs.getName()) || subs.getAliases().contains(sub[0].toLowerCase()))
			{
				if(!subs.hasPermission(sender))
					sender.sendMessage(CommonUtils.chat("&cYou dont have permission.")); 
				else
					subs.onCommandByPlayer((Player) sender, sub);
				return true;
			}

		List<String> message=new ArrayList<>();
		for(String s:plugin.getMessages().getFile().getStringList("help.incorrect"))
		{
			message.add(CommonUtils.chat(s.replace("%prefix%", plugin.getSettings().getFile().getString("settings.prefix"))));
		}
		MessageFormatter.sendJSONMessage((Player) sender, plugin, message);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String a, String[] sub) 
	{
		if(!(sender instanceof Player))
			return new ArrayList<>();
		if(sub.length==1)
		{
			List<String> subs=new ArrayList<String>();
			List<String> result=new ArrayList<>();
			for(SubCommands command:subCommands)
			{
				subs.addAll(command.getAliases());
			}
			StringUtil.copyPartialMatches(sub[0], subs, result);
			Collections.sort(result);
			return result;
		}
		else if(sub.length>1)
		{
			for(SubCommands command:subCommands)
			{
				if(command.getAliases().contains(sub[0].toLowerCase()))
				{
					return command.getCompletor((Player) sender, sub.length, sub[sub.length-1]);
				}
			}
		}
		return new ArrayList<>();
	}
	
}