package me.backstabber.epicsetclans.clanhandles.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.data.BaseData;
import me.backstabber.epicsetclans.api.data.ClanData;
import me.backstabber.epicsetclans.api.data.PlayerData;
import me.backstabber.epicsetclans.api.data.VaultsData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanTagManager;
import me.backstabber.epicsetclans.clanhandles.manager.ClanTopManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.clanhandles.saving.SavingManager;
import me.backstabber.epicsetclans.enums.ClanNodes;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class EpicClanData implements ClanData
{
	private EpicSetClans handle;
	private EpicClanManager clanManager;
	private ClanTopManager topManager;
	private SavingManager savingManager;
	private FileConfiguration file;
	private Map<String, PlayerData> memberData=new HashMap<>();
	private ClanVaultsData vaults;
	private ClanBasesData bases;
	
	public EpicClanData(
		final EpicSetClans handle,
		final SavingManager savingManager,
		final EpicClanManager clanManager,
		final ClanTopManager topManager
	) {
		this.handle = handle;
		this.savingManager = savingManager;
		this.clanManager = clanManager;
		this.topManager = topManager;
		this.file=new YamlConfiguration();
		//create default data
		for(ClanNodes data:ClanNodes.values())
		{
			if(!file.isSet(data.node()))
				file.set(data.node(), data.value());
		}
		
	}
	
	public EpicClanData(
		final EpicSetClans handle,
		final SavingManager savingManager,
		final EpicClanManager clanManager,
		final ClanTopManager topManager,
		final FileConfiguration file
	) {
		this.handle = handle;
		this.savingManager = savingManager;
		this.clanManager = clanManager;
		this.topManager = topManager;
		this.file=file;
		//create default data
		for(ClanNodes data:ClanNodes.values())
		{
			if(!file.isSet(data.node()))
				file.set(data.node(), data.value());
		}
	}
	public void setupNew(String leader,String clanName) {
		//set new clan data
		file.set(ClanNodes.CLAN_MEMBERS.node(), Arrays.asList(leader));
		//update member's data
		memberData.clear();
		memberData.put(leader, new ClanPlayersData(leader, file));
		file.set(ClanNodes.CLAN_NAME.node(), clanName);
		file.set(ClanNodes.CLAN_TAG.node(), ClanTagManager.generateTag(getClanNameRaw()));
		file.set(ClanNodes.CLAN_LEADER.node(), leader);
		//set clan specific vaults
		this.vaults=new ClanVaultsData(handle,savingManager, file, this);
		this.bases=new ClanBasesData(savingManager,file);
	}
	public void setup()
	{
		//load all member's data
		for(String member:getClanMembersName())
			memberData.put(member, new ClanPlayersData(member, file));
		//set clan specific vaults
		this.vaults=new ClanVaultsData(handle,savingManager, file, this);
		this.bases=new ClanBasesData(savingManager, file);
	}
	public boolean canRename()
	{
		if(!file.isSet(ClanNodes.RENAME_TIME.node()))
			return true;
		double current = System.currentTimeMillis()/1000D;
		double old = file.getDouble(ClanNodes.RENAME_TIME.node());
		double timer=CommonUtils.evaluateString(handle.getSettings().getFile().getString("settings.clan-rename-timer"));
		if((current-old)>=timer)
		{
			return true;
		}
		return false;
	}
	public Object getFromNode(ClanNodes node)
	{
		return file.get(node.node());
	}
	public void setFromNode(ClanNodes node,Object value)
	{
		file.set(node.node(), value);
		if(node.equals(ClanNodes.UPGRADE_VAULTS))
			this.vaults.saveAll();
		savingManager.save(file);
	}
	public String getRenameTimeLeft()
	{
		double old=file.getDouble(ClanNodes.RENAME_TIME.node());
		double current=System.currentTimeMillis()/1000D;
		double timer=CommonUtils.evaluateString(handle.getSettings().getFile().getString("settings.clan-rename-timer"));
		return CommonUtils.getTimeFormat((int) ((timer)-(current-old)));
	}
	public void updateRenameTimer()
	{
		file.set(ClanNodes.RENAME_TIME.node(), System.currentTimeMillis()/1000D);
		savingManager.save(file);
	}
	public int getHomeUpgrade()
	{
		return file.getInt(ClanNodes.UPGRADE_HOME.node());
	}
	public void setHomeUpgrade(int value)
	{
		file.set(ClanNodes.UPGRADE_HOME.node(), value);
		savingManager.save(file);
		}
	public int getMemberUpgrade()
	{
		return file.getInt(ClanNodes.UPGRADE_MEMBERS.node());
	}
	public void setMemberUpgrade(int value)
	{
		file.set(ClanNodes.UPGRADE_MEMBERS.node(), value);
		savingManager.save(file);
	}
	public int getVaultsUpgrade()
	{
		return file.getInt(ClanNodes.UPGRADE_VAULTS.node());
	}
	public void setVaultUpgrade(int value)
	{
		file.set(ClanNodes.UPGRADE_VAULTS.node(), value);
		savingManager.save(file);
	}
	public int getDuelsUpgrade()
	{
		return file.getInt(ClanNodes.UPGRADE_DUELS.node());
	}
	public void setDuelsUpgrade(int value)
	{
		file.set(ClanNodes.UPGRADE_DUELS.node(), value);
		savingManager.save(file);
	}
	public int getAlliesUpgrade()
	{
		return file.getInt(ClanNodes.UPGRADE_ALLIES.node());
	}
	public void setAlliesUpgrade(int value)
	{
		file.set(ClanNodes.UPGRADE_ALLIES.node(), value);
		savingManager.save(file);
	}
	public int getWonDuels()
	{
		return file.getInt(ClanNodes.DUELS_WON.node());
	}
	public void setWonDuels(int value)
	{
		file.set(ClanNodes.DUELS_WON.node(), value);
		savingManager.save(file);
	}
	public int getLostDuels()
	{
		return file.getInt(ClanNodes.DUELS_LOST.node());
	}
	public void setLostDuels(int value)
	{
		file.set(ClanNodes.DUELS_LOST.node(), value);
		savingManager.save(file);
	}
	public String getClanName()
	{
		return CommonUtils.chat(file.getString(ClanNodes.CLAN_NAME.node()));
	}
	public String getClanNameRaw()
	{
		return ChatColor.stripColor(getClanName());
	}
	public void setClanName(String name)
	{
		//change name for allies
		for(ClanData ally:getClanAllies())
			((EpicClanData) ally).removeClanAlly(this);
		for(ClanData truce:getClanTruce())
			((EpicClanData) truce).removeClanTruce(this);
		file.set(ClanNodes.CLAN_NAME.node(), name);
		for(ClanData ally:getClanAllies())
			((EpicClanData) ally).addClanAlly(this);
		for(ClanData truce:getClanTruce())
			((EpicClanData) truce).addClanTruce(this);
		//update clan tag
		setClanTag(ClanTagManager.generateTag(getClanNameRaw()));
		savingManager.save(file);
	}
	public void setClanTag(String tag)
	{
		file.set(ClanNodes.CLAN_TAG.node(), tag);
		savingManager.save(file);
	}
	public String getClanTag()
	{
		return CommonUtils.chat(file.getString(ClanNodes.CLAN_TAG.node()));
	}
	public String getClanTagRaw()
	{
		return ChatColor.stripColor(getClanTag());
	}
	public String getClanLeader()
	{
		return file.getString(ClanNodes.CLAN_LEADER.node());
	}
	public void setClanLeader(String name)
	{
		file.set(ClanNodes.CLAN_LEADER.node(), name);
		savingManager.save(file);
	}
	public Map<String, PlayerData> getMembersData()
	{
		return this.memberData;
	}
	public PlayerData getMemberData(String member)
	{
		if(getClanMembersName().contains(member)&&memberData.containsKey(member))
			return memberData.get(member);
		return null;
	}
	@SuppressWarnings("deprecation")
	public List<OfflinePlayer> getClanMembers()
	{
		List<OfflinePlayer> players=new ArrayList<OfflinePlayer>();
		for(String s:file.getStringList(ClanNodes.CLAN_MEMBERS.node()))
				if(Bukkit.getOfflinePlayer(s)!=null)
					players.add(Bukkit.getOfflinePlayer(s));
		return players;
	}
	public List<String> getClanMembersName()
	{
		return file.getStringList(ClanNodes.CLAN_MEMBERS.node());
	}
	public void setClanMembers(List<String> members)
	{
		//remove any member's data that was removed
		for(String oldMember:memberData.keySet())
		{
			if(!members.contains(oldMember)) //new member doesnt have this member
				((ClanPlayersData) memberData.get(oldMember)).remove();
		}
		file.set(ClanNodes.CLAN_MEMBERS.node(), members);
		//update member's data
		memberData.clear();
		for(String member:members)
			memberData.put(member, new ClanPlayersData(member, file));
		savingManager.save(file);
	}
	public void addClanMember(String name)
	{
		List<String> members = getClanMembersName();
		members.add(name);
		setClanMembers(members);
	}
	public void removeClanMember(String name)
	{
		if(getClanLeader().equals(name)) //leader left the clan
		{
			topManager.removeClan(this);
			clanManager.deleteClan(getClanLeader());
			return;
		}
		List<String> members = getClanMembersName();
		members.remove(name);
		setClanMembers(members);
	}
	public List<ClanData> getClanAllies()
	{
		List<ClanData> allies=new ArrayList<ClanData>();
		for(String name:file.getStringList(ClanNodes.CLAN_ALLIES.node()))
			if(clanManager.isClanName(name))
				allies.add(clanManager.getClanData(name));
		return allies;
	}
	void setClanAllies(List<ClanData> allies)
	{
		List<String> names=new ArrayList<>();
		for(ClanData data:allies)
			if(!data.getClanNameRaw().equals(this.getClanNameRaw()))
				names.add(data.getClanNameRaw());
		file.set(ClanNodes.CLAN_ALLIES.node(), names);
		savingManager.save(file);
	}
	public void addClanAlly(ClanData ally)
	{
		List<ClanData> allies = getClanAllies();
		allies.add(ally);
		setClanAllies(allies);
	}
	public void removeClanAlly(ClanData ally)
	{
		List<ClanData> allies = getClanAllies();
		allies.remove(ally);
		setClanAllies(allies);
	}
	public List<ClanData> getClanTruce()
	{
		List<ClanData> truces=new ArrayList<ClanData>();
		for(String name:file.getStringList(ClanNodes.CLAN_TRUCES.node()))
			if(clanManager.isClanName(name))
				truces.add(clanManager.getClanData(name));
		return truces;
	}
	void setClanTruce(List<ClanData> truces)
	{
		List<String> names=new ArrayList<>();
		for(ClanData data:truces)
			if(!data.getClanNameRaw().equals(this.getClanNameRaw()))
				names.add(data.getClanNameRaw());
		file.set(ClanNodes.CLAN_TRUCES.node(), names);
		savingManager.save(file);
	}
	public void addClanTruce(ClanData truce)
	{
		List<ClanData> truces = getClanTruce();
		truces.add(truce);
		setClanTruce(truces);
	}
	public void removeClanTruce(ClanData truce)
	{
		List<ClanData> truces = getClanTruce();
		truces.remove(truce);
		setClanTruce(truces);
	}
	public long getClanBalance()
	{
		return file.getLong(ClanNodes.CLAN_BALANCE.node());
	}
	public void setClanBalance(long balance)
	{
		file.set(ClanNodes.CLAN_BALANCE.node(),balance);
		savingManager.save(file);
	}
	public VaultsData getVaults()
	{
		return this.vaults;
	}
	public BaseData getBases()
	{
		return this.bases;
	}
	public long getClanRespect()
	{
		long respect=0;
		int totalKills=0;
		int totalDeaths=0;
		int totalPoints=0;
		for(PlayerData data:memberData.values())
		{
			totalKills=totalKills+data.getKills();
			totalDeaths=totalDeaths+data.getDeaths();
			totalPoints=totalPoints+data.getPoints();
		}
		int totalDuels=getWonDuels()+getLostDuels();
		int wonDuels=getWonDuels();
		int lostDuels=getLostDuels();
		long balance = getClanBalance();
		String formula=handle.getFormulas().getFile().getString("clan-respect");
		formula=formula.replace("%kills%", String.valueOf(totalKills));
		formula=formula.replace("%deaths%", String.valueOf(totalDeaths));
		formula=formula.replace("%points%", String.valueOf(totalPoints));
		formula=formula.replace("%duels%", String.valueOf(totalDuels));
		formula=formula.replace("%duelswon%", String.valueOf(wonDuels));
		formula=formula.replace("%duelslost%", String.valueOf(lostDuels));
		formula=formula.replace("%balance%", String.valueOf(balance));
		respect=(long) CommonUtils.evaluateString(formula);
		respect=respect+file.getLong(ClanNodes.RESPECT_ADITION.node());
		respect=respect-file.getLong(ClanNodes.RESPECT_SUBTRACTION.node());
		return respect;
	}
	public long getClanPoints()
	{
		long totalPoints=0;
		for(PlayerData data:memberData.values())
		{
			totalPoints=totalPoints+data.getPoints();
		}
		return totalPoints;
	}
	public void save()
	{
		savingManager.save(file);
	}
	public void saveFast()
	{
		savingManager.saveFast(file);
	}
	public FileConfiguration getFile()
	{
		return this.file;
	}
	public String replacePlaceholders(String s)
	{
		s=s.replace("%clan%", getClanName());
		s=s.replace("%leader%", getClanLeader());
		if(getClanRespect()<0)
			s=s.replace("%respect%", "-"+CommonUtils.getDecimalFormatted(Math.abs(getClanRespect())));
		else
			s=s.replace("%respect%", CommonUtils.getDecimalFormatted(getClanRespect()));
		s=s.replace("%pointst%", CommonUtils.getDecimalFormatted(getClanPoints()));
		s=s.replace("%balance%", CommonUtils.getDecimalFormatted(getClanBalance()));
		s=s.replace("%duels%", CommonUtils.getDecimalFormatted(getWonDuels()+getLostDuels()));
		s=s.replace("%duelswon%", CommonUtils.getDecimalFormatted(getWonDuels()));
		s=s.replace("%duelslost%", CommonUtils.getDecimalFormatted(getLostDuels()));
		s=s.replace("%membercount%", CommonUtils.getDecimalFormatted(getClanMembers().size()));
		return s;
	}
	public List<String> replacePlaceholders(List<String> messages)
	{
		List<String> result=new ArrayList<>();
		for(String s:messages)
		{
			s=s.replace("%clan%", getClanName());
			s=s.replace("%leader%", getClanLeader());
			if(getClanRespect()<0)
				s=s.replace("%respect%", "-"+CommonUtils.getDecimalFormatted(Math.abs(getClanRespect())));
			else
				s=s.replace("%respect%", CommonUtils.getDecimalFormatted(getClanRespect()));
			s=s.replace("%points%", CommonUtils.getDecimalFormatted(getClanPoints()));
			s=s.replace("%balance%", CommonUtils.getDecimalFormatted(getClanBalance()));
			s=s.replace("%totalpoints%", CommonUtils.getDecimalFormatted(getClanPoints()));
			s=s.replace("%duels%", CommonUtils.getDecimalFormatted(getWonDuels()+getLostDuels()));
			s=s.replace("%duelswon%", CommonUtils.getDecimalFormatted(getWonDuels()));
			s=s.replace("%duelslost%", CommonUtils.getDecimalFormatted(getLostDuels()));
			s=s.replace("%membercount%", CommonUtils.getDecimalFormatted(getClanMembers().size()));
			if(s.contains("%members%"))
			{
				String m=s;
				for(String member:getClanMembersName())
					result.add(m.replace("%members%", member));
				continue;
			}
			result.add(s);
		}
		return result;
	}
	public void addRespect(int respect) 
	{
		long old = file.getLong(ClanNodes.RESPECT_ADITION.node());
		file.set(ClanNodes.RESPECT_ADITION.node(), old+respect);
		savingManager.save(file);
	}
	public void removeRespect(long l) 
	{
		long old = file.getLong(ClanNodes.RESPECT_SUBTRACTION.node());
		file.set(ClanNodes.RESPECT_SUBTRACTION.node(), old+l);
		savingManager.save(file);
	}
	@Override
	public EpicClanData getHandle() {
		return this;
	}
	@Override
	public PlayerData getMemberData(OfflinePlayer player) {
		return getMemberData(player.getName());
	}
	@Override
	public void addClanMember(Player player) {
		addClanMember(player.getName());
	}
	@Override
	public void removeClanMember(Player player) {
		removeClanMember(player.getName());
	}
}
