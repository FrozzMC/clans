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
import me.backstabber.epicsetclans.api.events.ClanDeleteEvent;
import me.backstabber.epicsetclans.api.events.ClanDeleteEvent.DeleteCause;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.AdminCommands;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class DeleteCommand extends AdminCommands
{
	private String name;
	public DeleteCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager) 
	{
		super(plugin, clanManager, duelManager);
		this.name="delete";
	}
	@Override
	public void onCommandByPlayer(Player sender, String[] sub) 
	{
		if(sub.length>1)
		{
			String clanName = sub[1];
            for (int i = 2; i < sub.length; i++)
              clanName = clanName.concat(" " + sub[i]); 
            if(clanManager.getClanData(clanName)!=null)
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
            	ClanDeleteEvent deleteEvent=new ClanDeleteEvent(sender, clan,DeleteCause.ADMIN);
            	Bukkit.getPluginManager().callEvent(deleteEvent);
            	if(deleteEvent.isCancelled())
            		return;
            	for(String s:clan.getClanMembersName())
            		if(Bukkit.getPlayer(s)!=null)
            			Bukkit.getPlayer(s).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan has been deleted by "+sender.getName()));
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully deleted clan "+clan.getClanName()+"&f from database."));
				clanManager.deleteClan(clan.getClanNameRaw());
				return;
            }
            else
            {
        		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find any clan by that name."));
        		return;
            }
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin delete [clanname/playername]"));
	}

	@Override
	public void onCommandByConsole(CommandSender sender, String[] sub) 
	{

		if(sub.length>1)
		{
			String clanName = sub[1];
            for (int i = 2; i < sub.length; i++)
              clanName = clanName.concat(" " + sub[i]); 
            if(clanManager.getClanData(clanName)!=null)
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
            	ClanDeleteEvent deleteEvent=new ClanDeleteEvent(sender, clan,DeleteCause.ADMIN);
            	Bukkit.getPluginManager().callEvent(deleteEvent);
            	if(deleteEvent.isCancelled())
            		return;
            	for(String s:clan.getClanMembersName())
            		if(Bukkit.getPlayer(s)!=null)
            			Bukkit.getPlayer(s).sendMessage(CommonUtils.chat("&b&lClans &7: &fYour clan has been deleted by "+sender.getName()));
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully deleted clan "+clan.getClanName()+"&f from database."));
				clanManager.deleteClan(clan.getClanNameRaw());
				return;
            }
            else
            {
        		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find any clan by that name."));
        		return;
            }
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin delete [clanname/playername]"));
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

}
