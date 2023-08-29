package me.backstabber.epicsetclans.guis;

import java.util.ArrayList;
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

public class ClanTop extends Guiable
{
	public ClanTop(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager,ClanTopManager topManager)
	{
		super(plugin, clanManager, duelManager, topManager);
		guiName="top";
		file=plugin.getFiles().get(guiName);
	}
	@Override
	public Inventory getMainInventory(EpicClanData clan,Player player) 
	{
		final Inventory inv = Bukkit.createInventory(null, 9 * file.getInt("maingui.size"), CommonUtils.chat(file.getString("maingui.name")));
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if (file.isSet("maingui.background")) {
					ItemStack background = CommonUtils.getItemFromNode(file.getFile().getConfigurationSection("maingui.background"));
					background = CommonUtils.setStringTag(background, "gui:"+guiName+":background");
					
					for(int i = 0 ; i < 9 * file.getInt("maingui.size") ; i++) {
						inv.setItem(i, background);
					}
				}
				
				if (file.isSet("maingui.data")) {
					ItemStack clanData=CommonUtils.getCustomItem(
						file.getString("maingui.data.type"),
						file.getString("maingui.data.name"),
						file.getStringList("maingui.data.lore")
					);
					clanData = CommonUtils.setStringTag(clanData, "gui:"+guiName+":clandata");
					inv.setItem(file.getInt("maingui.data.slot"), clanData);
				}

				if (file.isSet("maingui.clandata")) {
					final StringBuilder builder = new StringBuilder();
					
					EpicClanData data;
					List<String> lore;
					ItemStack playerData;
					
					for (int slot = 10 ; slot <= 16 ; slot++) {
						if (topManager.getClan(slot - 9) == null) continue;
						
						data = (EpicClanData) topManager.getClan(slot - 9);
						lore = new ArrayList<>();
						
						for (final String str : data.replacePlaceholders(file.getStringList("maingui.clandata.lore"))) {
							lore.add(str.replace("%pos%", Integer.toString(slot - 9)));
						}
						
						playerData = CommonUtils.getCustomItem(
							data.replacePlaceholders(file.getString("maingui.clandata.type")).replace("%pos%", Integer.toString(slot - 9)),
							data.replacePlaceholders(file.getString("maingui.clandata.name")).replace("%pos%", Integer.toString(slot - 9)),
							lore
						);
						playerData = plugin.getMonthlyRewardManager().addLore(playerData, slot-9);
						playerData = CommonUtils.setStringTag(playerData, builder.append("gui:")
							.append(guiName)
							.append(":clandata:")
							.append(data.getClanLeader())
							.toString());
						inv.setItem(slot, playerData);
					}
				}
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
		if(tag.split(":").length==4&&tag.split(":")[2].equals("clandata"))
		{
			String clanName=tag.split(":")[3];
			if(clanManager.getClanData(clanName)==null)
				return;
			EpicClanData clan=(EpicClanData) clanManager.getClanData(clanName);
			Inventory stats = plugin.getGuis().get("stats").getMainInventory(clan,(Player) event.getWhoClicked());
			event.getWhoClicked().openInventory(stats);
		}
	}
	@Override
	public void inventoryCloseHandle(InventoryCloseEvent event) 
	{
		
	}
}
