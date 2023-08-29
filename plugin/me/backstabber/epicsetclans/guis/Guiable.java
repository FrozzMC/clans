package me.backstabber.epicsetclans.guis;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;


import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.data.ClanPlayersData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.ClanTopManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.clanhandles.manager.YMLManager;

public abstract class Guiable 
{
	protected EpicSetClans plugin;
	protected EpicClanManager clanManager;
	protected ClanDuelManager duelManager;
	protected ClanTopManager topManager;
	
	protected YMLManager file;
	protected String guiName;
	public abstract  Inventory getMainInventory(EpicClanData clan,Player player);
	public abstract  Inventory getSubInventory(EpicClanData clan,ClanPlayersData data,Player player);
	public abstract void inventoryClickHandle(InventoryClickEvent event);
	public abstract void inventoryCloseHandle(InventoryCloseEvent event);
	public Guiable(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager,ClanTopManager topManager)
	{
		this.plugin=plugin;
		this.clanManager=clanManager;
		this.duelManager=duelManager;
		this.topManager=topManager;
	}
	protected List<Integer> getSlots(int invSize,int count,boolean skipFirstRow)
	{
		List<Integer> slots=new ArrayList<Integer>();
		int prevSlot=-1;
		for(int i=0;i<count;i++)
		{
			if(prevSlot==-1)
			{
				prevSlot=4;
				if(skipFirstRow)
					prevSlot+=9;
				if(prevSlot<invSize)
					slots.add(prevSlot);
				continue;
			}
			int test=prevSlot;
			while(test>9)
				test=test-9;
			if(test==7)
				prevSlot=prevSlot+6;
			else if(test>=4)
				prevSlot=prevSlot-2*(test-4)-1;
			else if(test<4)
				prevSlot=prevSlot+2*(4-test);
			if(prevSlot<invSize)
				slots.add(prevSlot);
		}
		return slots;
	}
}
