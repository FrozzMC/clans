package me.backstabber.epicsetclans.commands.subcommands.admin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.AdminCommands;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class HelpCommand extends AdminCommands
{
	private String name;
	public HelpCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager) 
	{
		super(plugin, clanManager, duelManager);
		this.name="help";
	}
	@Override
	public void onCommandByPlayer(Player sender, String[] sub) 
	{
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reward &7: &bManage monthly rewards"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin arena &7: &bManage arenas for duels"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin kits &7: &bManage kits for duels"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin delete &7: &bDelete a clan"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin rename &7: &bRename a clan"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin team &7: &bModify teams of clans."));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin respect &7: &bModify respect of clans"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reset &7: &bReset different stats of clans & players."));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin points &7: &bManage clan points of players"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin spy &7: &bSpy on clan chats"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin vault &7: &bAccess clan vaults"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin bank &7: &bAccess clan banks"));
	}

	@Override
	public void onCommandByConsole(CommandSender sender, String[] sub) 
	{
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reward &7: &bManage monthly rewards"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin delete &7: &bDelete a clan"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin rename &7: &bRename a clan"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin team &7: &bModify teams of clans."));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin respect &7: &bModify respect of clans"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin reset &7: &bReset different stats of clans & players."));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin points &7: &bManage clan points of players"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin bank &7: &bAccess clan banks"));
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
