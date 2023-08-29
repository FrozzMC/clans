package me.backstabber.epicsetclans.commands.subcommands.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.events.ClanRenameEvent;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.AdminCommands;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class RenameCommand extends AdminCommands
{
	private String name;
	public RenameCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager) 
	{
		super(plugin, clanManager, duelManager);
		this.name="rename";
	}
	@Override
	public void onCommandByPlayer(Player sender, String[] sub) 
	{
		if(sub.length>2)
		{
			String member=sub[1];
			if(clanManager.isInClan(member))
			{
				EpicClanData clan = (EpicClanData) clanManager.getClanData(member);
				String newName = sub[2];
				for (int i = 3; i < sub.length; i++)
					newName = newName.concat(" " + sub[i]); 
				if(clanManager.isClanName(newName)&&!clan.getClanNameRaw().equals(ChatColor.stripColor(CommonUtils.chat(newName))))
				{
					sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fThat name is already in use."));
					return;
				}
				ClanRenameEvent event=new ClanRenameEvent(sender, clan);
				Bukkit.getPluginManager().callEvent(event);
				if(event.isCancelled())
					return;
				clan.setClanName(newName);
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fClan renamed to "+clan.getClanName()));
				return;
				
			}
			else
			{
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find clan of player "+member));
				return;
			}
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin rename [membername] [new name]  &bRename a clan"));
	}

	@Override
	public void onCommandByConsole(CommandSender sender, String[] sub) 
	{
		if(sub.length>2)
		{
			String member=sub[1];
			if(clanManager.isInClan(member))
			{
				EpicClanData clan = (EpicClanData) clanManager.getClanData(member);
				String newName = sub[2];
				for (int i = 3; i < sub.length; i++)
					newName = newName.concat(" " + sub[i]); 
				if(clanManager.isClanName(newName)&&!clan.getClanNameRaw().equals(ChatColor.stripColor(CommonUtils.chat(newName))))
				{
					sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fThat name is already in use."));
					return;
				}
				clan.setClanName(newName);
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fClan renamed to "+clan.getClanName()));
				return;
				
			}
			else
			{
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find clan of player "+member));
				return;
			}
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin rename [membername] [new name]  &bRename a clan"));
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
