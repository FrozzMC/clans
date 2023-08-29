package me.backstabber.epicsetclans.clanhandles.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.data.PlayerData;
import me.backstabber.epicsetclans.clanhandles.data.DuelArenaData.ArenaState;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.clan.DuelCommand;
import me.backstabber.epicsetclans.utils.CommonUtils;
import me.backstabber.epicsetclans.utils.materials.EpicMaterials;
import me.backstabber.epicsetclans.utils.materials.UMaterials;

public class ClanDuelData 
{
	private EpicSetClans handle;
	private EpicClanManager clanManager;
	private DuelArenaData arena;
	private boolean isRequestSent=false;
	private boolean isRequestAccepted=false;
	private boolean isTeamReady=false;
	private boolean isDuelStarted=false;
	private boolean isDuelEnded=false;
	private int requestSendTimeOut;
	private int requestAcceptTimeOut;
	private int duelPreStartTime;
	private int duelTime;
	private int duelPreEndTime;
	private int teamSize;
	private ArrayList<Player> teamAlpha=new ArrayList<>();
	private ArrayList<Player> teamBravo=new ArrayList<>();
	private EpicClanData clanAlpha;
	private EpicClanData clanBravo;

	public ClanDuelData(final EpicSetClans handle, final EpicClanManager clanManager, final DuelArenaData arena) {
		this.handle = handle;
		this.clanManager = clanManager;
		this.arena = arena;
	}

	public ClanDuelData createNew(
		final EpicClanManager clanManager,
		final ClanDuelManager duelManager,
		final DuelArenaData arena,
		final EpicClanData clanAlpha,
		final EpicClanData clanBravo
	) {
		if(duelManager.getFirstFree()!=null)
		{
			ClanDuelData data = new ClanDuelData(EpicSetClans.get(), clanManager, arena);
			data.arena=duelManager.getFirstFree();
			data.requestSendTimeOut=handle.getDuelSettings().getFile().getInt("settings.request-send-time");
			data.requestAcceptTimeOut=handle.getDuelSettings().getFile().getInt("settings.request-recieve-time");
			data.duelPreStartTime=handle.getDuelSettings().getFile().getInt("settings.duel-prestart-time");
			data.duelTime=handle.getDuelSettings().getFile().getInt("settings.duel-time");
			data.duelPreEndTime=handle.getDuelSettings().getFile().getInt("settings.duel-preend-time");
			data.teamSize=handle.getDuelSettings().getFile().getInt("settings.min-team-size");
			data.arena.setState(ArenaState.BOOKED);
			data.clanAlpha=clanAlpha;
			data.clanBravo=clanBravo;
			return data;
		}
		return null;
	}
	public DuelArenaData getArena()
	{
		return this.arena;
	}
	public int getTeamSize()
	{
		return this.teamSize;
	}
	public void handleDeath(Player player)
	{
		if(teamAlpha.contains(player))
		{
			teamAlpha.remove(player);
			//remove players from ignored in arena
			this.arena.removeIgnored(player);
		}
		if(teamBravo.contains(player))
		{
			teamBravo.remove(player);
			//remove players from ignored in arena
			this.arena.removeIgnored(player);
		}
	}
	public void runSecond()
	{
		if(!isRequestSent) //request was not sent (i.e sender is still setting up duel)
		{
			return;
		}
		if(isRequestSent&&!isRequestAccepted) //request was sent but not accepted by other clan
		{
			if(requestSendTimeOut>0) //other clan still has time
				requestSendTimeOut--;
			else //time ended for the clan to accept
				endDuel();
		}
		if(isRequestAccepted&&!isTeamReady) //request was accepted but teams were not set
		{
			if(areTeamsReady()) //teams are ready
			{
				preStartDuel();
				isTeamReady=true;
			}
			else if(requestAcceptTimeOut>0) //team are not ready but still have time
				requestAcceptTimeOut--;
			else	//teams not ready & time ran out
				endDuel();
		}
		if(isTeamReady&&!isDuelStarted) //teams are ready but duel hasnt started yet
		{
			if(duelPreStartTime>0)
				duelPreStartTime--;
			else
			{
				startDuel();
				isDuelStarted=true;
			}
		}
		if(isDuelStarted&&!isDuelEnded) //duel was started but hasnt ended yet
		{
			if(duelTime>0)
				duelTime--;
			else //time ran out
				endDuel();
		}
		if(isDuelStarted&&isWinner()&&!isDuelEnded) //one team won
		{
			preEndDuel();
			this.isDuelEnded=true;
		}
		if(isDuelEnded) //duel ended
		{
			if(duelPreEndTime>0)
				duelPreEndTime--;
			else
				endDuel();
		}
	}
	public void sentDuel(EpicClanData clanBravo)
	{
		if(this.clanBravo.equals(clanBravo))
			this.isRequestSent=true;
	}
	public boolean isSent() 
	{
		return this.isRequestSent;
	}
	public void acceptDuel(EpicClanData clanBravo)
	{
		if(this.clanBravo.equals(clanBravo))
			this.isRequestAccepted=true;
	}
	public EpicClanData getClanAlpha()
	{
		return this.clanAlpha;
	}
	public EpicClanData getClanBravo()
	{
		return this.clanBravo;
	}
	public void addPlayer(Player player)
	{
		if(canAdd(player))
		{
			if(clanManager.getClanData(player.getName()).equals(clanAlpha))
			{
				teamAlpha.add(player);
				//broadcast message
				for(String s:replacePlaceholders(handle.getDuelSettings().getFile().getStringList("messages.on-join"), player, true))
					player.sendMessage(s);
			}
			if(clanManager.getClanData(player.getName()).equals(clanBravo))
			{
				teamBravo.add(player);
				for(String s:replacePlaceholders(handle.getDuelSettings().getFile().getStringList("messages.on-join"), player, false))
					player.sendMessage(s);
			}
		}
	}
	public boolean canAdd(Player player)
	{
		if(clanManager.isInClan(player.getName()))
		{
			if(clanManager.getClanData(player.getName()).equals(clanAlpha))
			{
				if(teamAlpha.size()<teamSize)
					return true;
			}
			if(clanManager.getClanData(player.getName()).equals(clanBravo))
			{
				if(teamBravo.size()<teamSize)
					return true;
			}
		}
		return false;
	}
	public EpicClanData getLoser() 
	{
		if(teamAlpha.size()>teamBravo.size())
			return clanBravo;
		else
			return clanAlpha;
	}
	private boolean isWinner() 
	{
		if(this.teamAlpha.size()<=0||this.teamBravo.size()<=0)
			return true;
		return false;
	}
	public EpicClanData getWinner() 
	{
		if(teamAlpha.size()>teamBravo.size())
			return clanAlpha;
		else
			return clanBravo;
	}
	private void preStartDuel() 
	{
		List<String> message=handle.getDuelSettings().getFile().getStringList("messages.on-prestart");
		for(Player player:teamAlpha) {
			for(String s:replacePlaceholders(message, player, true)) {
				player.sendMessage(CommonUtils.chat(s));
			}
			player = null;
		}
		for(Player player : teamBravo) {
			for(String s:replacePlaceholders(message, player, false)) {
				player.sendMessage(CommonUtils.chat(s));
			}
			player = null;
		}
	}
	private void preEndDuel() 
	{
		//send message to all teams
		List<String> message=handle.getDuelSettings().getFile().getStringList("messages.on-end");
		for(Player player:teamAlpha)
			for(String s:replacePlaceholders(message, player, true))
				player.sendMessage(CommonUtils.chat(s));
		for(Player player:teamBravo)
			for(String s:replacePlaceholders(message, player, false))
				player.sendMessage(CommonUtils.chat(s));
		//broadcast message
		if(handle.getDuelSettings().getFile().isSet("messages.broadcast-on-end"))
		{
			List<String> broadcast=handle.getDuelSettings().getFile().getStringList("messages.broadcast-on-end");
			for(String s:replacePlaceholders(broadcast, null, true))
					Bukkit.broadcastMessage(CommonUtils.chat(s));
		}
	}
	public void endDuel()
	{
		//check at which point did the duel end & send message
		if(requestSendTimeOut<=0) //ended before request was even accepted
		{
			//send message &
			//remove metadata
			for(String playerName:clanAlpha.getClanMembersName())
			{
				if(Bukkit.getPlayerExact(playerName)!=null)
				{
					new DuelCommand(handle, clanManager, null).sendMessage(Bukkit.getPlayerExact(playerName), "reject", new HashMap<>());
					Bukkit.getPlayerExact(playerName).removeMetadata("EpicDuelRequest", handle);
				}
			}
			for(String playerName:clanBravo.getClanMembersName())
			{
				if(Bukkit.getPlayerExact(playerName)!=null)
				{
					new DuelCommand(handle, clanManager, null).sendMessage(Bukkit.getPlayerExact(playerName), "reject", new HashMap<>());
					Bukkit.getPlayerExact(playerName).removeMetadata("EpicDuelRequest", handle);
				}
			}
			
		}
		else if(requestAcceptTimeOut<=0) //ended before teams were ready (no winners/loosers)
		{
			List<String> message=handle.getDuelSettings().getFile().getStringList("messages.on-teaming-fail");
			for(Player player:teamAlpha)
				for(String s:replacePlaceholders(message, player, true))
					player.sendMessage(CommonUtils.chat(s));
			for(Player player:teamBravo)
				for(String s:replacePlaceholders(message, player, false))
					player.sendMessage(CommonUtils.chat(s));
			for(String playerName:clanAlpha.getClanMembersName())
			{
				if(Bukkit.getPlayerExact(playerName)!=null)
				{
					Bukkit.getPlayerExact(playerName).removeMetadata("EpicDuel", handle);
				}
			}
			for(String playerName:clanBravo.getClanMembersName())
			{
				if(Bukkit.getPlayerExact(playerName)!=null)
				{
					Bukkit.getPlayerExact(playerName).removeMetadata("EpicDuel", handle);
				}
			}
		}
		else //ended either after completetion or timeout (find winner)
		{
			
			EpicClanData winner=getWinner();
			winner.setWonDuels(winner.getWonDuels()+1);
			for(PlayerData data:winner.getMembersData().values())
			{
				((ClanPlayersData) data).setDuelsPlayed(data.getDuelsPlayed()+1);
				((ClanPlayersData) data).setDuelsWon(data.getDuelsWon()+1);
			}
			EpicClanData looser=getLoser();
			looser.setLostDuels(looser.getLostDuels()+1);
			for(PlayerData data:looser.getMembersData().values())
			{
				((ClanPlayersData) data).setDuelsPlayed(data.getDuelsPlayed()+1);
			}
		}
		
		//remove players from ignored in arena
		this.arena.clearIgnored();
		//set arena state
		this.arena.setState(ArenaState.READY);
		
		// Teleport players to the spawn.
		teamAlpha.forEach(player -> player.chat("/warp spawn"));
		teamBravo.forEach(player -> player.chat("/warp spawn"));
		
		//unregister this duel
		EpicSetClans.get().getClanDuelManager().removeDuel(getClanAlpha().getClanName());
	}
	
	private void startDuel() 
	{
		for(Player player : teamAlpha) {
			//remove all potion effects (to prevent pre-gapple abuse)
			for(PotionEffect effect : player.getActivePotionEffects()) {
				player.removePotionEffect(effect.getType());
			}
			
			player = null;
		}
		
		for(Player player : teamBravo) {
			//remove all potion effects (to prevent pre-gapple abuse)
			for(PotionEffect effect:player.getActivePotionEffects()) {
				player.removePotionEffect(effect.getType());
			}
			
			player = null;
		}
		
		//add players to arena
		this.arena.tpToSpawnAlpha(teamAlpha);
		this.arena.tpToSpawnBravo(teamBravo);
		//change state of arena
		this.arena.setState(ArenaState.IN_USE);
		
		//send message to all teams
		final List<String> message = handle.getDuelSettings().getFile().getStringList("messages.on-start");
		for(Player player:teamAlpha) {
			player.chat("/protectionoff");
			for(String s:replacePlaceholders(message, player, true)) {
				player.sendMessage(CommonUtils.chat(s));
			}
			
			player = null;
		}
		
		for(Player player:teamBravo) {
			player.chat("/protectionoff");
			for(String s:replacePlaceholders(message, player, false)) {
				player.sendMessage(CommonUtils.chat(s));
			}
			
			player = null;
		}
		
		//broadcast message
		if(handle.getDuelSettings().getFile().isSet("messages.broadcast-on-start")) {
			for(final String s : replacePlaceholders(handle.getDuelSettings().getFile().getStringList("messages.broadcast-on-start"), null, true)) {
				Bukkit.broadcastMessage(CommonUtils.chat(s));
			}
		}
	}
	private boolean areTeamsReady() 
	{
		return (teamAlpha.size() == teamSize) && (teamBravo.size() == teamSize);
	}
	private List<String> replacePlaceholders(List<String> message,Player player,boolean isAlpha)
	{
		List<String> replaced=new ArrayList<String>();
		for(String s:message)
		{
			if(player!=null)
				s=s.replace("%player%", player.getName());
			s=s.replace("%clanalpha%",clanAlpha.getClanName());
			s=s.replace("%clanbravo%",clanBravo.getClanName());
			s=s.replace("%dueltime%", CommonUtils.getTimeFormat(duelTime));
			s=s.replace("%winner%", getWinner().getClanName());
			s=s.replace("%loser%", getLoser().getClanName());
			if(s.contains("%clanalphateam%"))
			{
				String temp=s;
				for(Player p:teamAlpha)
					replaced.add(CommonUtils.chat(temp.replace("%clanalphateam%", p.getName())));
				continue;
			}
			if(s.contains("%clanbravoteam%"))
			{
				String temp=s;
				for(Player p:teamBravo)
					replaced.add(CommonUtils.chat(temp.replace("%clanbravoteam%", p.getName())));
				continue;
			}
			if(s.contains("%team%"))
			{
				String temp=s;
				if(isAlpha)
					for(Player p:teamAlpha)
						replaced.add(CommonUtils.chat(temp.replace("%team%", p.getName())));
				else
					for(Player p:teamBravo)
						replaced.add(CommonUtils.chat(temp.replace("%team%", p.getName())));
				continue;
			}
			replaced.add(CommonUtils.chat(s));
		}
		
		return replaced;
	}

	public void stop() 
	{
		//remove exceptions from arena
		this.arena.clearIgnored();
		this.arena.setState(ArenaState.READY);
	}
	public void setArena(DuelArenaData arena2) 
	{
		//free previous arena
		this.arena.setState(ArenaState.READY);
		//change arena
		this.arena=arena2;
		//book this arena
		arena2.setState(ArenaState.BOOKED);
	}
	public void setTeamSize(int team) 
	{
		this.teamSize=team;
	}
}
