package me.backstabber.epicsetclans.enums;

import java.util.ArrayList;

public enum ClanPlayerNodes 
{
	CLAN_KILLS("kills",0),
	CLAN_DEATHS("deaths",0),
	CLAN_POINTS("points",0),
	DUELS_PLAYED("duels",0),
	DUELS_WON("duelswon",0),
	CLAN_TAG("tag","&aMember"),
	CLAN_INFO("info","Member of a clan"),
	CLAN_PERMS("perms",new ArrayList<>());
	private String node;
	private Object value;
	ClanPlayerNodes(String node,Object value)
	{
		this.node=node;
		this.value=value;
	}
	public String node(String player)
	{
		return player+"."+this.node;
	}
	public String node()
	{
		return this.node;
	}
	public Object value()
	{
		return this.value;
	}
}
