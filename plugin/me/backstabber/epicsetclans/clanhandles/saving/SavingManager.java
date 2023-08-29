package me.backstabber.epicsetclans.clanhandles.saving;


import java.util.Collection;

import org.bukkit.configuration.file.FileConfiguration;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;

public class SavingManager {
	@SuppressWarnings("unused")
	private EpicSetClans plugin;
	private Saving saving;

	public SavingManager(final EpicSetClans plugin, final Saving saving) {
		this.plugin = plugin;
		this.saving = saving;
	}

	public void setup() {
		saving.setup(this);
	}
	public void save(FileConfiguration file) {
		saving.saveClan(file);
	}
	public void saveFast(FileConfiguration file) {
		saving.saveClanFast(file);
	}
	public void deleteClan(String leader) {
		saving.deleteClan(leader);
	}
	public Collection<EpicClanData> getAllClans() {
		return saving.getAllClans();
	}
	public EpicClanData createNewClan(String player,String clanName) {
		return saving.addNewClan(player, clanName);
	}
	public EpicClanData getClan(String name) {
		return saving.getClan(name);
	}
	public boolean isLeader(String player) {
		return saving.isLeader(player);
	}
	public boolean isInClan(String player) {
		return saving.isInClan(player);
	}
	public boolean isClanName(String name) {
		return saving.isClanName(name);
	}
	public void updateClan(String name) {
		saving.updateClan(name);
	}
	
}
