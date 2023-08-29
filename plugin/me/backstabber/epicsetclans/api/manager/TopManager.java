package me.backstabber.epicsetclans.api.manager;

import java.util.Map;


import me.backstabber.epicsetclans.api.data.ClanData;


public interface TopManager 
{
	public ClanData getClan(int position);
	public Map<Integer, ClanData> getSortedClans();
	public void setPosition(ClanData clan,int postion);
}
