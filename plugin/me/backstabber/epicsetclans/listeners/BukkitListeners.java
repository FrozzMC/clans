package me.backstabber.epicsetclans.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.data.ClanData;
import me.backstabber.epicsetclans.api.data.PlayerData;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.data.DuelArenaData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.clanhandles.saving.SavingManager;
import me.backstabber.epicsetclans.commands.subcommands.admin.ArenaCommand;
import me.backstabber.epicsetclans.guis.Guiable;
import me.backstabber.epicsetclans.hooks.worldguard.WorldguardHook;
import me.backstabber.epicsetclans.utils.AntiKillFarm;

@SuppressWarnings("deprecation")
public class BukkitListeners implements Listener 
{
	private EpicSetClans plugin;
	private ArenaCommand arenaCommand;
	private ClanDuelManager duelManager;
	private EpicClanManager clanManager;
	private WorldguardHook wgHook;
	private SavingManager savingManager;
	private AntiKillFarm antiFarm;

	public BukkitListeners(
		final EpicSetClans plugin,
		final ArenaCommand arenaCommand,
		final ClanDuelManager duelManager,
		final EpicClanManager clanManager,
		final WorldguardHook wgHook,
		final SavingManager savingManager,
		final AntiKillFarm antiFarm
	) {
		this.plugin = plugin;
		this.arenaCommand = arenaCommand;
		this.duelManager = duelManager;
		this.clanManager = clanManager;
		this.wgHook = wgHook;
		this.savingManager = savingManager;
		this.antiFarm = antiFarm;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		savingManager.updateClan(event.getPlayer().getName());
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		arenaCommand.onPlayerInteract(event);
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		for(Guiable guis:plugin.getGuis().values())
			guis.inventoryClickHandle(event);
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		for(DuelArenaData arena:duelManager.getAllArenas().values())
			arena.playerMoveHandle(event);
	}
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event)
	{
		for(Guiable guis:plugin.getGuis().values())
			guis.inventoryCloseHandle(event);
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(PlayerChatEvent event)
	{
		plugin.getChatHandle().handleChatEvent(event);
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent event)
	{
		plugin.getChatHandle().handleAsyncChatEvent(event);
	}
	@EventHandler
	public void onLogout(PlayerQuitEvent event)
	{
		duelManager.handlePlayerLeave(event);
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		duelManager.handlePlayerDeath(event);
		Player bravo=event.getEntity();
		if(event.getEntity().getKiller()!=null&&event.getEntity().getKiller() instanceof Player)
		{
			Player alpha=event.getEntity().getKiller();
			if(!antiFarm.isfarmed(alpha, bravo))
			{
				if(clanManager.getClanData(alpha.getName())!=null)
				{
					ClanData alphaClan = clanManager.getClanData(alpha.getName());
					PlayerData member = alphaClan.getMemberData(alpha);
					member.setKills(member.getKills()+1);
					member.setPoints(member.getPoints()+plugin.getFormulas().getFile().getInt("points-per-kill"));
				}
				if(clanManager.getClanData(bravo.getName())!=null)
				{
					ClanData bravoClan = clanManager.getClanData(bravo.getName());
					PlayerData member = bravoClan.getMemberData(bravo);
					member.setDeaths(member.getDeaths()+1);
					int points=member.getPoints()-plugin.getFormulas().getFile().getInt("points-per-death");
					if(points<0)
						points=0;
					member.setPoints(points);
				}
			}
		}
	}
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event)
	{
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player)
		{
			Player victim=(Player) event.getEntity();
			Player attacker=(Player) event.getDamager();
			if(isAllied(victim,attacker))
			{
				event.setCancelled(true);
			}
		}
		if(event.getEntity() instanceof Player &&event.getDamager() instanceof Projectile)
		{
			if(((Projectile)event.getDamager()).getShooter() instanceof Player)
			{
				Player victim=(Player) event.getEntity();
				Player attacker=(Player) ((Projectile)event.getDamager()).getShooter();
				if(isAllied(victim,attacker))
				{
					event.setCancelled(true);
				}
			}
		}
	}
	private boolean isAllied(Player victim, Player attacker) 
	{
		if(!clanManager.isInClan(victim.getName()))
			return false;
		if(!clanManager.isInClan(attacker.getName()))
			return false;
		EpicClanData attackerClan = (EpicClanData) clanManager.getClanData(attacker.getName());
		EpicClanData victimClan = (EpicClanData) clanManager.getClanData(victim.getName());
		if(attackerClan.equals(victimClan))
		{
			if(wgHook!=null&&!wgHook.allowedClans(attacker.getLocation()))
				return false;
			return true;
		}
		if(attackerClan.getClanAllies().contains(victimClan))
		{
			if(wgHook!=null&&!wgHook.allowedAllies(attacker.getLocation()))
				return false;
			return true;
		}
		if(attackerClan.getClanTruce().contains(victimClan))
		{
			if(wgHook!=null&&!wgHook.allowedTruce(attacker.getLocation()))
				return false;
			return true;
		}
		return false;
	}
}
