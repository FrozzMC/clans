package me.backstabber.epicsetclans.commands.subcommands.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.AdminCommands;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class RewardCommand extends AdminCommands
{
	private String name;
	public RewardCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager) 
	{
		super(plugin, clanManager, duelManager);
		this.name="reward";
	}
	@Override
	public void onCommandByPlayer(Player sender, String[] sub) 
	{
		if(sub.length==2&&sub[1].equalsIgnoreCase("execute"))
		{
			plugin.getMonthlyRewardManager().runReward();
			sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fExecuting monthly rewards."));
			return;
		}
		if(sub.length==2&&sub[1].equalsIgnoreCase("unrewarded"))
		{
			List<String> unrewarded = plugin.getMonthlyRewardManager().getUnrewarded();
			sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fFollowing players were not rewarded."));
			int index=1;
			for(String s:unrewarded)
				sender.sendMessage(CommonUtils.chat("&7"+index+"&f) "+s));
			return;
		}
		if(sub.length==3&&sub[1].equalsIgnoreCase("give")&&Bukkit.getPlayerExact(sub[2])!=null)
		{
			plugin.getMonthlyRewardManager().rewardPlayer(Bukkit.getPlayerExact(sub[2]));
			sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fRewarding player "+sub[2]));
			return;
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reward execute   &7Run monthly rewards for this month"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reward unrewarded   &7list unrewarded players this month."));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reward give [playerName]   &7Give reward to unrewarded player."));
	}

	@Override
	public void onCommandByConsole(CommandSender sender, String[] sub) 
	{
		if(sub.length==2&&sub[1].equalsIgnoreCase("execute"))
		{
			plugin.getMonthlyRewardManager().runReward();
			sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fExecuting monthly rewards."));
			return;
		}
		if(sub.length==2&&sub[1].equalsIgnoreCase("unrewarded"))
		{
			List<String> unrewarded = plugin.getMonthlyRewardManager().getUnrewarded();
			sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fFollowing players were not rewarded."));
			int index=1;
			for(String s:unrewarded)
				sender.sendMessage(CommonUtils.chat("&7"+index+"&f) "+s));
			return;
		}
		if(sub.length==3&&sub[1].equalsIgnoreCase("give")&&Bukkit.getPlayerExact(sub[2])!=null)
		{
			plugin.getMonthlyRewardManager().rewardPlayer(Bukkit.getPlayerExact(sub[2]));
			sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fRewarding player "+sub[2]));
			return;
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reward execute   &7Run monthly rewards for this month"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reward unrewarded   &7list unrewarded players this month."));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reward give [playerName]   &7Give reward to unrewarded player."));
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
		if(length==2)
		{
			List<String> result=new ArrayList<>();
			List<String> names=new ArrayList<>();
			names.add("execute");
			names.add("unrewarded");
			names.add("give");
			StringUtil.copyPartialMatches(hint, names, result);
			Collections.sort(result);
	        return result;
		}
		if(length==3)
		{
			List<String> result=new ArrayList<>();
			List<String> names=new ArrayList<>();
			for(Player player:Bukkit.getOnlinePlayers())
				names.add(player.getName());
			StringUtil.copyPartialMatches(hint, names, result);
			Collections.sort(result);
	        return result;
		}
		return new ArrayList<>();
	}

}
