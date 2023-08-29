package me.backstabber.epicsetclans.guis;

import java.util.ArrayList;
import java.util.HashMap;
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
import me.backstabber.epicsetclans.api.events.ClanUpgradeEvent;
import me.backstabber.epicsetclans.api.events.ClanUpgradeEvent.Upgradetype;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.data.ClanPlayersData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.ClanTopManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.clan.ShopCommand;
import me.backstabber.epicsetclans.enums.ClanNodes;
import me.backstabber.epicsetclans.utils.CommonUtils;
import me.backstabber.epicsetclans.utils.materials.EpicMaterials;
import me.backstabber.epicsetclans.utils.materials.UMaterials;

public class ClanShop extends Guiable
{
	public ClanShop(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager,ClanTopManager topManager)
	{
		super(plugin, clanManager, duelManager, topManager);
		guiName="shop";
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
					String itype=clan.replacePlaceholders(file.getString("maingui.clandata.item"));
					List<String> ilore=clan.replacePlaceholders(file.getStringList("maingui.clandata.lore"));
					int slot=file.getInt("maingui.clandata.slot");
					ItemStack clanData=CommonUtils.getCustomItem(itype, iname, ilore);
					clanData=CommonUtils.setStringTag(clanData, "gui:"+guiName+":clandata");
					inv.setItem(slot, clanData);
				}
				for(String key:file.getFile().getKeys(false))
				{
					if(key.equalsIgnoreCase("maingui"))
						continue;
					addData(inv, clan, key);
				}
				player.updateInventory();
			}
		}.runTaskAsynchronously(plugin);
		return inv;
	}

	@Override
	public Inventory getSubInventory(EpicClanData clan, ClanPlayersData data,Player player) 
	{
		return null;
	}

	@Override
	public void inventoryClickHandle(InventoryClickEvent event) 
	{
		//test to see if the clicked item was part of the gui (Unique way without using inventory name) 
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
		if(!clanManager.isInClan(player.getName()))
			return;
		EpicClanData clan=(EpicClanData) clanManager.getClanData(player.getName());
		Map<String, String> placeHolders=new HashMap<>();
		placeHolders.put("%clan%", clan.getClanName());
		placeHolders.put("%player%", player.getName());
		if(tag.split(":").length>2)
		{
			if(tag.split(":")[2].equals("background")||tag.split(":")[2].equals("clandata"))
				return;
			if(tag.split(":")[2].equals("data")&&tag.split(":").length==4)
			{
				String dataNode=tag.split(":")[3];
				if(file.isSet(dataNode))
				{
					int slot=event.getRawSlot();
					int row=(int) Math.floor(slot/9D);
					int number=(int) (slot-(row*9));
					int amount=file.getInt(dataNode+".upgrade."+number+".amount");
					Map<String, Integer> temp=new HashMap<>();
					temp.put("%amount%", amount);
					if(replacePlaceholders("%status%", clan, ClanNodes.valueOf(dataNode), temp).equals("&aUnlocked"))
						return;
					int respect=file.getInt(dataNode+".upgrade."+number+".respect");
					int points=file.getInt(dataNode+".upgrade."+number+".points");
					int bal=file.getInt(dataNode+".upgrade."+number+".bal");
			    	double balance = clan.getClanBalance();
			    	if(balance>=bal&&points<=clan.getMemberData(player.getName()).getPoints()&&respect<=clan.getClanRespect())
			    	{
			    		ClanUpgradeEvent upgradeEvent=new ClanUpgradeEvent(player, clan, Upgradetype.valueOf(dataNode), amount);
			    		Bukkit.getPluginManager().callEvent(upgradeEvent);
			    		if(upgradeEvent.isCancelled())
			    			return;
			    		clan.getMemberData(player.getName()).setPoints(clan.getMemberData(player.getName()).getPoints()-points);
			    		clan.setClanBalance((long) (balance-bal));
			    		clan.setFromNode(ClanNodes.valueOf(dataNode), amount);
			    		new ShopCommand(plugin, clanManager, duelManager).sendMessage(player, "sucess", placeHolders);
			    		player.openInventory(getMainInventory(clan,player));
			    		return;
			    	}
			    	else
			    	{
			    		String req="";
			    		if(respect>clan.getClanRespect())
			    			req="clan respect";
			    		else if(points>clan.getMemberData(player.getName()).getPoints())
			    			req="clan points";
			    		else if(balance<bal)
			    			req="balance";
			    		placeHolders.put("%cost%", req);
			    		new ShopCommand(plugin, clanManager, duelManager).sendMessage(player, "fail", placeHolders);;
			    		return;
			    	}
				}
			}
		}
	}
	
	private Inventory addData(Inventory inv, EpicClanData clan,String type) 
	{
		int row=file.getInt(type+".row");
		for(int i=0;i<9;i++)
		{
			if(file.isSet(type+".upgrade."+i))
			{
				Map<String, Integer> placeHolders=new HashMap<>();
				for(String key:file.getFile().getConfigurationSection(type+".upgrade."+i).getKeys(false))
					placeHolders.put("%"+key+"%", file.getInt(type+".upgrade."+i+"."+key));
				String iType=replacePlaceholders(file.getString(type+".item"),clan,ClanNodes.valueOf(type),placeHolders);
				String iName=replacePlaceholders(file.getString(type+".name"),clan,ClanNodes.valueOf(type),placeHolders);
				List<String> iLore=replacePlaceholders(file.getStringList(type+".lore"), clan,ClanNodes.valueOf(type), placeHolders);
				ItemStack item=CommonUtils.getCustomItem(iType, iName, iLore);
				item.setAmount(file.getInt(type+".upgrade."+i+".amount"));
				item=CommonUtils.setStringTag(item, "gui:"+guiName+":data:"+type);
				inv.setItem(i+(row*9), item);
			}
		}
		return inv;
	}
	private String replacePlaceholders(String s,EpicClanData clan,ClanNodes node,Map<String, Integer> placeHolders) 
	{
		for(String key:placeHolders.keySet())
			s=s.replace(key, CommonUtils.getDecimalFormatted(placeHolders.get(key)));
		int amount=placeHolders.get("%amount%");
		if(((int)clan.getFromNode(node))>=amount)
			s=s.replace("%status%", "&aUnlocked");
		else
			s=s.replace("%status%", "&cLocked");
		s=clan.replacePlaceholders(s);
		return s;
	}
	private List<String> replacePlaceholders(List<String> message,EpicClanData clan,ClanNodes node,Map<String, Integer> placeHolders) 
	{
		List<String> result=new ArrayList<>();
		for(String s: message)
		{
			for(String key:placeHolders.keySet())
				s=s.replace(key, CommonUtils.getDecimalFormatted(placeHolders.get(key)));
			int amount=placeHolders.get("%amount%");
			if(((int)clan.getFromNode(node))>=amount)
				s=s.replace("%status%", "&aUnlocked");
			else
				s=s.replace("%status%", "&cLocked");
			s=clan.replacePlaceholders(s);
			result.add(s);
		}
		return result;
	}
	@Override
	public void inventoryCloseHandle(InventoryCloseEvent event) 
	{

	}

}
