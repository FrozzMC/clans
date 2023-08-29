package me.backstabber.epicsetclans.api.events;

import org.bukkit.command.CommandSender;

import me.backstabber.epicsetclans.api.data.ClanData;

public class ClanDeleteEvent extends ClanEvent
{
	private CommandSender sender;
	private DeleteCause cause;

	public ClanDeleteEvent(CommandSender sender,ClanData clanData,DeleteCause cause) 
	{
		super(clanData);
		this.sender=sender;
		this.cause=cause;
	}
	public CommandSender getDeletor()
	{
		return sender;
	}
	public DeleteCause getCause() {
		return this.cause;
	}
	public enum DeleteCause {
		API,
		ADMIN,
		LEADER;
	}
}
