package me.backstabber.epicsetclans.commands.subcommands.clan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.SubCommands;

public class ChatCommand extends SubCommands
{
	private String name;
	public ChatCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager)
	{
		super(plugin, clanManager, duelManager);
		this.name = "chat";
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
		HashMap<String,String> placeHolders = new HashMap<>();
		placeHolders.put("%player%", sender.getName());
		if(sub.length<=1)
		{
			sendMessage(sender, "info",placeHolders);
			return;
		}
		if(!clanManager.isInClan(sender.getName()))
		{
			sendMessage(sender, "noclan", placeHolders);
			return;
		}
		placeHolders.put("%clan%", clanManager.getClanData(sender.getName()).getClanName());
		if(sub[1].equalsIgnoreCase("on")||sub[1].equalsIgnoreCase("clan")||sub[1].equalsIgnoreCase("c"))
		{
			sender.setMetadata("EpicSetClanChat", new FixedMetadataValue(plugin, "clan"));
			sendMessage(sender, "onenable", placeHolders);
			return;
		}
		if(sub[1].equalsIgnoreCase("off"))
		{
			if(sender.hasMetadata("EpicSetClanChat"))
				sender.removeMetadata("EpicSetClanChat", plugin);
			sendMessage(sender, "ondisable", placeHolders);
			return;
		}
		if(sub[1].equalsIgnoreCase("ally")||sub[1].equalsIgnoreCase("a"))
		{
			if(sub.length>2)
			{
				String msg = sub[2];
                	for (int i = 3; i < sub.length; i++)
                		msg = msg.concat(" " + sub[i]); 
            	plugin.getChatHandle().sendAllyChat(sender, msg);
                return;
			}
			sender.setMetadata("EpicSetClanChat", new FixedMetadataValue(plugin, "ally"));
			sendMessage(sender, "onenableally", placeHolders);
			return;
		}
		if(sub[1].equalsIgnoreCase("truce")||sub[1].equalsIgnoreCase("t"))
		{
			if(sub.length>2)
			{
				String msg = sub[2];
            	for (int i = 3; i < sub.length; i++)
            		msg = msg.concat(" " + sub[i]); 
            	plugin.getChatHandle().sendTruceChat(sender, msg);
            	return;
			}
			sender.setMetadata("EpicSetClanChat", new FixedMetadataValue(plugin, "truce"));
			sendMessage(sender, "onenabletruce", placeHolders);
			return;
		}
		String msg = sub[1];
		for (int i = 2; i < sub.length; i++)
			msg = msg.concat(" " + sub[i]); 
		plugin.getChatHandle().sendClanChat(sender, msg);
        return;
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
		alias.add("c");
		return alias;
	}
	@Override
	public List<String> getCompletor(Player sender, int length, String hint) 
	{
		return new ArrayList<>();
	}

}
