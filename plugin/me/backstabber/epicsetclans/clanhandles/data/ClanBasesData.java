package me.backstabber.epicsetclans.clanhandles.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import me.backstabber.epicsetclans.api.data.BaseData;
import me.backstabber.epicsetclans.clanhandles.saving.SavingManager;

public class ClanBasesData implements BaseData
{
	private SavingManager savingManager;
	private FileConfiguration file;
	private Map<Integer, Location> bases=new HashMap<>();
	ClanBasesData(SavingManager savingManager,FileConfiguration file)
	{
		this.file=file;
		this.savingManager=savingManager;
		ConfigurationSection node = file.getConfigurationSection("base");
		if(node!=null)
		{
			for(String key:node.getKeys(false))
			{
				String world=node.getString(key+".world");
				String loc=node.getString(key+".loc");
				if(world!=null&&getLocation(world, loc)!=null)
					bases.put(Integer.valueOf(key), getLocation(world, loc));
				else
				{
					removeBase(node.getString(key+".name"));
				}
			}
		}
			
	}
	public boolean isBase(String name)
	{
		for(int key:bases.keySet())
			if(file.isSet("base."+key+".name")&&file.getString("base."+key+".name").equals(name))
				return true;
		return false;
	}
	public void removeBase(String name)
	{
		for(int key:bases.keySet())
			if(file.isSet("base."+key+".name")&&file.getString("base."+key+".name").equals(name))
				{
					this.bases.remove(key);
					file.set("base."+key, null);
					savingManager.save(file);
				}
		savingManager.save(file);
	}
	public Map<Integer, Location> getAllBases()
	{
		return this.bases;
	}
	public Location getBase(String name)
	{
		for(int key:bases.keySet())
			if(file.isSet("base."+key+".name")&&file.getString("base."+key+".name").equals(name))
				return bases.get(key);
		return null;
	}
	public void addBase(String name,Location location)
	{
		if(!isBase(name))
		{
			int number=0;
			for(int i:bases.keySet())
				if(i>number)
					number=i;
			number=number+1;
			setBase(location, name, number);
		}
	}
	public String getBaseName(int number)
	{
		for(int key:bases.keySet())
			if(file.isSet("base."+key+".name"))
				{
					return file.getString("base."+key+".name");
				}
		return "";
	}
	private void setBase(Location location,String name,int number)
	{
		bases.put(number, location);
		String world=location.getWorld().getName();
		String loc=getLocation(location);
		file.set("base."+number+".name", name);
		file.set("base."+number+".world", world);
		file.set("base."+number+".loc", loc);
		savingManager.save(file);
	}
	private String getLocation(Location loc) 
	{
		return loc.getX()+","+loc.getY()+","+loc.getZ()+","+loc.getYaw()+","+loc.getPitch();
	}
	private Location getLocation(String world,String loc)
	{
		if(Bukkit.getWorld(world)==null)
			return null;
		if(loc.split(",").length==3)
			return new Location(Bukkit.getWorld(world), Double.valueOf(loc.split(",")[0]), Double.valueOf(loc.split(",")[1]), Double.valueOf(loc.split(",")[2]));
		if(loc.split(",").length==5)
			return new Location(Bukkit.getWorld(world), Double.valueOf(loc.split(",")[0]), Double.valueOf(loc.split(",")[1]), Double.valueOf(loc.split(",")[2]),Float.valueOf(loc.split(",")[3]),Float.valueOf(loc.split(",")[4]));
		return null;
	}
}
