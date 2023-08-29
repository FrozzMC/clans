package me.backstabber.epicsetclans.hooks.worldguard;

import org.bukkit.Location;


public interface WorldguardHook 
{
	public void attemptLoad();
	
	public boolean allowedClans(Location location);
	
	public boolean allowedAllies(Location location);
	 
	public boolean allowedTruce(Location location);
}
