package net.velex.clans.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.velex.clans.utils.location.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

public class BlockDataUtils {
	private Location location;
	private Material type;
  /**
   * blockData in case of 1.13 & above & byte incase of below
   */
	private Object data;
	private Block block;
	
	@SuppressWarnings("deprecation")
	public static @NotNull BlockDataUtils deserialize(final @NotNull String str) {
		final var jsonObject = (JsonObject) new JsonParser().parse(str);
    // Checks if contains the 'Location' value.
		if(jsonObject.get("Location") == null) {
			throw new IllegalArgumentException(String.format("Can't deserialize the string %s", str));
		}
		final var blockData = new BlockDataUtils();
		final var locSplit = jsonObject.get("Location")
			.getAsString()
			.split(";");
		blockData.location = new Location(
			Bukkit.getWorld(locSplit[0]),
			Double.parseDouble(locSplit[1]),
			Double.parseDouble(locSplit[2]),
			Double.parseDouble(locSplit[3])
		);
		blockData.block = blockData.location.getBlock();
		// If the 'Type' value isn't null, get that value for the material type.
    // Else, just use the material of the block.
		if (jsonObject.get("Type") != null) {
			blockData.type = Material.valueOf(jsonObject.get("Type").getAsString());
		} else {
			blockData.type = blockData.block.getType();
		}
		// If the 'Data' value isn't null, get that value for the block data creation.
    // Else, use the block data from the current block.
		if (jsonObject.get("Data") != null) {
			blockData.data = Bukkit.createBlockData(jsonObject.get("Data").getAsString());
		} else {
			blockData.data = blockData.block.getBlockData();
		}
		//update this block
		blockData.update();
		return blockData;
	}

	public static @NotNull BlockDataUtils adapt(final @NotNull Block block) {
		final var blockData = new BlockDataUtils();
		blockData.location = block.getLocation();
		blockData.block = block;
		blockData.type = block.getType();
		return blockData;
	}
	
	public boolean compareWith(final @NotNull Block block) {
		return BlockDataUtils.adapt(block).equals(this);
	}

	public void update() {
    // Checks if the block, or the location are null.
		if ((block == null) || (location == null)) {
			throw new IllegalArgumentException("BlockData not setup properly");
		}
    // Checks if the block of the location is equals to the current block.
		if (!location.getBlock().equals(block)) {
			throw new IllegalArgumentException("Block & Location mismatched.");
		}
		block.setType(type);
		block.setBlockData((BlockData) data);
		block.getState().update();
	}
	
	public @NotNull String serialize() {
    // Checks if the block, or the location are null.
		if ((block == null) || (location == null)) {
			throw new IllegalArgumentException("BlockData not setup properly");
		}
		final var object = new JsonObject();
		//set block location to data
		object.addProperty("Location", LocationUtils.to(block.getLocation(), false));
		// Checks if the material type is null.
		if (type != null) {
			object.addProperty("Type", type.name());
		}
    // Checks if the data value is null.
		if (data != null) {
			object.addProperty("Data", dataAsString());
		}
		return object.toString();
	}
	
	private @NotNull String dataAsString() {
		return ((BlockData) this.data).getAsString();
	}
	
	public @NotNull Location location() {
		return this.location;
	}
	
	public @NotNull Block block() {
		return this.block;
	}
	
	public @NotNull Material type() {
		return this.type;
	}
	
	public @NotNull Object data() {
		return this.data;
	}
}
