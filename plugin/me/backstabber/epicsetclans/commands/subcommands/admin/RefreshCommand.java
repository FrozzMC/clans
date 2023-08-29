package me.backstabber.epicsetclans.commands.subcommands.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.clanhandles.manager.YMLManager;
import me.backstabber.epicsetclans.commands.subcommands.AdminCommands;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class RefreshCommand extends AdminCommands
{
	private String name;
	public RefreshCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager) 
	{
		super(plugin, clanManager, duelManager);
		this.name="refresh";
	}
	@Override
	public void onCommandByPlayer(Player sender, String[] sub) 
	{
		Map<String, YMLManager> files = plugin.getFiles();
		if(sub.length>1)
		{
			String fileName=sub[1];
			if(fileName.equalsIgnoreCase("all"))
			{
				sender.sendMessage(CommonUtils.chat("&aRefreshing all yml files."));
				new BukkitRunnable() 
				{
					@Override
					public void run()
					{
						for(String file:files.keySet())
						{
							files.get(file).refresh();
							sender.sendMessage(CommonUtils.chat("&aRefreshed &a"+file+"&a sucessfully."));
							try 
							{
								Thread.sleep(100);
							} catch (InterruptedException e) 
							{
								
							}
						}
						sender.sendMessage(CommonUtils.chat("&aAll files refreshed."));
						plugin.updateCosts();
					}
				}.runTaskAsynchronously(plugin);
				return;
			}
			else if(files.containsKey(fileName.toLowerCase()))
			{
				YMLManager file = files.get(fileName.toLowerCase());
				file.refresh();
				sender.sendMessage(CommonUtils.chat("aRefreshed &a"+fileName.toLowerCase()+"&a sucessfully."));
				if(fileName.equalsIgnoreCase("config"))
					plugin.updateCosts();
				return;
			}
			else
			{

				sender.sendMessage(CommonUtils.chat("&cUnable to find that file."));
				return;
			}
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin refresh all/filename"));
	}

	@Override
	public void onCommandByConsole(CommandSender sender, String[] sub) 
	{
		Map<String, YMLManager> files = plugin.getFiles();
		if(sub.length>1)
		{
			String fileName=sub[1];
			if(fileName.equalsIgnoreCase("all"))
			{
				sender.sendMessage(CommonUtils.chat("&aRefreshing all yml files."));
				new BukkitRunnable() 
				{
					@Override
					public void run()
					{
						for(String file:files.keySet())
						{
							files.get(file).refresh();
							sender.sendMessage(CommonUtils.chat("&aRefreshed &a"+file+"&a sucessfully."));
							try 
							{
								Thread.sleep(100);
							} catch (InterruptedException e) 
							{
								
							}
						}
						sender.sendMessage(CommonUtils.chat("&aAll files refreshed."));
					}
				}.runTaskAsynchronously(plugin);
				return;
			}
			else if(files.containsKey(fileName.toLowerCase()))
			{
				YMLManager file = files.get(fileName.toLowerCase());
				file.refresh();
				sender.sendMessage(CommonUtils.chat("aRefreshed &a"+fileName.toLowerCase()+"&a sucessfully."));
				return;
			}
			else
			{

				sender.sendMessage(CommonUtils.chat("&cUnable to find that file."));
				return;
			}
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin refresh all/filename"));
	}

	@Override
	public String getName() 
	{
		return this.name;
	}

	@Override
	public List<String> getAliases() 
	{
		List<String> aliases=new ArrayList<String>();
		aliases.add(name);
		return aliases;
	}

	@Override
	public List<String> getCompletor(int length, String hint) 
	{
		if(length==1)
			return new ArrayList<>();
		if(length==2)
		{
			List<String> result=new ArrayList<>();
			List<String> names=new ArrayList<>();
			names.add("all");
			for(String fileName:plugin.getFiles().keySet())
				names.add(fileName);
			StringUtil.copyPartialMatches(hint, names, result);
			Collections.sort(result);
	        return result;
		}
		return new ArrayList<>();
	}

}
