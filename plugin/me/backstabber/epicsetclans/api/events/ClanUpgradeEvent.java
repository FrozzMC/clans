package me.backstabber.epicsetclans.api.events;

import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.api.data.ClanData;

public class ClanUpgradeEvent extends ClanEvent
{
	private int level;
	private Player player;
	private Upgradetype type;

	public ClanUpgradeEvent(Player player,ClanData clan,Upgradetype type,int level) 
	{
		super(clan);
		this.player=player;
		this.type=type;
		this.level=level;
	}
	public Player getUpgrader()
	{
		return player;
	}
	public Upgradetype getUpgradeType()
	{
		return type;
	}
	public int getLevel()
	{
		return level;
	}
	public enum Upgradetype
	{
		UPGRADE_HOME,
		UPGRADE_MEMBERS,
		UPGRADE_VAULTS,
		UPGRADE_DUELS,
		UPGRADE_ALLIES;
	}
}
