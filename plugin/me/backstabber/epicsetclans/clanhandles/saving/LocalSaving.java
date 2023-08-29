package me.backstabber.epicsetclans.clanhandles.saving;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import me.backstabber.epicsetclans.clanhandles.manager.ClanTopManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.YMLManager;
import me.backstabber.epicsetclans.enums.ClanNodes;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class LocalSaving implements Saving {
	private EpicSetClans plugin;
	private EpicClanManager clanManager;
	private ClanTopManager topManager;
	private SavingManager savingManager;
	private HashMap<String, EpicClanData> clans = new HashMap<String, EpicClanData>();
	
	public LocalSaving(
		final EpicSetClans plugin,
		final EpicClanManager clanManager,
		final ClanTopManager topManager
	) {
		this.plugin = plugin;
		this.clanManager = clanManager;
		this.topManager = topManager;
	}

	@Override
	public void setup(final SavingManager savingManager) {
		this.savingManager = savingManager;
		
		if(!plugin.getSettings().getBoolean("mysql.enabled"))
		{
			reloadAllClans();
			int interval=(int) CommonUtils.evaluateString(plugin.getSettings().getFile().getString("settings.clan-backup-interval"));
			backup();
		}
	}

	@Override
	public void reloadAllClans() {
		for(String filename:plugin.getAllFiles("/clans"))
		{
			if(!filename.equals("sample"))
			{
				//load from normal clan location
				if(CommonUtils.checkConfig(new File(plugin.getDataFolder()+"/clans",filename+".yml")))
				{
					YMLManager file=new YMLManager(plugin, filename, "/clans");
					EpicClanData data=new EpicClanData(plugin, savingManager, clanManager, topManager, file.getFile());
					data.setup();
					clans.put(filename, data);
					file.save(new File(plugin.getDataFolder()+"/clanbackups",filename+".yml"),true);
					continue;
				}
				Bukkit.getConsoleSender().sendMessage(CommonUtils.chat("[EpicSet-Clans] &aCould'nt load clan data for &c"+filename+" &a's clan. Searching backups."));
				//if normal is corrupted & a backup existed
				if(CommonUtils.checkConfig(new File(plugin.getDataFolder()+"/clanbackups",filename+".yml")))
				{
					Bukkit.getConsoleSender().sendMessage(CommonUtils.chat("[EpicSet-Clans] &aFound backup for &c"+filename+" &a's clan. Using that instead."));
					YMLManager file=new YMLManager(plugin, filename, "/clanbackups");
					EpicClanData data=new EpicClanData(plugin, savingManager, clanManager, topManager, file.getFile());
					data.setup();
					clans.put(filename, data);
					file.save(new File(plugin.getDataFolder()+"/clans",filename+".yml"),true);
					continue;
				}
				Bukkit.getConsoleSender().sendMessage(CommonUtils.chat("[EpicSet-Clans] &cCould'nt find any backup for &c"+filename+" &c's clan. His clan will be reset."));
				//if no backup could be found
				new File(plugin.getDataFolder()+"/clans",filename+".yml").delete();
				new File(plugin.getDataFolder()+"/clanbackups",filename+".yml").delete();
			}
		}
	}
	@Override
	public void updateClan(String player) {
		
	}
	@Override
	public EpicClanData addNewClan(String leader, String clanName) {
		//create file
		EpicClanData data=new EpicClanData(plugin, savingManager, clanManager, topManager);
		data.setupNew(leader, clanName);
		clans.put(leader, data);
		//save locally
		File main=new File(plugin.getDataFolder()+"/clans",leader+".yml");
		try {
			data.getFile().save(main);
		} catch (IOException e) {
		}
		return data;
	}
	@Override
	public void deleteClan(String leader) {
		//delete from memory
		clans.remove(leader);
		//delete locally
		YMLManager file=new YMLManager(plugin, leader,"/clans");
		file.delete();
		
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
		return null;
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
	}
	@Override
	public boolean isInClan(String player) {
		//check in memory
		for(EpicClanData data:clans.values())
			if(data.getClanMembersName().contains(player))
				return true;
		return false;
	}
	@Override
	public boolean isLeader(String player) {
		//check in memory
		for(EpicClanData data:clans.values())
			if(data.getClanLeader().equals(player))
				return true;
		return false;
	}
	@Override
	public boolean isClanName(String name) {
		name=CommonUtils.chat(name);
		name=ChatColor.stripColor(name);
		//check in memory
		for(EpicClanData data:clans.values())
			if(data.getClanNameRaw().equals(name))
				return true;
		return false;
	}
	
	private void backup() {
		new BukkitRunnable() {
			final ArrayList<EpicClanData> files = new ArrayList<>(clans.values());
			
			int t = files.size();
			
			@Override
			public void run() {
				if (t <= 0) {
					this.cancel();
					return;
				}
				
				FileConfiguration file = files.get(t - 1).getFile();
				try {
					file.save(new File(plugin.getDataFolder()+"/clans",files.get(t-1).getClanLeader()+".yml"));
					file.save(new File(plugin.getDataFolder()+"/clanbackups",files.get(t-1).getClanLeader()+".yml"));
				} catch (final IOException ignored) {}
				
				t--;
			}
		}.runTaskTimerAsynchronously(plugin, 1L, 60 * 60);
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
