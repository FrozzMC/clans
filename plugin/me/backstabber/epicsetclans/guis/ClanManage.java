package me.backstabber.epicsetclans.guis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import me.backstabber.epicsetclans.commands.subcommands.clan.MakeLeaderCommand;
import me.backstabber.epicsetclans.utils.CommonUtils;
import me.backstabber.epicsetclans.utils.materials.EpicMaterials;
import me.backstabber.epicsetclans.utils.materials.UMaterials;

public class ClanManage extends Guiable
{
	public ClanManage(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager,ClanTopManager topManager)
	{
		super(plugin, clanManager, duelManager, topManager);
		guiName="manage";
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
		String invName=data.replacePlaceholders(file.getString("subgui.name"));
		Inventory inv=Bukkit.createInventory(null, 9*file.getInt("subgui.size"),CommonUtils.chat(invName));
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(file.isSet("subgui.background"))
				{
					String iname=data.replacePlaceholders(file.getString("subgui.background.name"));
					String itype=data.replacePlaceholders(file.getString("subgui.background.type"));
					List<String> ilore=data.replacePlaceholders(file.getStringList("subgui.background.lore"));
					ItemStack background = CommonUtils.getCustomItem(itype, iname, ilore);
					background=CommonUtils.setStringTag(background, "gui:"+guiName+":background");
					for(int i=0;i<9*file.getInt("subgui.size");i++)
						inv.setItem(i, background);
				}
				for(int i=0;i<9*file.getInt("subgui.size");i++)
				{
					if(file.isSet("subgui.items."+i))
					{
						String iname=data.replacePlaceholders(file.getString("subgui.items."+i+".name"));
						String itype=data.replacePlaceholders(file.getString("subgui.items."+i+".item"));
						List<String> ilore=data.replacePlaceholders(file.getStringList("subgui.items."+i+".lore"));
						ItemStack item = CommonUtils.getCustomItem(itype, iname, ilore);
						item=CommonUtils.setStringTag(item, "gui:"+guiName+":item:"+data.getName());
						inv.setItem(i, item);
					}
				}
				player.updateInventory();
			}
		}.runTaskAsynchronously(plugin); 
		return inv;
	}

	@Override
	public void inventoryClickHandle(InventoryClickEvent event) 
	{
		Player player=(Player) event.getWhoClicked();
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
		if(!clanManager.isLeader(player.getName()))
			return;
		EpicClanData clan=(EpicClanData) clanManager.getClanData(player.getName());
		Map<String, String> placeHolders=new HashMap<>();
		placeHolders.put("%clan%", clan.getClanName());
		placeHolders.put("%player%", player.getName());
		if(tag.split(":").length>2)
		{
			if(tag.split(":")[2].equals("background")||tag.split(":")[2].equals("clandata"))
				return;
			if(tag.split(":")[2].equals("playerdata")&&tag.split(":").length==4)
			{
				String playerName=tag.split(":")[3];
				if(clan.getMemberData(playerName)!=null)
				{
					ClanPlayersData data = (ClanPlayersData) clan.getMemberData(playerName);
					Inventory sub=getSubInventory(clan, data,player);
					player.openInventory(sub);
					return;
				}
			}
			if(tag.split(":")[2].equals("item")&&tag.split(":").length==4)
			{
				String playerName=tag.split(":")[3];
				if(clan.getMemberData(playerName)==null)
					return;
				ClanPlayersData data = (ClanPlayersData) clan.getMemberData(playerName);
				placeHolders.put("%otherplayer%", data.getName());
				int slot=event.getRawSlot();
				if(file.isSet("subgui.items."+slot))
				{
					String permission=file.getString("subgui.items."+slot+".perm");
					if(permission.equalsIgnoreCase("leader"))
					{
						if(!clanManager.isLeader(data.getName()))
						{
							clanManager.makeLeader(data.getName());
							new MakeLeaderCommand(plugin, clanManager, duelManager).sendMessage(player, "sent", placeHolders);
							player.closeInventory();
							if(Bukkit.getPlayerExact(data.getName())!=null)
								new MakeLeaderCommand(plugin, clanManager, duelManager).sendMessage(Bukkit.getPlayerExact(data.getName()), "toplayer", placeHolders);
							return;
						}
						else
						{
							new MakeLeaderCommand(plugin, clanManager, duelManager).sendMessage(player, "alreadyleader", placeHolders);
							return;
						}
					}
					else
					{
						if(data.hasPermission(permission))
							data.removePermission(permission);
						else
							data.addPermission(permission);
						player.openInventory(getSubInventory(clan, data,player));
					}
					return;
				}
			}
		}
	}
	@Override
	public void inventoryCloseHandle(InventoryCloseEvent event) 
	{
		
	}
}
