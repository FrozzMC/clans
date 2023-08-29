package me.backstabber.epicsetclans.hooks.worldguard;

import org.bukkit.Location;

public class UnsupportedWG implements WorldguardHook {

	@Override
	public void attemptLoad() {
		
	}

	@Override
	public boolean allowedClans(Location location) {
		return true;
	}

	@Override
	public boolean allowedAllies(Location location) {
		return true;
	}

	@Override
	public boolean allowedTruce(Location location) {
		return true;
	}

}
