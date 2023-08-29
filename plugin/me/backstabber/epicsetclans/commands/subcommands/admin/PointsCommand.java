package me.backstabber.epicsetclans.commands.subcommands.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.data.ClanPlayersData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.AdminCommands;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class PointsCommand extends AdminCommands
{
	private String name;
	public PointsCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager) 
	{
		super(plugin, clanManager, duelManager);
		this.name="points";
	}
	@Override
	public void onCommandByPlayer(Player sender, String[] sub) 
	{
		if(sub.length>2&&sub[1].equalsIgnoreCase("reset")&&Bukkit.getPlayerExact(sub[2])!=null)
		{
			Player player=Bukkit.getPlayerExact(sub[2]);
			if(clanManager.isInClan(player.getName()))
			{
				EpicClanData clan = (EpicClanData) clanManager.getClanData(player.getName());
				ClanPlayersData member = (ClanPlayersData) clan.getMemberData(player.getName());
				member.setPoints(0);
				clan.save();
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully Reset "+sub[2]+"'s points to 0."));
				player.sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan points have been reset."));
				return;
			}
			else
			{
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fThat player is not in a clan."));
				return;
			}
		}
		if(sub.length>3&&sub[1].equalsIgnoreCase("set")&&Bukkit.getPlayerExact(sub[2])!=null)
		{
			Player player=Bukkit.getPlayerExact(sub[2]);
			if(clanManager.isInClan(player.getName()))
			{
				EpicClanData clan = (EpicClanData) clanManager.getClanData(player.getName());
				ClanPlayersData member = (ClanPlayersData) clan.getMemberData(player.getName());
				int amount=0;
				try 
				{
					amount=Integer.valueOf(sub[3]);
				}
				catch(NullPointerException | NumberFormatException e)
				{
					sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlease enter correct amount to set."));
					return;
				}
				member.setPoints(Math.abs(amount));
				clan.save();
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully Set "+sub[2]+"'s points to "+Math.abs(amount)+"."));
				player.sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan points have been set to "+Math.abs(amount)+"."));
				return;
			}
			else
			{
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fThat player is not in a clan."));
				return;
			}
		}
		if(sub.length>3&&sub[1].equalsIgnoreCase("give")&&Bukkit.getPlayerExact(sub[2])!=null)
		{
			Player player=Bukkit.getPlayerExact(sub[2]);
			if(clanManager.isInClan(player.getName()))
			{
				EpicClanData clan = (EpicClanData) clanManager.getClanData(player.getName());
				ClanPlayersData member = (ClanPlayersData) clan.getMemberData(player.getName());
				int amount=0;
				try 
				{
					amount=Integer.valueOf(sub[3]);
				}
				catch(NullPointerException | NumberFormatException e)
				{
					sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlease enter correct amount to set."));
					return;
				}
				member.setPoints(member.getPoints()+Math.abs(amount));
				clan.save();
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully Given "+Math.abs(amount)+" points to "+sub[2]+"."));
				player.sendMessage(CommonUtils.chat("&b&lClans &7: &fYou were given "+Math.abs(amount)+" clan points."));
				return;
			}
			else
			{
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fThat player is not in a clan."));
				return;
			}
		}
		if(sub.length>3&&sub[1].equalsIgnoreCase("take")&&Bukkit.getPlayerExact(sub[2])!=null)
		{
			Player player=Bukkit.getPlayerExact(sub[2]);
			if(clanManager.isInClan(player.getName()))
			{
				EpicClanData clan = (EpicClanData) clanManager.getClanData(player.getName());
				ClanPlayersData member = (ClanPlayersData) clan.getMemberData(player.getName());
				int amount=0;
				try 
				{
					amount=Integer.valueOf(sub[3]);
				}
				catch(NullPointerException | NumberFormatException e)
				{
					sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlease enter correct amount to set."));
					return;
				}
				int total=member.getPoints()-Math.abs(amount);
				if(total<0)
					total=0;
				member.setPoints(total);
				clan.save();
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully taken "+Math.abs(amount)+" points to "+sub[2]+"."));
				player.sendMessage(CommonUtils.chat("&b&lClans &7: &fYou were taken "+Math.abs(amount)+" clan points."));
				return;
			}
			else
			{
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fThat player is not in a clan."));
				return;
			}
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin points give [player] [amount]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin points take [player] [amount]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin points set [player] [amount]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin points reset [player]"));
	}

	@Override
	public void onCommandByConsole(CommandSender sender, String[] sub) 
	{

		if(sub.length>2&&sub[1].equalsIgnoreCase("reset")&&Bukkit.getPlayerExact(sub[2])!=null)
		{
			Player player=Bukkit.getPlayerExact(sub[2]);
			if(clanManager.isInClan(player.getName()))
			{
				EpicClanData clan = (EpicClanData) clanManager.getClanData(player.getName());
				ClanPlayersData member = (ClanPlayersData) clan.getMemberData(player.getName());
				member.setPoints(0);
				clan.save();
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully Reset "+sub[2]+"'s points to 0."));
				player.sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan points have been reset."));
				return;
			}
			else
			{
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fThat player is not in a clan."));
				return;
			}
		}
		if(sub.length>3&&sub[1].equalsIgnoreCase("set")&&Bukkit.getPlayerExact(sub[2])!=null)
		{
			Player player=Bukkit.getPlayerExact(sub[2]);
			if(clanManager.isInClan(player.getName()))
			{
				EpicClanData clan = (EpicClanData) clanManager.getClanData(player.getName());
				ClanPlayersData member = (ClanPlayersData) clan.getMemberData(player.getName());
				int amount=0;
				try 
				{
					amount=Integer.valueOf(sub[3]);
				}
				catch(NullPointerException | NumberFormatException e)
				{
					sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlease enter correct amount to set."));
					return;
				}
				member.setPoints(Math.abs(amount));
				clan.save();
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully Set "+sub[2]+"'s points to "+Math.abs(amount)+"."));
				player.sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan points have been set to "+Math.abs(amount)+"."));
				return;
			}
			else
			{
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fThat player is not in a clan."));
				return;
			}
		}
		if(sub.length>3&&sub[1].equalsIgnoreCase("give")&&Bukkit.getPlayerExact(sub[2])!=null)
		{
			Player player=Bukkit.getPlayerExact(sub[2]);
			if(clanManager.isInClan(player.getName()))
			{
				EpicClanData clan = (EpicClanData) clanManager.getClanData(player.getName());
				ClanPlayersData member = (ClanPlayersData) clan.getMemberData(player.getName());
				int amount=0;
				try 
				{
					amount=Integer.valueOf(sub[3]);
				}
				catch(NullPointerException | NumberFormatException e)
				{
					sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlease enter correct amount to set."));
					return;
				}
				member.setPoints(member.getPoints()+Math.abs(amount));
				clan.save();
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully Given "+Math.abs(amount)+" points to "+sub[2]+"."));
				player.sendMessage(CommonUtils.chat("&b&lClans &7: &fYou were given "+Math.abs(amount)+" clan points."));
				return;
			}
			else
			{
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fThat player is not in a clan."));
				return;
			}
		}
		if(sub.length>3&&sub[1].equalsIgnoreCase("take")&&Bukkit.getPlayerExact(sub[2])!=null)
		{
			Player player=Bukkit.getPlayerExact(sub[2]);
			if(clanManager.isInClan(player.getName()))
			{
				EpicClanData clan = (EpicClanData) clanManager.getClanData(player.getName());
				ClanPlayersData member = (ClanPlayersData) clan.getMemberData(player.getName());
				int amount=0;
				try 
				{
					amount=Integer.valueOf(sub[3]);
				}
				catch(NullPointerException | NumberFormatException e)
				{
					sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlease enter correct amount to set."));
					return;
				}
				int total=member.getPoints()-Math.abs(amount);
				if(total<0)
					total=0;
				member.setPoints(total);
				clan.save();
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully taken "+Math.abs(amount)+" points to "+sub[2]+"."));
				player.sendMessage(CommonUtils.chat("&b&lClans &7: &fYou were taken "+Math.abs(amount)+" clan points."));
				return;
			}
			else
			{
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fThat player is not in a clan."));
				return;
			}
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin points give [player] [amount]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin points take [player] [amount]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin points set [player] [amount]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin points reset [player]"));
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
			List<String> cmd=new ArrayList<>();
			result.add("give");
			result.add("take");
			result.add("set");
			result.add("reset");
			StringUtil.copyPartialMatches(hint, cmd, result);
			Collections.sort(result);
	        return result;
		}
		if(length==3)
		{
			List<String> result=new ArrayList<>();
			List<String> players=new ArrayList<>();
			for(Player player:Bukkit.getOnlinePlayers())
				if(clanManager.isInClan(player.getName()))
					players.add(player.getName());
			StringUtil.copyPartialMatches(hint, players, result);
			Collections.sort(result);
	        return result;
		}
		return new ArrayList<>();
	}

}
