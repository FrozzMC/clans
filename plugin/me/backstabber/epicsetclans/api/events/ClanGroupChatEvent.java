package me.backstabber.epicsetclans.api.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.api.data.ClanData;

public class ClanGroupChatEvent extends ClanEvent
{
	private Player player;
	private Map<Player, String> recipient=new HashMap<Player,String>();
	private String message;
	private GroupType type;

	public ClanGroupChatEvent(Player player,Map<Player, String> recipient,ClanData clan,String message,GroupType type) 
	{
		super(clan);
		this.player=player;
		this.recipient=recipient;
		this.message=message;
		this.type=type;
	}
	public Player getSender()
	{
		return player;
	}
	public List<Player> getRecipient()
	{
		List<Player> players=new ArrayList<Player>();
		for(Player p:recipient.keySet())
			players.add(p);
		return players;
	}
	public String getFromat(Player recipient)
	{
		return this.recipient.get(recipient);
	}
	public void setFromat(Player recipient,String format)
	{
		this.recipient.put(recipient, format);
	}
	public String getMessage()
	{
		return this.message;
	}
	public void setMessage(String message)
	{
		this.message=message;
	}
	public GroupType getType()
	{
		return this.type;
	}
	public enum GroupType {
		CLAN,
		CLAN_ALLY,
		CLAN_ALLY_TRUCE,
	}
}
