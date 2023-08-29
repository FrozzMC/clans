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
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.AdminCommands;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class RespectCommand extends AdminCommands
{
	private String name;
	public RespectCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager) 
	{
		super(plugin, clanManager, duelManager);
		this.name="respect";
	}
	@Override
	public void onCommandByPlayer(Player sender, String[] sub) 
	{
		if(sub.length>3&&sub[1].equalsIgnoreCase("give"))
		{
			String clanName=sub[3];
            for (int i = 4; i < sub.length; i++)
            	clanName = clanName.concat(" " + sub[i]); 
            if(clanManager.getClanData(clanName)!=null)
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
            	int amount=0;
    			try 
    			{
    				amount=Integer.valueOf(sub[2]);
    			}
    			catch(NullPointerException | NumberFormatException e)
    			{
    				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlease enter correct amount to give."));
    				return;
    			}
    			clan.addRespect(Math.abs(amount));
            	clan.save();
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully given "+Math.abs(amount)+" respect to "+clan.getClanName()+"."));
				return;
            }
            else
            {
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find clan by that name."));
            }
			
		}
		if(sub.length>3&&sub[1].equalsIgnoreCase("take"))
		{
			String clanName=sub[3];
            for (int i = 4; i < sub.length; i++)
            	clanName = clanName.concat(" " + sub[i]); 
            if(clanManager.getClanData(clanName)!=null)
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
            	int amount=0;
    			try 
    			{
    				amount=Integer.valueOf(sub[2]);
    			}
    			catch(NullPointerException | NumberFormatException e)
    			{
    				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlease enter correct amount to give."));
    				return;
    			}
    			clan.removeRespect(Math.abs(amount));
            	clan.save();
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully taken "+Math.abs(amount)+" respect from "+clan.getClanName()+"."));
				return;
            }
            else
            {
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find clan by that name."));
            }
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin respect give [amount] [clanname/membername]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin respect take [amount] [clanname/membername]"));
	}

	@Override
	public void onCommandByConsole(CommandSender sender, String[] sub) 
	{
		if(sub.length>3&&sub[1].equalsIgnoreCase("give"))
		{
			String clanName=sub[3];
            for (int i = 4; i < sub.length; i++)
            	clanName = clanName.concat(" " + sub[i]); 
            if(clanManager.getClanData(clanName)!=null)
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
            	int amount=0;
    			try 
    			{
    				amount=Integer.valueOf(sub[2]);
    			}
    			catch(NullPointerException | NumberFormatException e)
    			{
    				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlease enter correct amount to give."));
    				return;
    			}
    			clan.addRespect(Math.abs(amount));
            	clan.save();
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully given "+Math.abs(amount)+" respect to "+clan.getClanName()+"."));
				return;
            }
            else
            {
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find clan by that name."));
            }
			
		}
		if(sub.length>3&&sub[1].equalsIgnoreCase("take"))
		{
			String clanName=sub[3];
            for (int i = 4; i < sub.length; i++)
            	clanName = clanName.concat(" " + sub[i]); 
            if(clanManager.getClanData(clanName)!=null)
            {
            	EpicClanData clan = (EpicClanData) clanManager.getClanData(clanName);
            	int amount=0;
    			try 
    			{
    				amount=Integer.valueOf(sub[2]);
    			}
    			catch(NullPointerException | NumberFormatException e)
    			{
    				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlease enter correct amount to give."));
    				return;
    			}
    			clan.removeRespect(Math.abs(amount));
            	clan.save();
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully taken "+Math.abs(amount)+" respect from "+clan.getClanName()+"."));
				return;
            }
            else
            {
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fCant find clan by that name."));
            }
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin respect give [amount] [clanname/membername]"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin respect take [amount] [clanname/membername]"));
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
			StringUtil.copyPartialMatches(hint, cmd, result);
			Collections.sort(result);
	        return result;
		}
		if(length==4)
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
