package me.backstabber.epicsetclans.commands.subcommands.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.StringUtil;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.data.ClanData;
import me.backstabber.epicsetclans.api.data.VaultsData;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.data.ClanVaultsData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.AdminCommands;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class VaultCommand extends AdminCommands
{
	private String name;
	public VaultCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager) 
	{
		super(plugin, clanManager, duelManager);
		this.name="vault";
	}
	@Override
	public void onCommandByPlayer(Player sender, String[] sub) 
	{
		
		if(sub.length>3&&sub[1].equalsIgnoreCase("open"))
		{
			String clanName = sub[3];
	        for (int i = 4; i < sub.length; i++)
	        	clanName = clanName.concat(" " + sub[i]); 
	        if(clanManager.getClanData(clanName)!=null)
	        {
	        	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
	        	VaultsData vaults = clan.getVaults();
	        	int amount=0;
				try 
				{
					amount=Integer.valueOf(sub[2]);
				}
				catch(NullPointerException | NumberFormatException e)
				{
					sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlease enter correct vault number."));
					return;
				}
				amount=Math.abs(amount);
				if(amount>clan.getVaultsUpgrade())
				{
					sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fIncorrect vault specified"));
					return;
				}
            	Inventory vault = vaults.getVault(amount);
            	sender.openInventory(vault);
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fOpening vault #"+amount+" of clan "+clan.getClanName()+"&f."));
				return;
	        }
	        else
	        {
	        	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find clan by that name."));
				return;
	        }
			
		}
		if(sub.length>3&&sub[1].equalsIgnoreCase("clear"))
		{
			String clanName = sub[3];
	        for (int i = 4; i < sub.length; i++)
	        	clanName = clanName.concat(" " + sub[i]); 
	        if(clanManager.getClanData(clanName)!=null)
	        {
	        	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
	        	VaultsData vaults = clan.getVaults();
	        	int amount=0;
				try 
				{
					amount=Integer.valueOf(sub[2]);
				}
				catch(NullPointerException | NumberFormatException e)
				{
					sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlease enter correct vault number."));
					return;
				}
				amount=Math.abs(amount);
				if(amount>clan.getVaultsUpgrade())
				{
					sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fIncorrect vault specified"));
					return;
				}
            	((ClanVaultsData) vaults).clearVault(amount);
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fClearing vault #"+amount+" of clan "+clan.getClanName()+"&f."));
				return;
	        }
	        else
	        {
	        	sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find clan by that name."));
				return;
	        }
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin vault open [number] [clanname/membername]  &bopen a vault"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin vault clear [number] [clanname/membername] &bclear a vault "));
	}

	@Override
	public void onCommandByConsole(CommandSender sender, String[] sub) 
	{
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fThis command isnt supported by console yet."));
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
			result.add("open");
			result.add("clear");
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
