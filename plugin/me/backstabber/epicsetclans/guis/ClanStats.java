package me.backstabber.epicsetclans.guis;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.data.ClanPlayersData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.ClanTopManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.utils.CommonUtils;
import me.backstabber.epicsetclans.utils.materials.EpicMaterials;
import me.backstabber.epicsetclans.utils.materials.UMaterials;

public class ClanStats extends Guiable
{
	public ClanStats(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager,ClanTopManager topManager)
	{
		super(plugin, clanManager, duelManager, topManager);
		guiName="stats";
		file=plugin.getFiles().get(guiName);
	}
	@Override
	public Inventory getMainInventory(EpicClanData clan,Player player) 
	{
		Inventory inv=Bukkit.createInventory(null, 9*file.getInt("maingui.size"),CommonUtils.chat(file.getString("maingui.name")));
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(file.isSet("maingui.background"))
				{
					ItemStack background = CommonUtils.getItemFromNode(file.getFile().getConfigurationSection("maingui.background"));
					background=CommonUtils.setStringTag(background, "gui:"+guiName+":background");
					for(int i=0;i<9*file.getInt("maingui.size");i++)
						inv.setItem(i, background);
				}
				if(file.isSet("maingui.clandata"))
				{
					String iname=clan.replacePlaceholders(file.getString("maingui.clandata.name"));
					String itype=clan.replacePlaceholders(file.getString("maingui.clandata.type"));
					List<String> ilore=clan.replacePlaceholders(file.getStringList("maingui.clandata.lore"));
					int slot=file.getInt("maingui.clandata.slot");
					ItemStack clanData=CommonUtils.getCustomItem(itype, iname, ilore);
					clanData=CommonUtils.setStringTag(clanData, "gui:"+guiName+":clandata");
					inv.setItem(slot, clanData);
				}

				if(file.isSet("maingui.playerdata"))
				{
					Iterator<String> members = clan.getClanMembersName().iterator();
					for(int slot:getSlots(9*file.getInt("maingui.size"), clan.getClanMembers().size(), true))
					{
						if(members.hasNext())
						{
							String name=members.next();
							ClanPlayersData data = (ClanPlayersData) clan.getMemberData(name);
							String iname=data.replacePlaceholders(file.getString("maingui.playerdata.name"));
							String itype=data.replacePlaceholders(file.getString("maingui.playerdata.type"));
							List<String> ilore=data.replacePlaceholders(file.getStringList("maingui.playerdata.lore"));
							ItemStack playerData=CommonUtils.getCustomItem(itype, iname, ilore);
							playerData=CommonUtils.setStringTag(playerData, "gui:"+guiName+":playerdata:"+name);
							inv.setItem(slot, playerData);
						}
					}
				}
				player.updateInventory();
			}
		}.runTaskAsynchronously(plugin);
		return inv;
	}

	@Override
	public Inventory getSubInventory(EpicClanData clan,ClanPlayersData data,Player player) 
	{
		return null;
	}

	@Override
	public void inventoryClickHandle(InventoryClickEvent event) 
	{
		//test to see if the clicked item was part of the gui (Unique way without using inventory name) 
		if(event.getRawSlot()<0)
			return;
		ItemStack item=event.getCurrentItem();
		if(item==null||item.getType().equals(EpicMaterials.valueOf(UMaterials.AIR).getMaterial()))
			return;
		String tag=CommonUtils.getStringTag(item);
		if(tag==null||tag.equals(""))
			return;
		if(!tag.startsWith("gui:"+guiName))
			return;
		event.setCancelled(true);
	}
	@Override
	public void inventoryCloseHandle(InventoryCloseEvent event) 
	{
		
	}
}
