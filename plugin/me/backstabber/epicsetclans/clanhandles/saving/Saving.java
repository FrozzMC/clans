package me.backstabber.epicsetclans.clanhandles.saving;

import java.util.Collection;

import org.bukkit.configuration.file.FileConfiguration;

import me.backstabber.epicsetclans.clanhandles.saving.SavingManager;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;

public interface Saving {
	public void reloadAllClans();
	
	public EpicClanData addNewClan(String leader,String clanName);
	public EpicClanData getClan(String member);
	
	public void deleteClan(String leader);
	public void deleteClan(FileConfiguration file);

	public void saveClan(FileConfiguration file);
	public void saveClanFast(FileConfiguration file);
	public void updateClan(String player);
	
	public boolean isInClan(String player);
	public boolean isLeader(String player);
	public boolean isClanName(String name);
	
	public Collection<EpicClanData> getAllClans();

	public void setup(final SavingManager savingManager);
}
