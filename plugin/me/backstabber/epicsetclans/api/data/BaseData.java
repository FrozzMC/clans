package me.backstabber.epicsetclans.api.data;

import org.bukkit.Location;

/**
 * Container class for all clan base related data & operations
 * @author Backstabber
 *
 */
public interface BaseData 
{
	/**
	 * Check if the string is saved as a base
	 * @param name of base
	 * @return
	 */
	public boolean isBase(String name);
	/**
	 * Remove a base by name from the clan
	 * @param name of base
	 */
	public void removeBase(String name);
	/**
	 * Get the spawn location of a saved base
	 * @param name of base
	 * @return spawn location
	 */
	public Location getBase(String name);
	/**
	 * Add a new base to the clan
	 * @param name of base
	 * @param spawn location
	 */
	public void addBase(String name,Location location);
}
