package me.backstabber.epicsetclans.commands.subcommands.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.data.ClanData;
import me.backstabber.epicsetclans.api.data.PlayerData;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.data.ClanPlayersData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.AdminCommands;
import me.backstabber.epicsetclans.enums.ClanNodes;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class ResetCommand extends AdminCommands
{
	private String name;
	public ResetCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager) 
	{
		super(plugin, clanManager, duelManager);
		this.name="reset";
	}
	@Override
	public void onCommandByPlayer(Player sender, String[] sub) 
	{
		if(sub.length>2&&sub[1].equalsIgnoreCase("all"))
		{
			String clanName = sub[2];
            for (int i = 3; i < sub.length; i++)
            	clanName = clanName.concat(" " + sub[i]); 
            if(clanManager.getClanData(clanName)!=null)
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
            	clan.setMemberUpgrade((int) ClanNodes.UPGRADE_MEMBERS.value());
            	clan.setHomeUpgrade((int) ClanNodes.UPGRADE_HOME.value());
            	clan.setVaultUpgrade((int) ClanNodes.UPGRADE_VAULTS.value());
            	clan.setDuelsUpgrade((int) ClanNodes.UPGRADE_DUELS.value());
            	clan.setWonDuels(0);
            	clan.setLostDuels(0);
            	clan.setClanBalance(0);
            	for(PlayerData member:clan.getMembersData().values())
            	{
            		member.setDeaths(0);
            		member.setKills(0);
            		member.setPoints(0);
            		((ClanPlayersData) member).setDuelsPlayed(0);
            		((ClanPlayersData) member).setDuelsWon(0);
            		((ClanPlayersData) member).setPermissions(new ArrayList<>());
            	}
            	clan.save();
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully reset all stats of "+clan.getClanName()+"&f."));
            	for(String s:clan.getClanMembersName())
            		if(Bukkit.getPlayerExact(s)!=null)
            			Bukkit.getPlayerExact(s).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan stats have been reset by "+sender.getName()));
    			return;
            }
            else
            {
	            sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find any clan by that name."));
	            return;
            }
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("upgrades"))
		{
			String clanName = sub[2];
            for (int i = 3; i < sub.length; i++)
            	clanName = clanName.concat(" " + sub[i]); 
            if(clanManager.getClanData(clanName)!=null)
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
            	clan.setMemberUpgrade((int) ClanNodes.UPGRADE_MEMBERS.value());
            	clan.setHomeUpgrade((int) ClanNodes.UPGRADE_HOME.value());
            	clan.setVaultUpgrade((int) ClanNodes.UPGRADE_VAULTS.value());
            	clan.setDuelsUpgrade((int) ClanNodes.UPGRADE_DUELS.value());
            	clan.save();
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully reset Upgrades of "+clan.getClanName()+"&f."));
            	for(String s:clan.getClanMembersName())
            		if(Bukkit.getPlayerExact(s)!=null)
            			Bukkit.getPlayerExact(s).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan upgrades have been reset by "+sender.getName()));
    			return;
            }
            else
            {
	            sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find any clan by that name."));
	            return;
            }
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("duel"))
		{
			String clanName = sub[2];
            for (int i = 3; i < sub.length; i++)
            	clanName = clanName.concat(" " + sub[i]); 
            if(clanManager.getClanData(clanName)!=null)
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
            	clan.setWonDuels(0);
            	clan.setLostDuels(0);
            	for(PlayerData member:clan.getMembersData().values())
            	{
            		((ClanPlayersData) member).setDuelsPlayed(0);
            		((ClanPlayersData) member).setDuelsWon(0);
            	}
            	clan.save();
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully reset Duels of "+clan.getClanName()+"&f."));
            	for(String s:clan.getClanMembersName())
            		if(Bukkit.getPlayerExact(s)!=null)
            			Bukkit.getPlayerExact(s).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan duels have been reset by "+sender.getName()));
    			return;
            }
            else
            {
	            sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find any clan by that name."));
	            return;
            }
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("duelwin"))
		{
			String clanName = sub[2];
            for (int i = 3; i < sub.length; i++)
            	clanName = clanName.concat(" " + sub[i]); 
            if(clanManager.getClanData(clanName)!=null)
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
            	clan.setWonDuels(0);
            	for(PlayerData member:clan.getMembersData().values())
            	{
            		((ClanPlayersData) member).setDuelsWon(0);
            	}
            	clan.save();
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully reset Won Duels of "+clan.getClanName()+"&f."));
            	for(String s:clan.getClanMembersName())
            		if(Bukkit.getPlayerExact(s)!=null)
            			Bukkit.getPlayerExact(s).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan duel wins have been reset by "+sender.getName()));
    			return;
            }
            else
            {
	            sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find any clan by that name."));
	            return;
            }
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("duellose"))
		{
			String clanName = sub[2];
            for (int i = 3; i < sub.length; i++)
            	clanName = clanName.concat(" " + sub[i]); 
            if(clanManager.getClanData(clanName)!=null)
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
            	clan.setLostDuels(0);
            	clan.save();
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully reset Lost Duels of "+clan.getClanName()+"&f."));
            	for(String s:clan.getClanMembersName())
            		if(Bukkit.getPlayerExact(s)!=null)
            			Bukkit.getPlayerExact(s).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan lost duels have been reset by "+sender.getName()));
    			return;
            }
            else
            {
	            sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find any clan by that name."));
	            return;
            }
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("stats"))
		{
			String player=sub[2];
            if(clanManager.isInClan(player))
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(player);
            	ClanPlayersData member = (ClanPlayersData) clan.getMemberData(player);
            	member.setDeaths(0);
        		member.setKills(0);
        		member.setPoints(0);
        		member.setDuelsPlayed(0);
        		member.setDuelsWon(0);
        		member.setPermissions(new ArrayList<>());
        		clan.save();
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully reset stats of "+player+"&f."));
        		if(Bukkit.getPlayerExact(player)!=null)
        			Bukkit.getPlayerExact(player).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan stats have been reset by "+sender.getName()));
    			return;
            }
            else
            {
	            sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlayer is not in a clan."));
	            return;
            }
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("kills"))
		{
			
			String player=sub[2];
            if(clanManager.isInClan(player))
            {
            	
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(player);
            	ClanPlayersData member = (ClanPlayersData) clan.getMemberData(player);
        		member.setKills(0);
        		clan.save();
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully reset kills of "+player+"&f."));
            	if(Bukkit.getPlayerExact(player)!=null)
            		Bukkit.getPlayerExact(player).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan kills have been reset by "+sender.getName()));
    			return;
            }
            else
            {
	            sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlayer is not in a clan."));
	            return;
            }
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("deaths"))
		{
			
			String player=sub[2];
            if(clanManager.isInClan(player))
            {
            	
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(player);
            	ClanPlayersData member = (ClanPlayersData) clan.getMemberData(player);
        		member.setDeaths(0);
        		clan.save();
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully reset deaths of "+player+"&f."));
            	if(Bukkit.getPlayerExact(player)!=null)
            		Bukkit.getPlayerExact(player).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan deaths have been reset by "+sender.getName()));
    			return;
            }
            else
            {
	            sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlayer is not in a clan."));
	            return;
            }
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reset all [clanname/membername]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reset upgrades [clanname/membername]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reset duel [clanname/membername]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reset duelwin [clanname/membername]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reset duellose [clanname/membername]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reset stats [membername]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reset kills [membername]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reset deaths [membername]"));
	}

	@Override
	public void onCommandByConsole(CommandSender sender, String[] sub) 
	{

		if(sub.length>2&&sub[1].equalsIgnoreCase("all"))
		{
			String clanName = sub[2];
            for (int i = 3; i < sub.length; i++)
            	clanName = clanName.concat(" " + sub[i]); 
            if(clanManager.getClanData(clanName)!=null)
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
            	clan.setMemberUpgrade((int) ClanNodes.UPGRADE_MEMBERS.value());
            	clan.setHomeUpgrade((int) ClanNodes.UPGRADE_HOME.value());
            	clan.setVaultUpgrade((int) ClanNodes.UPGRADE_VAULTS.value());
            	clan.setDuelsUpgrade((int) ClanNodes.UPGRADE_DUELS.value());
            	clan.setWonDuels(0);
            	clan.setLostDuels(0);
            	clan.setClanBalance(0);
            	for(PlayerData member:clan.getMembersData().values())
            	{
            		member.setDeaths(0);
            		member.setKills(0);
            		member.setPoints(0);
            		((ClanPlayersData) member).setDuelsPlayed(0);
            		((ClanPlayersData) member).setDuelsWon(0);
            		((ClanPlayersData) member).setPermissions(new ArrayList<>());
            	}
            	clan.save();
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully reset all stats of "+clan.getClanName()+"&f."));
            	for(String s:clan.getClanMembersName())
            		if(Bukkit.getPlayerExact(s)!=null)
            			Bukkit.getPlayerExact(s).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan stats have been reset by "+sender.getName()));
    			return;
            }
            else
            {
	            sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find any clan by that name."));
	            return;
            }
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("upgrades"))
		{
			String clanName = sub[2];
            for (int i = 3; i < sub.length; i++)
            	clanName = clanName.concat(" " + sub[i]); 
            if(clanManager.getClanData(clanName)!=null)
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
            	clan.setMemberUpgrade((int) ClanNodes.UPGRADE_MEMBERS.value());
            	clan.setHomeUpgrade((int) ClanNodes.UPGRADE_HOME.value());
            	clan.setVaultUpgrade((int) ClanNodes.UPGRADE_VAULTS.value());
            	clan.setDuelsUpgrade((int) ClanNodes.UPGRADE_DUELS.value());
            	clan.save();
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully reset Upgrades of "+clan.getClanName()+"&f."));
            	for(String s:clan.getClanMembersName())
            		if(Bukkit.getPlayerExact(s)!=null)
            			Bukkit.getPlayerExact(s).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan upgrades have been reset by "+sender.getName()));
    			return;
            }
            else
            {
	            sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find any clan by that name."));
	            return;
            }
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("duel"))
		{
			String clanName = sub[2];
            for (int i = 3; i < sub.length; i++)
            	clanName = clanName.concat(" " + sub[i]); 
            if(clanManager.getClanData(clanName)!=null)
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
            	clan.setWonDuels(0);
            	clan.setLostDuels(0);
            	for(PlayerData member:clan.getMembersData().values())
            	{
            		((ClanPlayersData) member).setDuelsPlayed(0);
            		((ClanPlayersData) member).setDuelsWon(0);
            	}
            	clan.save();
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully reset Duels of "+clan.getClanName()+"&f."));
            	for(String s:clan.getClanMembersName())
            		if(Bukkit.getPlayerExact(s)!=null)
            			Bukkit.getPlayerExact(s).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan duels have been reset by "+sender.getName()));
    			return;
            }
            else
            {
	            sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find any clan by that name."));
	            return;
            }
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("duelwin"))
		{
			String clanName = sub[2];
            for (int i = 3; i < sub.length; i++)
            	clanName = clanName.concat(" " + sub[i]); 
            if(clanManager.getClanData(clanName)!=null)
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
            	clan.setWonDuels(0);
            	for(PlayerData member:clan.getMembersData().values())
            	{
            		((ClanPlayersData) member).setDuelsWon(0);
            	}
            	clan.save();
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully reset Won Duels of "+clan.getClanName()+"&f."));
            	for(String s:clan.getClanMembersName())
            		if(Bukkit.getPlayerExact(s)!=null)
            			Bukkit.getPlayerExact(s).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan duel wins have been reset by "+sender.getName()));
    			return;
            }
            else
            {
	            sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find any clan by that name."));
	            return;
            }
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("duellose"))
		{
			String clanName = sub[2];
            for (int i = 3; i < sub.length; i++)
            	clanName = clanName.concat(" " + sub[i]); 
            if(clanManager.getClanData(clanName)!=null)
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
            	clan.setLostDuels(0);
            	clan.save();
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully reset Lost Duels of "+clan.getClanName()+"&f."));
            	for(String s:clan.getClanMembersName())
            		if(Bukkit.getPlayerExact(s)!=null)
            			Bukkit.getPlayerExact(s).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan lost duels have been reset by "+sender.getName()));
    			return;
            }
            else
            {
	            sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find any clan by that name."));
	            return;
            }
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("stats"))
		{
			String player=sub[2];
            if(clanManager.isInClan(player))
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(player);
            	ClanPlayersData member = (ClanPlayersData) clan.getMemberData(player);
            	member.setDeaths(0);
        		member.setKills(0);
        		member.setPoints(0);
        		member.setDuelsPlayed(0);
        		member.setDuelsWon(0);
        		member.setPermissions(new ArrayList<>());
        		clan.save();
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully reset stats of "+player+"&f."));
        		if(Bukkit.getPlayerExact(player)!=null)
        			Bukkit.getPlayerExact(player).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan stats have been reset by "+sender.getName()));
    			return;
            }
            else
            {
	            sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlayer is not in a clan."));
	            return;
            }
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("kills"))
		{
			
			String player=sub[2];
            if(clanManager.isInClan(player))
            {
            	
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(player);
            	ClanPlayersData member = (ClanPlayersData) clan.getMemberData(player);
        		member.setKills(0);
        		clan.save();
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully reset kills of "+player+"&f."));
            	if(Bukkit.getPlayerExact(player)!=null)
            		Bukkit.getPlayerExact(player).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan kills have been reset by "+sender.getName()));
    			return;
            }
            else
            {
	            sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlayer is not in a clan."));
	            return;
            }
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("deaths"))
		{
			
			String player=sub[2];
            if(clanManager.isInClan(player))
            {
            	
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(player);
            	ClanPlayersData member = (ClanPlayersData) clan.getMemberData(player);
        		member.setDeaths(0);
        		clan.save();
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully reset deaths of "+player+"&f."));
            	if(Bukkit.getPlayerExact(player)!=null)
            		Bukkit.getPlayerExact(player).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan deaths have been reset by "+sender.getName()));
    			return;
            }
            else
            {
	            sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlayer is not in a clan."));
	            return;
            }
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reset all [clanname/membername]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reset upgrades [clanname/membername]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reset duel [clanname/membername]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reset duelwin [clanname/membername]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reset duellose [clanname/membername]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reset stats [membername]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reset kills [membername]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reset deaths [membername]"));
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
			result.add("all");
			result.add("upgrades");
			result.add("duel");
			result.add("duelwin");
			result.add("duellose");
			result.add("stats");
			result.add("kills");
			result.add("deaths");
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
			List<String> clans=new ArrayList<>();
			for(ClanData clan : clanManager.getAllClans())
				clans.add(clan.getClanNameRaw());
			StringUtil.copyPartialMatches(hint, clans, result);
			Collections.sort(result);
	        return result;
		}
		return new ArrayList<>();
	}

}
