package me.backstabber.epicsetclans.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.manager.YMLManager;

import java.util.ArrayList;
import java.util.List;

public class Cuboid 
{
	private EpicSetClans plugin;
	private Location cornerAlpha;
	private Location cornerBravo;
	private List<Blockdata> savedBlocks=new ArrayList<Blockdata>();
	private boolean resetting=false;
	private boolean saving=false;
	private boolean updating=false;
	public static Cuboid getFromFile(EpicSetClans plugin,YMLManager file)
	{
		if(!file.getFile().isSet("cuboid"))
			throw new InvalidCuboidException("Cant find any saved Cuboid.");
		Cuboid cuboid=new Cuboid();
		cuboid.plugin=plugin;
		String stringAlpha=file.getFile().getString("cuboid.cornerAlpha");
		if(Bukkit.getWorld(stringAlpha.split(";")[0])==null)
			throw new InvalidCuboidException("Cant find world named "+stringAlpha.split(";")[0]);
		cuboid.cornerAlpha= StringFormater.stringToLocation(stringAlpha);
		String stringBravo=file.getFile().getString("cuboid.cornerBravo");
		cuboid.cornerBravo= StringFormater.stringToLocation(stringBravo);
		cuboid.saveBlocks();
		return cuboid;
	}
	public static Cuboid create(EpicSetClans plugin,Location cornerAlpha,Location cornerBravo)
	{
		if(!cornerAlpha.getWorld().equals(cornerBravo.getWorld()))	//if coords arent in same world
			throw new InvalidCuboidException("Corners are in different worlds.");
		Cuboid cuboid=new Cuboid();
		cuboid.plugin=plugin;
		cuboid.cornerAlpha=cornerAlpha;
		cuboid.cornerBravo=cornerBravo;
		//get all blocks & store them
		cuboid.saveBlocks();
		return cuboid;
	}
	public boolean isInCuboid(Entity entity)
	{
		return isInCuboid(entity.getLocation());
	}
	public boolean isInCuboid(Location location)
	{
		if(!location.getWorld().equals(cornerAlpha.getWorld()))
			return false;
		if(location.getX()<Math.min(cornerAlpha.getX(), cornerBravo.getX())||location.getX()>Math.max(cornerAlpha.getX(), cornerBravo.getX()))
			return false;
		if(location.getY()<Math.min(cornerAlpha.getY(), cornerBravo.getY())||location.getY()>Math.max(cornerAlpha.getY(), cornerBravo.getY()))
			return false;
		if(location.getZ()<Math.min(cornerAlpha.getZ(), cornerBravo.getZ())||location.getZ()>Math.max(cornerAlpha.getZ(), cornerBravo.getZ()))
			return false;
		return true;
	}
	public List<Blockdata> getBlocks()
	{
		return savedBlocks;
	}
	
	public boolean isSaving()	//if blocks are being saved in a file
	{
		return saving;
	}
	
	
	public boolean isResetting()	//if blocks are being resetted
	{
		return resetting;
	}
	
	
	public boolean isUpdating()		//if blocks are being updated
	{
		return updating;
	}
	public void saveToFile(YMLManager file)
	{
		if(saving)
			throw new InvalidCuboidException("Cuboid already Saving.");
		saving=true;
		//create node cuboid & save corners there
		file.getFile().set("cuboid.cornerAlpha", StringFormater.locationToString(cornerAlpha, false));
		file.getFile().set("cuboid.cornerBravo", StringFormater.locationToString(cornerBravo, false));
		//get serialized saved blockdata & save it as list
		file.save(false);
		saving=false;
	}
	
	public void resetCuboid()	//this will reset the cuboid to the previous known state
	{
		if(resetting)
			throw new InvalidCuboidException("The Cuboid is already Resetting.");
		resetting=true;
		//to make it lag less im limiting the amount of blocks replaced to 1000 per 2 tick (pretty safe)
		//the speed of this logic is about 40 sec for 640000 blocks (kinda good if u ask me)
		new BukkitRunnable() 
		{
			int index=0;
			int per=1000;
			@Override
			public void run() 
			{
				int temp=index;
				for(int i=index;i<temp+per;i++)
				{
					if(i<savedBlocks.size())
					{
						savedBlocks.get(i).update();
						index++;
					}
					else
					{
						break;
					}
				}
				if(index>=savedBlocks.size())
				{
					resetting=false;
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 1, 1);
	}

	public Location getCornerAlpha() {
		return cornerAlpha;
	}

	public Location getCornerBravo() {
		return cornerBravo;
	}

	private void saveBlocks()
	{
		if(updating)
			throw new InvalidCuboidException("Cuboid is Already Updating");
		//this lagged if blocks are too many so i try to save 10000 blocks per tick (takes time though)
		//get all blocks in a list
		List<Block> allBlocks=new ArrayList<>();
		//iterate through x y & z coords from alpha to bravo
		for(int xCord=Math.min(cornerAlpha.getBlockX(), cornerBravo.getBlockX());xCord<=Math.max(cornerAlpha.getBlockX(), cornerBravo.getBlockX());xCord++)
		{
			for(int yCord=Math.min(cornerAlpha.getBlockY(), cornerBravo.getBlockY());yCord<=Math.max(cornerAlpha.getBlockY(), cornerBravo.getBlockY());yCord++)
			{
				for(int zCord=Math.min(cornerAlpha.getBlockZ(), cornerBravo.getBlockZ());zCord<=Math.max(cornerAlpha.getBlockZ(), cornerBravo.getBlockZ());zCord++)
				{
					Location location=new Location(cornerAlpha.getWorld(), xCord, yCord, zCord);
					//get block on this location
					allBlocks.add(location.getBlock());
					//save this block
				}
			}
		}
		updating=true;
		new BukkitRunnable() 
		{
			int index=0;
			int per=10000;
			@Override
			public void run() 
			{
				int temp=index;
				for(int i=index;i<temp+per;i++)
				{
					if(i<allBlocks.size())
					{
						savedBlocks.add(Blockdata.adapt(allBlocks.get(i)));
						index++;
					}
					else
					{
						break;
					}
				}
				if(index>=allBlocks.size())
				{
					updating=false;
					this.cancel();
				}
			}
		}.runTaskTimer(plugin,0,1);
		
	}
}
