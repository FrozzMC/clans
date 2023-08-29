package me.backstabber.epicsetclans.api.events;

import java.util.List;

import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.api.data.ClanData;
@Deprecated
public class ClanChatEvent extends ClanEvent
{
	private Player player;
	private List<Player> recipient;
	private String format;
	private String message;
	private ChatType type;

	public ClanChatEvent(Player player,List<Player> recipient,ClanData clan,String format,String message,ChatType type) 
	{
		super(clan);
		this.player=player;
		this.recipient=recipient;
		this.format=format;
		this.message=message;
		this.type=type;
	}
	public Player getSender()
	{
		return player;
	}
	public List<Player> getRecipient()
	{
		return this.recipient;
	}
	public String getFromat()
	{
		return this.format;
	}
	public void setFromat(String format)
	{
		this.format=format;
	}
	public String getMessage()
	{
		return this.message;
	}
	public void setMessage(String message)
	{
		this.message=message;
	}
	public ChatType getType()
	{
		return this.type;
	}
	public enum ChatType
	{
		CLAN,ALLY,TRUCE,SPY;
	}
}
