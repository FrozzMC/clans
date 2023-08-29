package me.backstabber.epicsetclans.api.manager;

import org.bukkit.entity.Player;


import me.backstabber.epicsetclans.api.data.ClanData;


public interface ClanManager 
{
	public boolean areAllies(ClanData alpha,ClanData bravo);
	public boolean areTruces(ClanData alpha,ClanData bravo);
	public void removeAllies(ClanData alpha,ClanData bravo);
	public void removeTruce(ClanData alpha,ClanData bravo);
	public void makeAllies(ClanData alpha,ClanData bravo);
	public void makeTruces(ClanData alpha,ClanData bravo);
	public ClanData createNewClan(Player leader,String clanName,boolean bypassRequirements);
	public boolean isLeader(String player);
	public boolean isInClan(String player);
	public boolean isLeader(Player player);
	public boolean isInClan(Player player);
	public boolean isClanName(String clanName);
	public ClanData getClanData(String name);
	public ClanData getClanData(Player player);
	public void deleteClan(ClanData clan);
	public void makeLeader(String member);
	public void makeLeader(Player player);
	public boolean isSpying(Player player);
}
