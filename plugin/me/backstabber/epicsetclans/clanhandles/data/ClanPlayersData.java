package me.backstabber.epicsetclans.clanhandles.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import me.backstabber.epicsetclans.api.data.PlayerData;
import me.backstabber.epicsetclans.enums.ClanNodes;
import me.backstabber.epicsetclans.enums.ClanPlayerNodes;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class ClanPlayersData implements PlayerData
{
	private FileConfiguration file;
	private String player;
	ClanPlayersData(String player,FileConfiguration file)
	{
		this.file=file;
		this.player=player;
		//check if default files are needed
		for(ClanPlayerNodes node:ClanPlayerNodes.values())
		{
			if(!file.isSet(node.node(player)))
				file.set(node.node(player), node.value());
		}
	}
	public void remove()
	{
		file.set(player, null);
	}
	public String getName()
	{
		return this.player;
	}
	public int getKills()
	{
		return file.getInt(ClanPlayerNodes.CLAN_KILLS.node(player));
	}
	public void setKills(int value)
	{
		file.set(ClanPlayerNodes.CLAN_KILLS.node(player), value);
	}
	public int getDeaths()
	{
		return file.getInt(ClanPlayerNodes.CLAN_DEATHS.node(player));
	}
	public void setDeaths(int value)
	{
		file.set(ClanPlayerNodes.CLAN_DEATHS.node(player), value);
	}
	public int getPoints()
	{
		return file.getInt(ClanPlayerNodes.CLAN_POINTS.node(player));
	}
	public void setPoints(int value)
	{
		file.set(ClanPlayerNodes.CLAN_POINTS.node(player), value);
	}
	public int getDuelsPlayed()
	{
		return file.getInt(ClanPlayerNodes.DUELS_PLAYED.node(player));
	}
	public void setDuelsPlayed(int value)
	{
		file.set(ClanPlayerNodes.DUELS_PLAYED.node(player), value);
	}
	public int getDuelsWon()
	{
		return file.getInt(ClanPlayerNodes.DUELS_WON.node(player));
	}
	public void setDuelsWon(int value)
	{
		file.set(ClanPlayerNodes.DUELS_WON.node(player), value);
	}
	public String getTag()
	{
		return file.getString(ClanPlayerNodes.CLAN_TAG.node(player));
	}
	public void setTag(String value)
	{
		file.set(ClanPlayerNodes.CLAN_TAG.node(player), value);
	}
	public String getInfo()
	{
		return file.getString(ClanPlayerNodes.CLAN_INFO.node(player));
	}
	public void setInfo(String value)
	{
		file.set(ClanPlayerNodes.CLAN_INFO.node(player), value);
	}
	public List<String> getPermissions()
	{
		return file.getStringList(ClanPlayerNodes.CLAN_PERMS.node(player));
	}
	public void setPermissions(List<String> perms)
	{
		file.set(ClanPlayerNodes.CLAN_PERMS.node(player), perms);
	}
	public void addPermission(String perm)
	{
		List<String> perms = getPermissions();
		perms.add(perm);
		setPermissions(perms);
	}
	public void removePermission(String perm)
	{
		List<String> perms = getPermissions();
		perms.remove(perm);
		setPermissions(perms);
	}
	public boolean hasPermission(String permission)
	{
		return(getPermissions().contains(permission));
	}
	public boolean isLeader()
	{
		return file.getString(ClanNodes.CLAN_LEADER.node()).equals(player);
	}
	public List<String> replacePlaceholders(List<String> messages)
	{
		List<String> result=new ArrayList<>();
		for(String s:messages)
		{
			s=s.replace("%player%", this.player);
			s=s.replace("%kills%", CommonUtils.getDecimalFormatted(getKills()));
			s=s.replace("%deaths%", CommonUtils.getDecimalFormatted(getDeaths()));
			s=s.replace("%points%", CommonUtils.getDecimalFormatted(getPoints()));
			s=s.replace("%duels%", CommonUtils.getDecimalFormatted(getDuelsPlayed()));
			s=s.replace("%duelswon%", CommonUtils.getDecimalFormatted(getDuelsWon()));
			s=s.replace("%duelslost%", CommonUtils.getDecimalFormatted(getDuelsPlayed()-getDuelsWon()));
			s=s.replace("%tag%", getTag());
			s=s.replace("%comment%", getInfo());
			s=s.replace("%comment%", getInfo());
			if(hasPermission("invite")||isLeader())
				s=s.replace("%invite%", "&aallow");
			else
				s=s.replace("%invite%", "&cdeny");
			if(hasPermission("kick")||isLeader())
				s=s.replace("%kick%", "&aallow");
			else
				s=s.replace("%kick%", "&cdeny");
			if(hasPermission("tag")||isLeader())
				s=s.replace("%taging%", "&aallow");
			else
				s=s.replace("%taging%", "&cdeny");
			if(hasPermission("ally")||isLeader())
				s=s.replace("%allying%", "&aallow");
			else
				s=s.replace("%allying%", "&cdeny");
			if(hasPermission("truce")||isLeader())
				s=s.replace("%trucing%", "&aallow");
			else
				s=s.replace("%trucing%", "&cdeny");
			if(hasPermission("remove")||isLeader())
				s=s.replace("%remove%", "&aallow");
			else
				s=s.replace("%remove%", "&cdeny");
			if(hasPermission("duel")||isLeader())
				s=s.replace("%duel%", "&aallow");
			else
				s=s.replace("%duel%", "&cdeny");
			if(hasPermission("setbase")||isLeader())
				s=s.replace("%setbase%", "&aallow");
			else
				s=s.replace("%setbase%", "&cdeny");
			if(hasPermission("delbase")||isLeader())
				s=s.replace("%delbase%", "&aallow");
			else
				s=s.replace("%delbase%", "&cdeny");
			if(hasPermission("vault")||isLeader())
				s=s.replace("%vault%", "&aallow");
			else
				s=s.replace("%vault%", "&cdeny");
			if(hasPermission("bank")||isLeader())
				s=s.replace("%banking%", "&aallow");
			else
				s=s.replace("%banking%", "&cdeny");
			result.add(s);
		}
		return result;
	}
	public String replacePlaceholders(String s)
	{
		s=s.replace("%player%", this.player);
		s=s.replace("%kills%", CommonUtils.getDecimalFormatted(getKills()));
		s=s.replace("%deaths%", CommonUtils.getDecimalFormatted(getDeaths()));
		s=s.replace("%points%", CommonUtils.getDecimalFormatted(getPoints()));
		s=s.replace("%duels%", CommonUtils.getDecimalFormatted(getDuelsPlayed()));
		s=s.replace("%duelswon%", CommonUtils.getDecimalFormatted(getDuelsWon()));
		s=s.replace("%duelslost%", CommonUtils.getDecimalFormatted(getDuelsPlayed()-getDuelsWon()));
		s=s.replace("%tag%", getTag());
		s=s.replace("%comment%", getInfo());
		s=s.replace("%comment%", getInfo());
		if(hasPermission("invite")||isLeader())
			s=s.replace("%invite%", "&aallow");
		else
			s=s.replace("%invite%", "&cdeny");
		if(hasPermission("kick")||isLeader())
			s=s.replace("%kick%", "&aallow");
		else
			s=s.replace("%kick%", "&cdeny");
		if(hasPermission("tag")||isLeader())
			s=s.replace("%taging%", "&aallow");
		else
			s=s.replace("%taging%", "&cdeny");
		if(hasPermission("ally")||isLeader())
			s=s.replace("%allying%", "&aallow");
		else
			s=s.replace("%allying%", "&cdeny");
		if(hasPermission("truce")||isLeader())
			s=s.replace("%trucing%", "&aallow");
		else
			s=s.replace("%trucing%", "&cdeny");
		if(hasPermission("remove")||isLeader())
			s=s.replace("%remove%", "&aallow");
		else
			s=s.replace("%remove%", "&cdeny");
		if(hasPermission("duel")||isLeader())
			s=s.replace("%duel%", "&aallow");
		else
			s=s.replace("%duel%", "&cdeny");
		if(hasPermission("setbase")||isLeader())
			s=s.replace("%setbase%", "&aallow");
		else
			s=s.replace("%setbase%", "&cdeny");
		if(hasPermission("delbase")||isLeader())
			s=s.replace("%delbase%", "&aallow");
		else
			s=s.replace("%delbase%", "&cdeny");
		if(hasPermission("vault")||isLeader())
			s=s.replace("%vault%", "&aallow");
		else
			s=s.replace("%vault%", "&cdeny");
		if(hasPermission("bank")||isLeader())
			s=s.replace("%banking%", "&aallow");
		else
			s=s.replace("%banking%", "&cdeny");
		return s;
	}
	public String getComment()
	{
		return file.getString(ClanPlayerNodes.CLAN_INFO.node(player));
	}
	public void setComment(String value)
	{
		file.set(ClanPlayerNodes.CLAN_INFO.node(player), value);
	}
}
