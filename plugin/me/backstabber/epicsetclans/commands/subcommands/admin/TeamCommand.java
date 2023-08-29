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
import me.backstabber.epicsetclans.api.events.ClanJoinEvent;
import me.backstabber.epicsetclans.api.events.ClanLeaveEvent;
import me.backstabber.epicsetclans.api.events.ClanJoinEvent.JoinCause;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.AdminCommands;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class TeamCommand extends AdminCommands
{
	private String name;
	public TeamCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager) 
	{
		super(plugin, clanManager, duelManager);
		this.name="team";
	}
	@Override
	public void onCommandByPlayer(Player sender, String[] sub) 
	{
		if(sub.length>3&&sub[1].equalsIgnoreCase("add")&&Bukkit.getPlayerExact(sub[2])!=null)
		{
			Player player=Bukkit.getPlayerExact(sub[2]);
			String clanName = sub[3];
            for (int i = 4; i < sub.length; i++)
            	clanName = clanName.concat(" " + sub[i]); 
            if(clanManager.getClanData(clanName)!=null)
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
		        if(clanManager.isInClan(player.getName()))
		        {
		        	EpicClanData playerClan = (EpicClanData) clanManager.getClanData(player.getName());
		        	ClanLeaveEvent leaveEvent=new ClanLeaveEvent(player, playerClan);
		        	Bukkit.getPluginManager().callEvent(leaveEvent);
		        	if(leaveEvent.isCancelled())
		        		return;
		        	playerClan.removeClanMember(player.getName());
		        }
		        ClanJoinEvent joinEvent=new ClanJoinEvent(player, clan,JoinCause.ADMIN);
		        Bukkit.getPluginManager().callEvent(joinEvent);
		        if(joinEvent.isCancelled())
		        	return;
		        clan.addClanMember(player.getName());
	        	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully added "+player.getName()+"to "+clan.getClanName()+"&f clan."));
    			player.sendMessage(CommonUtils.chat("&b&lClans &7: &fYour have been added to clan "+clan.getClanName()+"&f by "+sender.getName()));
				return;
            }
            else
            {
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find any clan by that name."));
            	return;
            }
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("remove")&&Bukkit.getPlayerExact(sub[2])!=null)
		{
			Player player=Bukkit.getPlayerExact(sub[2]);
            if(clanManager.isInClan(player.getName()))
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(player.getName());
            	ClanLeaveEvent leaveEvent=new ClanLeaveEvent(player, clan);
	        	Bukkit.getPluginManager().callEvent(leaveEvent);
	        	if(leaveEvent.isCancelled())
	        		return;
	        	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully removed "+player.getName()+"from clan "+clan.getClanName()));
	    		if(Bukkit.getPlayer(sub[2])!=null)
	    			Bukkit.getPlayer(sub[2]).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour have been removed from clan by "+sender.getName()));
	        	clan.removeClanMember(player.getName());
	    		return;
            }
            else
            {
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fThat player is not in a clan."));
            	return;
            }
		        
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin team add [player] [clanname/membername]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin team remove [membername]"));
	}

	@Override
	public void onCommandByConsole(CommandSender sender, String[] sub) 
	{

		if(sub.length>3&&sub[1].equalsIgnoreCase("add")&&Bukkit.getPlayerExact(sub[2])!=null)
		{
			Player player=Bukkit.getPlayerExact(sub[2]);
			String clanName = sub[3];
            for (int i = 4; i < sub.length; i++)
            	clanName = clanName.concat(" " + sub[i]); 
            if(clanManager.getClanData(clanName)!=null)
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
		        if(clanManager.isInClan(player.getName()))
		        {
		        	EpicClanData playerClan = (EpicClanData) clanManager.getClanData(player.getName());
		        	ClanLeaveEvent leaveEvent=new ClanLeaveEvent(player, playerClan);
		        	Bukkit.getPluginManager().callEvent(leaveEvent);
		        	if(leaveEvent.isCancelled())
		        		return;
		        	playerClan.removeClanMember(player.getName());
		        }
		        ClanJoinEvent joinEvent=new ClanJoinEvent(player, clan,JoinCause.ADMIN);
		        Bukkit.getPluginManager().callEvent(joinEvent);
		        if(joinEvent.isCancelled())
		        	return;
		        clan.addClanMember(player.getName());
	        	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully added "+player.getName()+"to "+clan.getClanName()+"&f clan."));
    			player.sendMessage(CommonUtils.chat("&b&lClans &7: &fYour have been added to clan "+clan.getClanName()+"&f by "+sender.getName()));
				return;
            }
            else
            {
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find any clan by that name."));
            	return;
            }
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("remove")&&Bukkit.getPlayerExact(sub[2])!=null)
		{
			Player player=Bukkit.getPlayerExact(sub[2]);
            if(clanManager.isInClan(player.getName()))
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(player.getName());
            	ClanLeaveEvent leaveEvent=new ClanLeaveEvent(player, clan);
	        	Bukkit.getPluginManager().callEvent(leaveEvent);
	        	if(leaveEvent.isCancelled())
	        		return;
	        	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully removed "+player.getName()+"from clan "+clan.getClanName()));
	    		if(Bukkit.getPlayer(sub[2])!=null)
	    			Bukkit.getPlayer(sub[2]).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour have been removed from clan by "+sender.getName()));
	        	clan.removeClanMember(player.getName());
	    		return;
            }
            else
            {
            	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fThat player is not in a clan."));
            	return;
            }
		        
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin team add [player] [clanname/membername]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin team remove [membername]"));
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
			result.add("add");
			result.add("remove");
			StringUtil.copyPartialMatches(hint, cmd, result);
			Collections.sort(result);
	        return result;
		}
		if(length==3)
		{
			List<String> result=new ArrayList<>();
			List<String> players=new ArrayList<>();
			for(Player player:Bukkit.getOnlinePlayers())
				players.add(player.getName());
			StringUtil.copyPartialMatches(hint, players, result);
			Collections.sort(result);
	        return result;
		}
		if(length==4)
		{
			List<String> result=new ArrayList<>();
			List<String> players=new ArrayList<>();
			for(Player player:Bukkit.getOnlinePlayers())
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
