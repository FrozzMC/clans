package me.backstabber.epicsetclans.api.events;

import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.api.data.ClanData;

public class ClanLeaveEvent extends ClanEvent
{
	 private Player player;

	 public ClanLeaveEvent(Player player,ClanData clanData) 
	 {
		super(clanData);
		this.player=player;
	}
	public Player getLeaver()
	{
		return player;
	}
}
