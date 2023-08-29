package me.backstabber.epicsetclans.clanhandles.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.data.ClanData;
import me.backstabber.epicsetclans.api.events.ClanCeateEvent;
import me.backstabber.epicsetclans.api.events.ClanDeleteEvent;
import me.backstabber.epicsetclans.api.events.ClanCeateEvent.CreateCause;
import me.backstabber.epicsetclans.api.events.ClanDeleteEvent.DeleteCause;
import me.backstabber.epicsetclans.api.manager.ClanManager;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.saving.SavingManager;

public class EpicClanManager implements ClanManager
{
	private EpicSetClans handle;
	private CostsManager costsManager;
	private SavingManager savingManager;
	private ClanTopManager topManager;
	
	public EpicClanManager(
		final EpicSetClans handle,
		final CostsManager costsManager,
		final SavingManager savingManager,
		final ClanTopManager topManager
	) {
		this.handle = handle;
		this.costsManager = costsManager;
		this.savingManager = savingManager;
		this.topManager = topManager;
	}

	@Override
	public boolean areAllies(ClanData alpha, ClanData bravo) {
		if(alpha.getClanAllies().contains(bravo)&&bravo.getClanAllies().contains(alpha))
			return true;
		return false;
	}
	@Override
	public boolean areTruces(ClanData alpha, ClanData bravo) {
		if(alpha.getClanTruce().contains(bravo)&&bravo.getClanTruce().contains(alpha))
			return true;
		return false;
	}
	@Override
	public void removeAllies(ClanData alpha, ClanData bravo) {
		((EpicClanData) alpha).removeClanAlly(bravo);
		((EpicClanData) bravo).removeClanAlly(alpha);
	}
	@Override
	public void removeTruce(ClanData alpha, ClanData bravo) {
		((EpicClanData) alpha).removeClanTruce(bravo);
		((EpicClanData) bravo).removeClanTruce(alpha);
	}
	@Override
	public void makeAllies(ClanData alpha, ClanData bravo) {
		removeAllies(alpha, bravo);
		removeTruce(alpha, bravo);
		((EpicClanData) alpha).addClanAlly(bravo);
		((EpicClanData) bravo).addClanAlly(alpha);
	}
	@Override
	public void makeTruces(ClanData alpha, ClanData bravo) {
		removeAllies(alpha, bravo);
		removeTruce(alpha, bravo);
		((EpicClanData) alpha).addClanTruce(bravo);
		((EpicClanData) bravo).addClanTruce(alpha);
	}
	public ClanData createNewClan(Player player, String clanName,boolean bypass,CreateCause cause) {
		if(!bypass&&handle.getSettings().getBoolean("creation-costs.enabled"))
		{
			if(!costsManager.apply(player))
			{
				return null;
			}
		}
		String leader=player.getName();
		if(!isInClan(leader)&&!isClanName(clanName))
		{
			ClanData data = savingManager.createNewClan(player.getName(), clanName);
			ClanCeateEvent event=new ClanCeateEvent(player, data,CreateCause.PLAYER);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
			{
				deleteClan(data.getClanName());
				return null;
			}
			return data;
		}
		return null;
	}
	@Override
	public ClanData createNewClan(Player player, String clanName,boolean bypass) {
		return createNewClan(player, clanName,bypass, CreateCause.API);
	}
	@Override
	public boolean isLeader(String player) {
		return savingManager.isLeader(player);
	}
	@Override
	public boolean isInClan(String player) {
		return savingManager.isInClan(player);
	}
	@Override
	public boolean isLeader(Player player) {
		return isLeader(player.getName());
	}
	@Override
	public boolean isInClan(Player player) {
		return isInClan(player.getName());
	}
	@Override
	public boolean isClanName(String clanName) {
		return savingManager.isClanName(clanName);
	}
	
	@Override
	public ClanData getClanData(String name) {
		return savingManager.getClan(name);
	}
	
	@Override
	public ClanData getClanData(Player player) {
		return savingManager.getClan(player.getName());
	}
	
	public void deleteClan(String name) {
		ClanData data = getClanData(name);
		if(data!=null)
		{
			for(ClanData ally:data.getClanAllies())
				((EpicClanData) ally).removeClanAlly(data);
			for(ClanData truce:data.getClanTruce())
				((EpicClanData) truce).removeClanTruce(data);
			savingManager.deleteClan(data.getClanLeader());
		}
	}
	public void deleteClan(ClanData clan) {
		ClanDeleteEvent event=new ClanDeleteEvent(Bukkit.getConsoleSender(),clan,DeleteCause.API);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		deleteClan(clan.getClanNameRaw());
	}
	@Override
	public void makeLeader(String member) {
		if(isInClan(member))
		{
			EpicClanData data = (EpicClanData) getClanData(member);
			String oldLeader = data.getClanLeader();
			data.setClanLeader(member);
			FileConfiguration file=data.getFile();
			try {
				file.save(new File(handle.getDataFolder()+"/clans",member+".yml"));
			} catch (IOException e) {
			}
			savingManager.deleteClan(oldLeader);
			EpicClanData cdata=new EpicClanData(EpicSetClans.get(), savingManager, this, topManager, file);
			cdata.setup();
			savingManager.save(file);
		}
	}
	@Override
	public void makeLeader(Player player) {
		makeLeader(player.getName());
	}
	public List<ClanData> getAllClans() {
		List<ClanData> clans=new ArrayList<ClanData>();
		for(ClanData clan:savingManager.getAllClans())
			clans.add(clan);
		return clans;
	}
	@Override
	public boolean isSpying(Player player) {
		return player.hasMetadata("EpicChatSpy");
	}
	
}
