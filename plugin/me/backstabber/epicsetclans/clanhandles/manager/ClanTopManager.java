package me.backstabber.epicsetclans.clanhandles.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.scheduler.BukkitRunnable;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.data.ClanData;
import me.backstabber.epicsetclans.api.manager.TopManager;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.saving.SavingManager;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class ClanTopManager implements TopManager
{
	private EpicSetClans handle;
	private SavingManager savingManager;
	private Map<Integer, ClanData> sortedClans=new HashMap<>();
	private Map<Integer,ClanData> forcedClans=new HashMap<>();

	public ClanTopManager(final EpicSetClans handle, final SavingManager savingManager) {
		this.handle = handle;
		this.savingManager = savingManager;
	}

	public void setup()
	{
		int timer=(int) CommonUtils.evaluateString(handle.getSettings().getFile().getString("settings.clan-top-update-interval"));
		new BukkitRunnable() 
		{
			@Override
			public void run() 
			{
				sortClans();
			}
		}.runTaskTimer(handle, 20, 20*timer);
	}
	public ClanData getClan(int position)
	{
		if(sortedClans.containsKey(position))
			return sortedClans.get(position);
		return null;
	}
	public Map<Integer, ClanData> getSortedClans()
	{
		return this.sortedClans;
	}
	public void setPosition(ClanData clan,int postion)
	{
		forcedClans.put(postion, clan);
	}
	public void removeClan(ClanData clanData) 
	{
		sortedClans.remove(getPosition(clanData.getHandle()));
	}
	public void sortClans() 
	{
		Collection<EpicClanData> clans=savingManager.getAllClans();
		EpicClanData[] unSorted=new EpicClanData[clans.size()];
		int index=0;
		for(EpicClanData clan:clans)
		{
			unSorted[index]=clan;
			index++;
		}
		new BukkitRunnable() 
		{
			@Override
			public void run() 
			{
				int n = unSorted.length;
		        EpicClanData temp;  
		        for(int i=0; i < n; i++)
		        {  
		        	for(int j=1; j < (n-i); j++)
		        	{  
		        		if(unSorted[j-1].getClanRespect() < unSorted[j].getClanRespect())
		        		{  
		                     //swap clans
		                     temp = unSorted[j-1];  
		                     unSorted[j-1] = unSorted[j];  
		                     unSorted[j] = temp;  
                         }  
		                          
	                 }  
		        }
		        //clear clans & add sorted clans
		        List<ClanData> sorted=new ArrayList<>();
		        for(int i=0;i<unSorted.length;i++)
		        {
		        	sorted.add(unSorted[i]);
		        }
		        //add the forced positions
		        for(int i:forcedClans.keySet())
		        	sorted.add(i, forcedClans.get(i));
		        //set the new sortedClans
		        for(int i=0;i<sorted.size();i++)
		        	sortedClans.put(i+1, sorted.get(i));
			}
		}.runTaskAsynchronously(handle);
	}
	public int getPosition(EpicClanData clan) 
	{
		for(int position:this.sortedClans.keySet())
			if(this.sortedClans.get(position).equals(clan))
				return position;
		return 0;
	}
}
