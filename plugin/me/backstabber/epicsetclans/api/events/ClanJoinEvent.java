package me.backstabber.epicsetclans.api.events;

import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.api.data.ClanData;

public class ClanJoinEvent extends ClanEvent
{
	 private Player player;
	private JoinCause cause;

	 public ClanJoinEvent(Player player,ClanData clanData,JoinCause cause) 
	 {
		super(clanData);
		this.player=player;
		this.cause=cause;
	}
	public Player getJoiner()
	{
		return player;
	}
	public JoinCause getCause() {
		return this.cause;
	}
	public enum JoinCause {
		INVITE,
		ADMIN;
	}
}
