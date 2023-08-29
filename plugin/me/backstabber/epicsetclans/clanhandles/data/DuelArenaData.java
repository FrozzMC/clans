package me.backstabber.epicsetclans.clanhandles.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.manager.YMLManager;
import me.backstabber.epicsetclans.utils.Cuboid;
import me.backstabber.epicsetclans.utils.StringFormater;
import me.backstabber.epicsetclans.utils.materials.EpicMaterials;
import me.backstabber.epicsetclans.utils.materials.UMaterials;

public class DuelArenaData 
{ 
	private EpicSetClans handle;
	private Cuboid area;
	private Location spawnAlpha;
	private Location spawnBravo;
	private String arenaName;
	private YMLManager file;
	private ArenaState state;
	private List<Player> ignored=new ArrayList<>();
	private ItemStack item=EpicMaterials.valueOf(UMaterials.GRASS_BLOCK).getItemStack();

	public DuelArenaData(final EpicSetClans handle) {
		this.handle = handle;
	}

	public DuelArenaData load(YMLManager file)
	{
		if(file.getFile().isSet("arena-settings")&&file.getFile().isSet("cuboid"))
		{
			DuelArenaData data=new DuelArenaData(EpicSetClans.get());
			data.area=Cuboid.getFromFile(handle,file);
			data.spawnAlpha=StringFormater.stringToLocation(file.getFile().getString("arena-settings.alpha-location"));
			data.spawnBravo=StringFormater.stringToLocation(file.getFile().getString("arena-settings.bravo-location"));
			data.arenaName=file.getFile().getString("arena-settings.arena-name");
			if(file.getFile().isSet("arena-settings.item"))
				data.item=file.getFile().getItemStack("arena-settings.item");
			data.file=file;
			data.state=ArenaState.READY;
			return data;
		}
		return null;
	}
	public DuelArenaData createNew(String arenaName,Cuboid area,Location spawnAlpha,Location spawnBravo)
	{
		DuelArenaData data=new DuelArenaData(EpicSetClans.get());
		data.area=area;
		data.spawnAlpha=spawnAlpha;
		data.spawnBravo=spawnBravo;
		data.arenaName=arenaName;
		data.file=new YMLManager(handle, "sample", "/arenas",arenaName);
		data.saveToFile();
		data.state=ArenaState.READY;
		return data;
	}
	public void playerMoveHandle(PlayerMoveEvent event)
	{
		Location to=event.getTo();
		Player player=event.getPlayer();
		if(ignored.contains(player))
			return;
		if(area.isInCuboid(to))
		{
			if(!player.hasPermission("epicsetclans.bypassarena"))
			{
				Location spawn;
				if(Bukkit.getWorld("world")!=null)
					spawn=Bukkit.getWorld("world").getSpawnLocation();
				else
					spawn=player.getWorld().getSpawnLocation();
				player.teleport(spawn);
			}
		}
	}
	public ArenaState getState()
	{
		return this.state;
	}
	public void addIgnored(Player player)
	{
		ignored.add(player);
	}
	public void addIgnored(List<Player> player)
	{
		for(Player p:player)
			ignored.add(p);
	}
	public void removeIgnored(Player player)
	{
		Location spawn;
		if(Bukkit.getWorld("world")!=null)
			spawn=Bukkit.getWorld("world").getSpawnLocation();
		else
			spawn=player.getWorld().getSpawnLocation();
		player.teleport(spawn);
		ignored.remove(player);
	}
	public void clearIgnored()
	{
		for(Player player:ignored)
		{
			Location spawn;
			if(Bukkit.getWorld("world")!=null)
				spawn=Bukkit.getWorld("world").getSpawnLocation();
			else
				spawn=player.getWorld().getSpawnLocation();
			player.teleport(spawn);
		}
		this.ignored.clear();
	}
	public void tpToSpawnAlpha(Player player)
	{
		addIgnored(player);
		player.teleport(spawnAlpha);
	}
	public void tpToSpawnAlpha(List<Player> player)
	{
		addIgnored(player);
		for(Player p:player)
			p.teleport(spawnAlpha);
	}
	public void tpToSpawnBravo(Player player)
	{
		addIgnored(player);
		player.teleport(spawnBravo);
	}
	public void tpToSpawnBravo(List<Player> player)
	{
		addIgnored(player);
		for(Player p:player)
			p.teleport(spawnBravo);
	}
	public void setState(ArenaState state)
	{
		this.state=state;
	}
	public void saveToFile()
	{
		area.saveToFile(file);
		file.getFile().set("arena-settings.arena-name", arenaName);
		file.getFile().set("arena-settings.alpha-location", StringFormater.locationToString(spawnAlpha, true));
		file.getFile().set("arena-settings.bravo-location", StringFormater.locationToString(spawnBravo, true));
		file.getFile().set("arena-settings.item", item);
		file.save(true);
	}
	public void setItem(ItemStack item)
	{
		this.item=item.clone();
	}
	public ItemStack getItem()
	{
		return this.item.clone();
	}
	public enum ArenaState
	{
		UNLOADED,READY,BOOKED,IN_USE;
	}
	public String getName() 
	{
		return this.arenaName;
	}
}
