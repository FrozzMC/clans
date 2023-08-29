package me.backstabber.epicsetclans.commands.subcommands.clan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.data.ClanData;
import me.backstabber.epicsetclans.api.events.ClanCeateEvent.CreateCause;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.SubCommands;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class CreateCommand extends SubCommands
{
	private String name;
	public CreateCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager)
	{
		super(plugin, clanManager, duelManager);
		this.name = "create";
		this.permission=plugin.isCommandOpen(name);
		if(plugin.getMessages().isSet(this.name))
		{
			for(String key:plugin.getMessages().getFile().getConfigurationSection(this.name).getKeys(false))
			{
				messages.put(key, this.name+"."+key);
			}
		}
	}
	@Override
	public void onCommandByPlayer(Player sender, String[] sub) 
	{
		if(sub.length<=1)
		{
			sendMessage(sender, "info",new HashMap<>());
			return;
		}
		String clanName = sub[1];
        for (int i = 2; i < sub.length; i++)
        	clanName = clanName.concat(" " + sub[i]); 
		HashMap<String,String> placeHolder = new HashMap<>();
		placeHolder.put("%player%", sender.getName());
		placeHolder.put("%clan%", clanName);
		placeHolder.put("%timer%", CommonUtils.getTimeFormat(plugin.getSettings().getFile().getInt("settings.clan-create-timer")));
		if(clanManager.isInClan(sender.getName()))
		{
			sendMessage(sender,"inclan",placeHolder);
			return;
		}
		if(ChatColor.stripColor(CommonUtils.chat(clanName)).replace(" ", "").equalsIgnoreCase(""))
		{
			sendMessage(sender,"noname",placeHolder);
			return;
		}
		if(clanManager.isClanName(clanName))
		{
			sendMessage(sender,"nametaken",placeHolder);
			return;
		}
		if(clanName.toCharArray().length>plugin.getSettings().getFile().getInt("settings.max-clan-name"))
		{
			sendMessage(sender,"bigname",placeHolder);
			return;
		}
		if(sender.hasMetadata("EpicSetClanTimer"))
		{
			sendMessage(sender,"timer",placeHolder);
			return;
		}
		clanName=clanName.replace("&k", "");
		ClanData data = clanManager.createNewClan(sender, clanName,false,CreateCause.PLAYER);
		if(data==null)
			return;
		broadcastMessage(sender, "broadcast", placeHolder);
		sendMessage(sender, "sucess", placeHolder);
		sender.setMetadata("EpicSetClanTimer", new FixedMetadataValue(plugin, true));
		new BukkitRunnable() 
		{
			@Override
			public void run() 
			{
				sender.removeMetadata("EpicSetClanTimer", plugin);
			}
		}.runTaskLater(plugin, 20*plugin.getSettings().getFile().getInt("settings.clan-create-timer"));
	}

	@Override
	public String getName() 
	{
		return this.name;
	}

	@Override
	public List<String> getAliases() 
	{
		List<String> alias=new ArrayList<String>();
		alias.add(name);
		alias.add("new");
		return alias;
	}
	@Override
	public List<String> getCompletor(Player sender, int length, String hint) 
	{
		return new ArrayList<>();
	}

}
