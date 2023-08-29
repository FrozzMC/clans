package me.backstabber.epicsetclans.clanhandles.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import me.backstabber.epicsetclans.utils.CommonUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.data.ClanData;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.data.ClanDuelData;
import me.backstabber.epicsetclans.clanhandles.data.DuelArenaData;
import me.backstabber.epicsetclans.clanhandles.data.KitsData;
import me.backstabber.epicsetclans.messager.MessageFormatter;
import me.backstabber.epicsetclans.clanhandles.data.DuelArenaData.ArenaState;
import me.backstabber.epicsetclans.utils.Cuboid;

public class ClanDuelManager 
{
	private EpicSetClans handle;
	private EpicClanManager clanManager;
	private DuelArenaData arenaData;
	private ClanDuelData duelData;
	private YMLManager kitsFile;
	private ConcurrentMap<String,ClanDuelData> runningDuels=new ConcurrentHashMap<>();
	private Map<String, DuelArenaData> registeredArenas=new ConcurrentHashMap<String, DuelArenaData>();
	private Map<EpicClanData, Integer> duelRecord=new HashMap<>();
	private Map<String, KitsData> kits=new HashMap<>();
	
	public ClanDuelManager(
		final EpicSetClans handle,
		final EpicClanManager clanManager,
		final DuelArenaData arenaData,
		final ClanDuelData duelData
	) {
		this.handle = handle;
		this.clanManager = clanManager;
		this.arenaData = arenaData;
		this.duelData = duelData;
	}

	public void setup()
	{
		kitsFile=new YMLManager(handle, "Kits", null);
		//load all arenas
		for(String filename:handle.getAllFiles("/arenas"))
		{
			if(filename.equals("sample"))
				continue;
			YMLManager file=new YMLManager(handle, filename, "/arenas");
			if(arenaData.load(file)!=null)
			{
				registeredArenas.put(filename, arenaData.load(file)).setState(ArenaState.READY);
			}
			else
			{
				file.delete();
			}
		}
		//load all kits
		for(String kitName:kitsFile.getFile().getKeys(false))
		{
			this.kits.put(kitName, KitsData.load(kitsFile.getFile().getConfigurationSection(kitName), kitName));
		}
		//start task for duels
		new BukkitRunnable() 
		{
			@Override
			public void run() 
			{
				for(ClanDuelData duels:runningDuels.values())
				{
					duels.runSecond();
				}
			}
		}.runTaskTimer(handle, 20, 20);
		//start task for record
		new BukkitRunnable() 
		{
			@Override
			public void run() 
			{
				duelRecord.clear();
			}
		}.runTaskTimer(handle, 20, 20*60*60*24);
	}
	public KitsData addKit(String kitName,List<ItemStack> kit)
	{
		if(getKit(kitName)==null)
		{
			KitsData data = KitsData.createNew(kitName, kit);
			data.save(kitsFile);
			this.kits.put(kitName, data);
			return data;
		}
		return null;
	}
	public void removeKit(KitsData kit) 
	{
		kit.remove(kitsFile);
		if(this.kits.containsKey(kit.getName()))
			this.kits.remove(kit.getName());
	}
	public void handlePlayerDeath(PlayerDeathEvent event)
	{
		Player player=event.getEntity();
		if(clanManager.isInClan(player.getName()))
		{
			ClanData clan = clanManager.getClanData(player.getName());
			if(isDueling(clan))
				getDuel(clan).handleDeath(player);
		}
	}
	public void handlePlayerLeave(PlayerQuitEvent event)
	{
		Player player=event.getPlayer();
		if(clanManager.isInClan(player.getName()))
		{
			ClanData clan = clanManager.getClanData(player.getName());
			if(isDueling(clan))
				getDuel(clan).handleDeath(player);
		}
	}
	public void sendDuelRequest(ClanDuelData duel)
	{
		final FileConfiguration duelSettings = handle.getDuelSettings().getFile();
		final List<String> message = new ArrayList<>();
		
		for(String s : duelSettings.getStringList("messages.on-request"))
		{
			s=s.replace("%clan%", duel.getClanAlpha().getClanName());
			s=s.replace("%arena%", duel.getArena().getName());
			s=s.replace("%teamsize%", String.valueOf(duel.getTeamSize()));
			message.add(s);
		}
		
		for(String playerName : duel.getClanBravo().getClanMembersName())
			if(Bukkit.getPlayerExact(playerName)!=null)
			{
				Player player=Bukkit.getPlayerExact(playerName);
				player.setMetadata("EpicDuelRequest", new FixedMetadataValue(handle, true));
				MessageFormatter.sendJSONMessage(player, handle, message);
				player.sendTitle(
					CommonUtils.chat(duelSettings.getString("messages.title-request")),
					CommonUtils.chat(duelSettings.getString("messages.subtitle-request")),
					5, 40, 5
				);
			}
		duel.sentDuel(duel.getClanBravo());
	}
	public void acceptDuelRequest(ClanDuelData duel)
	{
		for(String playerName:duel.getClanBravo().getClanMembersName())
			if(Bukkit.getPlayerExact(playerName)!=null)
			{
				Player player=Bukkit.getPlayerExact(playerName);
				player.setMetadata("EpicDuel", new FixedMetadataValue(handle, true));
			}
		for(String playerName:duel.getClanAlpha().getClanMembersName())
			if(Bukkit.getPlayerExact(playerName)!=null)
			{
				Player player=Bukkit.getPlayerExact(playerName);
				player.setMetadata("EpicDuel", new FixedMetadataValue(handle, true));
			}
		duel.acceptDuel(duel.getClanBravo());
	}
	public KitsData getKit(String kitName)
	{
		if(this.kits.containsKey(kitName))
			return this.kits.get(kitName);
		return null;
	}
	public List<KitsData> getAllKits()
	{
		List<KitsData> list=new ArrayList<KitsData>();
		for(KitsData kit:this.kits.values())
			list.add(kit);
		return list;
	}
	public void saveAllKits()
	{
		for(KitsData kit:kits.values())
			kit.save(kitsFile);
	}
	public ClanDuelData createNewDuel(EpicClanData clanAlpha,EpicClanData clanBravo)
	{
		if(isDueling(clanAlpha) || isDueling(clanBravo)) {
			System.out.println(String.format("First condition: %s", true));
			return null;
		}
		
		if(!canDuel(clanAlpha) || !canDuel(clanBravo)) {
			System.out.println(String.format("Second condition: %s", true));
			return null;
		}

		final DuelArenaData arena = getFirstFree();
		if (arena == null) {
			System.out.println(String.format("Third condition: %s", true));
			return null;
		}

		ClanDuelData duel = duelData.createNew(clanManager, EpicSetClans.get().getClanDuelManager(), arena, clanAlpha, clanBravo);
		if(duel==null) {
			System.out.println(String.format("Fourd condition: %s", true));
			return null;
		}
		
		this.runningDuels.put(duel.getClanAlpha().getClanName(), duel);
		//this.duelRecord.put(clanAlpha, this.duelRecord.get(clanAlpha)+1);
		//this.duelRecord.put(clanBravo, this.duelRecord.get(clanBravo)+1);
		return duel;
	}
	public void removeDuel(String clanName)
	{
		this.runningDuels.remove(clanName);
	}
	
	public void stopDuel(ClanDuelData duel)
	{
		duel.stop();
		this.runningDuels.remove(duel.getClanAlpha().getClanName());
	}
	public boolean canDuel(EpicClanData data)
	{
		if(!duelRecord.containsKey(data))
			duelRecord.put(data, 0);
		if(duelRecord.get(data)>=data.getDuelsUpgrade())
			return false;
		return true;
	}
	public ClanDuelData getDuel(ClanData clan)
	{
		for(ClanDuelData duels:runningDuels.values())
		{
			if(duels.getClanAlpha().equals(clan)||duels.getClanBravo().equals(clan))
				return duels;
		}
		return null;
	}
	public Collection<ClanDuelData> getRunningDuels()
	{
		return this.runningDuels.values();
	}
	public boolean isDueling(ClanData clan)
	{
		for(ClanDuelData duels:runningDuels.values())
		{
			if (duels.getClanAlpha().equals(clan) || duels.getClanBravo().equals(clan)) {
				return true;
			}
		}
		return false;
	}
	public DuelArenaData registerNewArena(String arenaName,Cuboid area,Location spawnAlpha,Location spawnBravo)
	{
		if(!this.registeredArenas.containsKey(arenaName))
		{
			DuelArenaData data=arenaData.createNew(arenaName, area, spawnAlpha, spawnBravo);
			this.registeredArenas.put(arenaName, data);
			return data;
		}
		return null;
	}
	public Map<String, DuelArenaData> getAllArenas()
	{
		return registeredArenas;
	}
	public DuelArenaData getArena(String arenaName)
	{
		if(registeredArenas.containsKey(arenaName))
			return registeredArenas.get(arenaName);
		return null;
	}
	public void deleteArena(DuelArenaData arena)
	{
		if(registeredArenas.containsKey(arena.getName()))
			registeredArenas.remove(arena.getName());
		File file=new File(handle.getDataFolder()+"/arenas",arena.getName()+".yml");
		file.delete();
	}
	public DuelArenaData getFirstFree()
	{
		for(DuelArenaData arena:registeredArenas.values())
			if(arena.getState().equals(ArenaState.READY)) {
				System.out.println(String.format("Current state of the arena: %s", arena.getState().name()));
				return arena;
			}
		return null;
	}
}
