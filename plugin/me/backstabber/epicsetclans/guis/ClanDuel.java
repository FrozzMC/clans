package me.backstabber.epicsetclans.guis;

import java.util.ArrayList;
import java.util.Collection;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.data.ClanDuelData;
import me.backstabber.epicsetclans.clanhandles.data.ClanPlayersData;
import me.backstabber.epicsetclans.clanhandles.data.DuelArenaData;
import me.backstabber.epicsetclans.clanhandles.data.DuelArenaData.ArenaState;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.ClanTopManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.clanhandles.data.KitsData;
import me.backstabber.epicsetclans.commands.subcommands.clan.DuelCommand;
import me.backstabber.epicsetclans.utils.CommonUtils;
import me.backstabber.epicsetclans.utils.materials.EpicMaterials;
import me.backstabber.epicsetclans.utils.materials.UMaterials;

public class ClanDuel extends Guiable
{
	public ClanDuel(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager,ClanTopManager topManager)
	{
		super(plugin, clanManager, duelManager, topManager);
		guiName="duel";
		file=null;
	}
	@Override
	public Inventory getMainInventory(EpicClanData clan,Player player) 
	{
		if(duelManager.isDueling(clan)) return null;

		Inventory inv=Bukkit.createInventory(null, 9, CommonUtils.chat("&8Duel Settings"));
		ItemStack item=EpicMaterials.valueOf(UMaterials.BLACK_STAINED_GLASS_PANE).getItemStack();
		item=CommonUtils.setStringTag(item, "gui:"+guiName+":background");
		for(int i=0;i<9;i++) {
			inv.setItem(i, item);
		}

		setToggles(inv, duelManager.getDuel(clan));
		player.updateInventory();
		return inv;
	}

	
	@Override
	public Inventory getSubInventory(EpicClanData clan,ClanPlayersData data,Player player) 
	{
		return null;
	}

	@Override
	public void inventoryCloseHandle(InventoryCloseEvent event) 
	{
		final String title = event.getView().getTitle();
		final Player player = (Player) event.getPlayer();
		if (!clanManager.isInClan(player.getName())) return;
		
		final EpicClanData clan = (EpicClanData) clanManager.getClanData(player.getName());
		final Map<String, String> placeHolders = new HashMap<>();
		placeHolders.put("%clan%", clan.getClanName());
		placeHolders.put("%player%", player.getName());
		
		if (!duelManager.isDueling(clan)) return;
		
		final ClanDuelData duel = duelManager.getDuel(clan);
		if (!title.equals(CommonUtils.chat("&8Duel Settings")) || !title.equals(CommonUtils.chat("&8Select Arena"))) return;

		new BukkitRunnable() {
			@Override
			public void run() {
				if (player.getOpenInventory() != null) {
					if (!duel.isSent()) {
						new DuelCommand(plugin, clanManager, duelManager).sendMessage(player, "fail", placeHolders);
						duelManager.removeDuel(duel.getClanAlpha().getClanName());
					}
				}
			}
		}.runTaskLater(plugin, 2L);
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
		if(!duelManager.isDueling(clan))
			return;
		ClanDuelData duel = duelManager.getDuel(clan);
		placeHolders.put("%otherclan%", duel.getClanBravo().getClanName());
		if(tag.split(":").length>2)
		{
			if(tag.split(":")[2].equals("background"))
				return;
			if(tag.split(":")[2].equals("arena"))
			{
				duel.getArena().setState(ArenaState.READY);
				player.openInventory(getArenaSelector());
				return;
			}
			if(tag.split(":")[2].equals("arenaselect")&&tag.split(":").length==4)
			{
				DuelArenaData arena=duelManager.getArena(tag.split(":")[3]);
				if(arena!=null&&arena.getState().equals(ArenaState.READY))
				{
					duel.setArena(arena);
					player.openInventory(getMainInventory(clan,player));
					return;
				}
				else
				{
					duel.getArena().setState(ArenaState.READY);
					player.openInventory(getArenaSelector());
					return;
				}
					
			}
			if(tag.split(":")[2].equals("team"))
			{
				int team=duel.getTeamSize();
				if(team<plugin.getDuelSettings().getInt("settings.max-team-size"))
					team=team+1;
				else
					team=plugin.getDuelSettings().getInt("settings.min-team-size");
				duel.setTeamSize(team);
				player.openInventory(getMainInventory(clan,player));
				return;
			}
			if(tag.split(":")[2].equals("send"))
			{
				new DuelCommand(plugin, clanManager, duelManager).sendMessage(player, "sent", placeHolders);
				duelManager.sendDuelRequest(duel);
				player.closeInventory();
				return;
			}
		}
	}
	private Inventory getArenaSelector() 
	{
		Collection<DuelArenaData> arenas = duelManager.getAllArenas().values();
		int size=(int) Math.floor(((double)arenas.size())/9D)+1;
		Inventory inv=Bukkit.createInventory(null, 9*size,CommonUtils.chat("&8Select Arena"));
		ItemStack back=EpicMaterials.valueOf(UMaterials.BLACK_STAINED_GLASS_PANE).getItemStack();
		back=CommonUtils.setStringTag(back, "gui:"+guiName+":background");
		for(int i=0;i<9*size;i++)
		{
			inv.setItem(i, back);
		}
		Iterator<DuelArenaData> iterator = arenas.iterator();
		int index=0;
		while(iterator.hasNext())
		{
			DuelArenaData arena = iterator.next();
			ItemStack item=arena.getItem();
			ItemMeta im=item.getItemMeta();
			List<String> lore=new ArrayList<String>();
			if(item.hasItemMeta()&&item.getItemMeta().hasLore())
				for(String s:item.getItemMeta().getLore())
					lore.add(s);
			lore.add(" ");
			lore.add(CommonUtils.chat("&7Status: "+arena.getState().name()));
			im.setLore(lore);
			item.setItemMeta(im);
			item=CommonUtils.setStringTag(item, "gui:"+guiName+":arenaselect:"+arena.getName());
			inv.setItem(index, item);
			index++;
		}
		return inv;
		
	}
	
	private void setToggles(Inventory inv, ClanDuelData duel)
	{
		ItemStack item=EpicMaterials.valueOf(UMaterials.GRASS_BLOCK).getItemStack();
		ItemMeta im=item.getItemMeta();
		im.setDisplayName(CommonUtils.chat("&e&lArena"));
		ArrayList<String> lore=new ArrayList<String>();
		lore.add(CommonUtils.chat("&fSelect the arena you"));
		lore.add(CommonUtils.chat("&fwant to duel in."));
		lore.add(CommonUtils.chat(" "));
		lore.add(CommonUtils.chat("&7Arena : &a"+duel.getArena().getName()));
		lore.add(CommonUtils.chat("&7(Click to change)"));
		im.setLore(lore);
		item.setItemMeta(im);
		item=CommonUtils.setStringTag(item, "gui:"+guiName+":arena");
		inv.setItem(2, item);
		
		item=EpicMaterials.valueOf(UMaterials.PLAYER_HEAD_ITEM).getItemStack();
		im=item.getItemMeta();
		im.setDisplayName(CommonUtils.chat("&a&lTeam Size"));
		lore.clear();
		lore.add(CommonUtils.chat("&fSet the team size"));
		lore.add(CommonUtils.chat("&ffor this duel."));
		lore.add(CommonUtils.chat(" "));
		lore.add(CommonUtils.chat("&7Size : &a"+duel.getTeamSize()));
		lore.add(CommonUtils.chat("&7(Click to change)"));
		im.setLore(lore);
		item.setItemMeta(im);
		item=CommonUtils.setStringTag(item, "gui:"+guiName+":team");
		// 012345678
		inv.setItem(4, item);
		
		item=EpicMaterials.valueOf(UMaterials.ARROW).getItemStack();
		im=item.getItemMeta();
		im.setDisplayName(CommonUtils.chat("&b&lSend Request"));
		lore.clear();
		lore.add(CommonUtils.chat("&fSend duel request to"));
		lore.add(CommonUtils.chat("&fother clan."));
		lore.add(CommonUtils.chat("&7(Click to send)"));
		im.setLore(lore);
		item.setItemMeta(im);
		item=CommonUtils.setStringTag(item, "gui:"+guiName+":send");
		inv.setItem(6, item);
	}
}
