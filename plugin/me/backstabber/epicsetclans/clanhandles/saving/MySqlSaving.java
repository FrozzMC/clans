package me.backstabber.epicsetclans.clanhandles.saving;


import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import me.backstabber.epicsetclans.clanhandles.manager.ClanTopManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.MySqlManager;
import me.backstabber.epicsetclans.clanhandles.manager.YMLManager;
import me.backstabber.epicsetclans.enums.ClanNodes;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class MySqlSaving implements Saving {
	private EpicSetClans plugin;
	private EpicClanManager clanManager;
	private ClanTopManager topManager;
	private SavingManager savingManager;
	private MySqlManager sql;
	private HashMap<String, EpicClanData> clans=new HashMap<String, EpicClanData>();
	
	public MySqlSaving(
		final EpicSetClans plugin,
		final EpicClanManager clanManager,
		final ClanTopManager topManager,
		final MySqlManager sql
	) {
		this.plugin = plugin;
		this.clanManager = clanManager;
		this.topManager = topManager;
		this.sql = sql;
	}
	
	@Override
	public void setup(final SavingManager savingManager) {
		this.savingManager = savingManager;

		if(!plugin.getSettings().getBoolean("mysql.enabled"))
			return;
		sql.create(
				plugin.getSettings().getString("mysql.server.host"), 
				plugin.getSettings().getString("mysql.server.port"), 
				plugin.getSettings().getString("mysql.server.database"), 
				plugin.getSettings().getString("mysql.server.username"), 
				plugin.getSettings().getString("mysql.server.password"));
		new BukkitRunnable() {
			@Override
			public void run() {
				sql.createTable();
			}
		}.runTaskAsynchronously(plugin);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				reloadAllClans();
			}
		}.runTaskTimer(plugin, 10, 20 * 30);
	}

	@Override
	public void reloadAllClans() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				File fileLocation;
				EpicClanData data;
				
				//get all leaders
				for(String leader:sql.getAllLeaders()) {
					fileLocation = new File(plugin.getDataFolder()+"/clans",leader+".yml");
					if(fileLocation.exists()) {
						data = new EpicClanData(plugin, savingManager, clanManager, topManager, sql.downloadClan(
							leader,
							YamlConfiguration.loadConfiguration(fileLocation)
						));
					} else {
						data = new EpicClanData(plugin, savingManager, clanManager, topManager, sql.downloadClan(leader));
					}
					
					data.setup();
					clans.put(leader, data);
					
					try {
						data.getFile().save(fileLocation);
					} catch (final IOException ignored) {}
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	@Override
	public void updateClan(String player) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(sql.isInClan(player)) {
					for(EpicClanData data:clans.values()) {
						if(data.getClanMembersName().contains(player))
							return;
					}
					EpicClanData data=new EpicClanData(plugin, savingManager, clanManager, topManager, sql.downloadClan(player));
					File fileLocation=new File(plugin.getDataFolder()+"/clans",data.getClanLeader()+".yml");
					if(fileLocation.exists()) {
						FileConfiguration file=YamlConfiguration.loadConfiguration(fileLocation);
						data =new EpicClanData(plugin, savingManager, clanManager, topManager, sql.downloadClan(data.getClanLeader(),file));
					}
					data.setup();
					clans.put(data.getClanLeader(), data);
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	@Override
	public EpicClanData addNewClan(String leader, String clanName) {
		//create file
		EpicClanData data=new EpicClanData(plugin, savingManager, clanManager, topManager);
		data.setupNew(leader, clanName);
		clans.put(leader, data);
		//save locally
		this.clans.put(leader, data);
		File main=new File(plugin.getDataFolder()+"/clans",data.getClanLeader()+".yml");
		try {
			data.getFile().save(main);
		} catch (IOException e) {
		}
		//save on database
		new BukkitRunnable() {
			
			@Override
			public void run() {
				sql.uploadClan(data.getFile());
			}
		}.runTaskAsynchronously(plugin);
		return data;
	}
	@Override
	public void deleteClan(String leader) {
		//delete from memory
		if(clans.containsKey(leader))
			clans.remove(leader);
		//delete locally
		YMLManager file=new YMLManager(plugin, leader,"/clans");
		file.delete();
		//delete on database
		new BukkitRunnable() {
			
			@Override
			public void run() {
				sql.deleteClan(leader);
			}
		}.runTaskAsynchronously(plugin);
	}
	@Override
	public void deleteClan(FileConfiguration file) {
		if(!file.isSet(ClanNodes.CLAN_LEADER.node()))
			return;
		deleteClan(file.getString(ClanNodes.CLAN_LEADER.node()));
	}
	@Override
	public EpicClanData getClan(String member) {
		for(EpicClanData data:clans.values())
			if(data.getClanMembersName().contains(member))
				return data;
			else 
			{
				String name=CommonUtils.chat(member);
				name=ChatColor.stripColor(name);
				if(data.getClanNameRaw().equalsIgnoreCase(name))
					return data;
			}
		FileConfiguration cfile=sql.downloadClan(member);
		if(cfile==null)
			return null;
		EpicClanData data=new EpicClanData(plugin, savingManager, clanManager, topManager, cfile);
		File fileLocation=new File(plugin.getDataFolder()+"/clans",data.getClanLeader()+".yml");
		if(fileLocation.exists()) {
			FileConfiguration file=YamlConfiguration.loadConfiguration(fileLocation);
			data =new EpicClanData(plugin, savingManager, clanManager, topManager, sql.downloadClan(data.getClanLeader(),file));
		}
		data.setup();
		clans.put(data.getClanLeader(), data);
		return data;
	}
	@Override
	public void saveClan(FileConfiguration file) {
		//save in memory
		EpicClanData data=new EpicClanData(plugin, savingManager, clanManager, topManager, file);
		data.setup();
		clans.put(data.getClanLeader(), data);
		//save locally
		File fileLocation=new File(plugin.getDataFolder()+"/clans",data.getClanLeader()+".yml");
		if(!fileLocation.exists())
			try {
				fileLocation.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		try {
			file.save(fileLocation);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//save on database
		new BukkitRunnable() {
			
			@Override
			public void run() {
				sql.uploadClan(file);
			}
		}.runTaskAsynchronously(plugin);
	}
	@Override
	public boolean isInClan(String player) {
		//check in memory
		for(EpicClanData data:clans.values())
			if(data.getClanMembersName().contains(player))
				return true;
		//check in database
		return sql.isInClan(player);
	}
	@Override
	public boolean isLeader(String player) {
		//check in memory
		for(EpicClanData data:clans.values())
			if(data.getClanLeader().equals(player))
				return true;
		//check in database
		return sql.isClanLeader(player);
	}
	@Override
	public boolean isClanName(String name) {
		name=CommonUtils.chat(name);
		name=ChatColor.stripColor(name);
		//check in memory
		for(EpicClanData data:clans.values())
			if(data.getClanNameRaw().equals(name))
				return true;
		//check in database
		return sql.isClanName(name);
	}

	@Override
	public void saveClanFast(FileConfiguration file) {
		String leader=file.getString(ClanNodes.CLAN_LEADER.node());
		//save locally
		File fileLocation=new File(plugin.getDataFolder()+"/clans",leader+".yml");
		if(!fileLocation.exists())
			try {
				fileLocation.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		try {
			file.save(fileLocation);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Collection<EpicClanData> getAllClans() {
		return clans.values();
	}
	
}
