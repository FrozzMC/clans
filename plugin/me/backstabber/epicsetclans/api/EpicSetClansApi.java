package me.backstabber.epicsetclans.api;

import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.manager.ClanManager;
import me.backstabber.epicsetclans.api.manager.TopManager;
/**
 * Old implementation of the clans api. Currently Planning a replacement replacement.
 * 
 * @author Backstabber
 *
 */
public class EpicSetClansApi 
{
	/**
	 * Method of fetching the Clanmanager
	 * 
	 * @return ClanManager 
	 */
	public static ClanManager getClanManager()
	{
		EpicSetClans plugin=EpicSetClans.getPlugin(EpicSetClans.class);
		return plugin.getClanManager();
	}
	/**
	 * Method of fetching the ClanTopManager
	 * @return ClanTopManager
	 */
	public static TopManager getClanTopManager()
	{
		EpicSetClans plugin=EpicSetClans.getPlugin(EpicSetClans.class);
		return plugin.getClanTopManager();
	}
	@Deprecated
	/**
	 * Method to check if a player is spying clan chats
	 * @Depreciated see ClanManager.isSpying(Player) 
	 * @param player to check
	 * @return if player is apying
	 */
	public static boolean isSpying(Player player)
	{
		return player.hasMetadata("EpicChatSpy");
	}
}
