package me.backstabber.epicsetclans.api.events;

import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.api.data.ClanData;

public class ClanCeateEvent extends ClanEvent 
{
	 private Player player;
	 private CreateCause cause;
	 public ClanCeateEvent(Player player,ClanData data, CreateCause cause) 
	 {
		super(data);
		this.player=player;
	}
	public Player getCreator()
	{
		return player;
	}
	public CreateCause getCreationCause() {
		return cause;
	}
	public enum CreateCause {
		API,
		PLAYER,
	}
}
