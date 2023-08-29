package me.backstabber.epicsetclans.commands.subcommands.admin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.AdminCommands;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class SpyCommand extends AdminCommands
{
	private String name;
	public SpyCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager) 
	{
		super(plugin, clanManager, duelManager);
		this.name="spy";
	}
	@Override
	public void onCommandByPlayer(Player sender, String[] sub) 
	{
		if(sender.hasMetadata("EpicChatSpy"))
		{
			sender.removeMetadata("EpicChatSpy", plugin);
			sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fChat spy turned off."));
		}
		else
		{
			sender.setMetadata("EpicChatSpy", new FixedMetadataValue(plugin, true));
			sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fChat spy turned on."));
		}
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
		return new ArrayList<>();
	}

}
