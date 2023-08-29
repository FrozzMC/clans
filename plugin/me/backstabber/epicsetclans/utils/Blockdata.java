package me.backstabber.epicsetclans.utils;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.backstabber.epicsetclans.utils.materials.EpicMaterials;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import static me.backstabber.epicsetclans.utils.ReflectionUtils.*;

public class Blockdata {
	private Location location = null;
	private EpicMaterials type = null;
	private Object data = null;	//blockData in case of 1.13 & above & byte incase of below
	private Block block = null;
	private String skullOwner;
	
	@SuppressWarnings("deprecation")
	public static Blockdata deserialize(String string)
	{
		JsonParser parser=new JsonParser();
		JsonObject object =(JsonObject) parser.parse(string);
		if(object.get("SavedVersion")==null||object.get("Location")==null)
			throw new InvalidCuboidException("Cant deserialize the string "+string);
		if(object.get("SavedVersion").getAsDouble()!=CommonUtils.getServerVersion())
			throw new InvalidCuboidException("String made for different Version.");
		Blockdata bd=new Blockdata();
		if(CommonUtils.getServerVersion()>=13) //1.13 or above
		{
			//getlocation & block
			String locationString = object.get("Location").getAsString();
			bd.location=new Location(Bukkit.getWorld(locationString.split(";")[0]), Double.valueOf(locationString.split(";")[1]), Double.valueOf(locationString.split(";")[2]), Double.valueOf(locationString.split(";")[3]));
			bd.block=bd.location.getBlock();
			//get the type
			if(object.get("Type")!=null)
				bd.type=EpicMaterials.valueOf(object.get("Type").getAsString());
			else
				bd.type=EpicMaterials.valueOf(bd.block);
			//get data
			if(object.get("Data")!=null)
				bd.data=Bukkit.createBlockData(object.get("Data").getAsString());
			else
				bd.data=bd.block.getBlockData();
		}
		else
		{
			//getlocation & block
			String locationString = object.get("Location").getAsString();
			bd.location=new Location(Bukkit.getWorld(locationString.split(";")[0]), Double.valueOf(locationString.split(";")[1]), Double.valueOf(locationString.split(";")[2]), Double.valueOf(locationString.split(";")[3]));
			bd.block=bd.location.getBlock();
			//get the type
			if(object.get("Type")!=null)
				bd.type=EpicMaterials.valueOf(object.get("Type").getAsString());
			else
				bd.type=EpicMaterials.valueOf(bd.block);
			//get data
			if(object.get("Data")!=null)
				bd.data=Byte.valueOf(object.get("Data").getAsString());
			else
				bd.data=bd.block.getData();
			//get skullOwner
			if(object.get("SkullOwner")!=null)
				bd.skullOwner=object.get("SkullOwner").getAsString();
		}
		//update this block
		bd.update();
		return bd;
	}
	@SuppressWarnings("deprecation")
	public static Blockdata adapt(Block block)
	{
		Blockdata bd=new Blockdata();
		if(CommonUtils.getServerVersion()>=13) //1.13 or above
		{
			bd.location=block.getLocation();
			bd.block=block;
			bd.type=EpicMaterials.valueOf(block);
		}
		else
		{
			bd.location=block.getLocation();
			bd.data=block.getData();
			bd.type=EpicMaterials.valueOf(block);
			bd.block=block;
			if(block.getState() instanceof Skull)
				bd.skullOwner=((Skull)block.getState()).getOwner();
		}
		return bd;
	}
	public boolean compareWith(Block block)
	{
		if(Blockdata.adapt(block).equals(this))
			return true;
		return false;
	}
	@SuppressWarnings("deprecation")
	public void update()
	{
		if(block==null||location==null)
			throw new InvalidCuboidException("BlockData not setup properly");
		if(!location.getBlock().equals(block))
			throw new InvalidCuboidException("Block & Location mismatched.");
		if(CommonUtils.getServerVersion()>=13) //1.13 or above
		{
			//set type & data only
			block.setType(type.getMaterial());
			block.setBlockData((BlockData) data);
		}
		else
		{
			//set all available data
			block.setType(type.getMaterial());
			//set data using reflections (block.setData(byte data) was removed in 1.13)
			setdata((byte)data);
			block.getState().update();
			if(block.getState() instanceof Skull&&skullOwner!=null)	//set skullOwner
				((Skull)block.getState()).setOwner(skullOwner);
		}
		//update block
		block.getState().update();
	}
	private void setData(final byte info) {
		RefClass Block=getRefClass("{cb}.block.CraftBlock");
		RefMethod setData=Block.getMethod("setData", byte.class);
		setData.of(block).call(data);
	}
	
	public @NotNull String serialize() {
		if ((block == null) || (location == null)) {
			throw new InvalidCuboidException("BlockData not setup properly");
		}
		
		final var object = new JsonObject();
		//set block location to data
		object.addProperty("Location", StringFormater.locationToString(block.getLocation(), false));
		
		//set other data (based on server version)
		if(type != null) {
			object.addProperty("Type", type.name());
		}
		
		if(data!=null) {
			object.addProperty("Data", dataObject());
		}
		
		if(skullOwner!=null) {
			object.addProperty("SkullOwner", skullOwner);
		}
		
		return object.toString();
	}
	
	private @NotNull String dataObject() {
		return ((BlockData) this.data).getAsString();
	}
	
	public @NotNull Location location() {
		return this.location;
	}
	
	public @NotNull Block block() {
		return this.block;
	}
	
	public @NotNull EpicMaterials type() {
		return this.type;
	}
	
	public @NotNull Object data() {
		return this.data;
	}
}
