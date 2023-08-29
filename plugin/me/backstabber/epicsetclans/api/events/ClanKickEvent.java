package me.backstabber.epicsetclans.api.events;

import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.api.data.ClanData;

public class ClanKickEvent extends ClanEvent 
{
	 private String kickedplayer;
	 private Player player;

	 public ClanKickEvent(Player player,String kickedplayer,ClanData clanData) 
	 {
		super(clanData);
		this.player=player;
		this.kickedplayer=kickedplayer;
	}
	public Player getKicker()
	{
		return player;
	}
	public String getKickedPlayer()
	{
		return kickedplayer;
	}
	
}
