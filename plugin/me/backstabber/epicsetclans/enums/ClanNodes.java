package me.backstabber.epicsetclans.enums;

import java.util.ArrayList;

public enum ClanNodes 
{
	UPGRADE_HOME("maxhomes",1),
	UPGRADE_MEMBERS("maxmembers",2),
	UPGRADE_VAULTS("maxvaults",1),
	UPGRADE_DUELS("maxduels",2),
	UPGRADE_ALLIES("maxallies",1),
	
	DUELS_WON("wonduels",0),
	DUELS_LOST("lostduels",0),

	CLAN_NAME("name",""),
	CLAN_NAME_RAW("nameraw",""),
	CLAN_LEADER("leader",""),
	CLAN_MEMBERS("members",new ArrayList<>()),
	CLAN_ALLIES("ally",new ArrayList<>()),
	CLAN_TRUCES("truce",new ArrayList<>()),
	CLAN_BALANCE("balance",0),
	
	RESPECT_ADITION("respectadd",0),
	RESPECT_SUBTRACTION("respectsub",0),
	
	CLAN_VAULT("vaults",""),
	CLAN_BASES("base",""),
	
	RENAME_TIME("renametime",0D),
	
	CLAN_TAG("tag", "");
	
	private String node;
	private Object value;
	ClanNodes(String node, Object value)
	{
		this.node=node;
		this.value=value;
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
