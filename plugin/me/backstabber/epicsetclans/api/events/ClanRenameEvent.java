package me.backstabber.epicsetclans.api.events;

import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.api.data.ClanData;

public class ClanRenameEvent extends ClanEvent
{
	 private Player player;

	 public ClanRenameEvent(Player player,ClanData clan) 
	 {
		super(clan);
		this.player=player;
	}
	public Player getRenamer()
	{
		return player;
	}
	
}
