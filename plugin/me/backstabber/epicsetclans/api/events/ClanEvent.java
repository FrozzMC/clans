package me.backstabber.epicsetclans.api.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.backstabber.epicsetclans.api.data.ClanData;

public class ClanEvent extends Event implements Cancellable{
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel = false;
	private ClanData clan;
	
	 public ClanEvent(ClanData data) 
	 {
		this.clan=data;
	 }
	 public ClanData getClan()
	 {
		 return clan;
	 }
	 @Override
	 public boolean isCancelled() 
	 {
		 return cancel;
	 }

	 @Override
	 public void setCancelled(boolean cancel) 
	 {
		 this.cancel=cancel;
	 }

	 @Override
	 public HandlerList getHandlers() 
	 {
		 return handlers;
	 }

	 public static final HandlerList getHandlerList() 
	 { 
		 return handlers; 
	 }
}
