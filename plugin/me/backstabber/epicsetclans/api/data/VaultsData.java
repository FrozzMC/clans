package me.backstabber.epicsetclans.api.data;

import org.bukkit.inventory.Inventory;


public interface VaultsData 
{
	public Inventory getVault(int number);
	public void saveVaults();
}
