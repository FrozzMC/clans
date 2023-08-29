package me.backstabber.epicsetclans.clanhandles.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.data.VaultsData;
import me.backstabber.epicsetclans.clanhandles.saving.SavingManager;

public class ClanVaultsData implements VaultsData
{
	private SavingManager savingManager;
	private EpicSetClans plugin;
	private FileConfiguration file;
	private Map<Integer, Inventory> vaults=new HashMap<>();
	private EpicClanData clanData;
	ClanVaultsData(EpicSetClans plugin, SavingManager savingManager,FileConfiguration file,EpicClanData clanData)
	{
		this.plugin=plugin;
		this.file=file;
		this.clanData=clanData;
		this.savingManager=savingManager;
		ConfigurationSection node = file.getConfigurationSection("vaults");
		if(node!=null)
		{
			for(String name:node.getKeys(false))
			{
				int number=Integer.valueOf(name);
				loadVault(number);
			}
		}
	}
	public Inventory getVault(int number)
	{
		if(!this.vaults.containsKey(number))
		{
			Inventory inv=Bukkit.createInventory(null, 9*6,clanData.getClanName()+"'s Vault# "+number);
			this.vaults.put(number, inv);
		}
			return this.vaults.get(number);
	}
	public void clearVault(int number)
	{
		this.vaults.remove(number);
		file.set("vaults."+number, null);
		savingManager.save(file);
	}

	public void clearAllVaults() 
	{
		for(int number:vaults.keySet())
			clearVault(number);
	}
	public void saveAll()
	{
		for(int number:vaults.keySet())
			saveVault(number);
		File fileLocation=new File(plugin.getDataFolder()+"/clans",clanData.getClanLeader()+".yml");
		try {
			file.save(fileLocation);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void saveVault(Inventory vault,String name)
	{
		if(!name.startsWith(clanData.getClanName()))
			return;
		name=name.replace(clanData.getClanName()+"'s Vault# ", "");
		int number=0;
		try{number=Integer.valueOf(name);}catch (Exception e) {return;}
		if(!this.vaults.get(number).equals(vault))
			return;
		saveVault(number);
	}
	private void saveVault(int number)
	{
		for(int i=0;i<(9*6);i++)
		{
			file.set("vaults."+number+"."+i, this.vaults.get(number).getItem(i));
		}
		
	}
	private void loadVault(int number)
	{
		if(!this.vaults.containsKey(number))
		{
			Inventory inv=Bukkit.createInventory(null, 9*6,clanData.getClanName()+"'s Vault# "+number);
			this.vaults.put(number, inv);
		}
		if(file.isSet("vaults."+number))
		{
			for(int i=0;i<(9*6);i++)
			{
				if(file.isSet("vaults."+number+"."+i))
					this.vaults.get(number).setItem(i, file.getItemStack("vaults."+number+"."+i));
			}
		}
	}
	@Override
	public void saveVaults() {
		this.saveAll();
	}
}
