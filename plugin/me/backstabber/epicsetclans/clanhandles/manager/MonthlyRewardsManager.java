package me.backstabber.epicsetclans.clanhandles.manager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;


import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.data.ClanData;
import me.backstabber.epicsetclans.api.data.PlayerData;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.data.ClanVaultsData;
import me.backstabber.epicsetclans.enums.ClanNodes;
import me.backstabber.epicsetclans.utils.CommonUtils;

public class MonthlyRewardsManager 
{
	private EpicSetClans handle;
	private EpicClanManager clanManager;
	private ClanTopManager topManager;
	
	private YMLManager settings;
	private String currentYear;
	private String currentMonth;
	private boolean messaged=false;
	public MonthlyRewardsManager(YMLManager file,EpicSetClans handle,EpicClanManager clanManager,ClanTopManager topManager)
	{
		this.settings=file;
		this.handle=handle;
		this.clanManager=clanManager;
		this.topManager=topManager;
		if(isEnabled())
		{
			new BukkitRunnable() 
			{

				@Override
				public void run() 
				{
					currentMonth=CommonUtils.getMonth();
					currentYear=CommonUtils.getYear();
					if(!file.getFile().getStringList("ignored-months").contains(currentMonth.toUpperCase())) //is not ignored
					{
						if(testTime())//if time has passed
						{
							File file=new File(handle.getDataFolder()+"/rewards/"+currentYear,currentMonth+".yml");
							if(!file.exists())
							{
								if(!tooLate())
								{
									try 
									{
										file.createNewFile();
									} 
									catch (IOException e) 
									{
										
									}
									//send message to the server
									breadcastMessage();
									//save reward in a file
									YMLManager data=new YMLManager(handle, currentMonth, "/rewards/"+currentYear);
									saveRewardData(data);
									//reset all clan stats
									resetStats();
									messaged=false;
								}
								else if(!messaged)
								{
									messaged=true;
									Bukkit.getConsoleSender().sendMessage(CommonUtils.chat("&a[EpicSet-Clans] >&aMonthly rewards for &e"+currentMonth+" "+currentYear+"&a were not given automatically."));
									Bukkit.getConsoleSender().sendMessage(CommonUtils.chat("&a[EpicSet-Clans] >&aRun /clanadmin rewards execute to give them manually."));
								}
							}
							else
							{
								YMLManager data=new YMLManager(handle, currentMonth, "/rewards/"+currentYear);
								rewardPlayers(data);
							}
						}
					}
				}
			}.runTaskTimer(handle, 3*20, 20*60);
		}
	}


	public boolean isEnabled()
	{
		return settings.getFile().getBoolean("enabled");
	}
	public void runReward() 
	{
		File file=new File(handle.getDataFolder()+"/rewards/"+currentYear,currentMonth+".yml");
		try 
		{
			file.createNewFile();
		} 
		catch (IOException e) 
		{
			
		}
		//send message to the server
		breadcastMessage();
		//save reward in a file
		YMLManager data=new YMLManager(handle, currentMonth, "/rewards/"+currentYear);
		saveRewardData(data);
		//reset all clan stats
		resetStats();
	}
	public List<String> getUnrewarded()
	{
		File file=new File(handle.getDataFolder()+"/rewards/"+currentYear,currentMonth+".yml");
		if(file.exists())
		{
			List<String> names=new ArrayList<>();
			YMLManager data=new YMLManager(handle, currentMonth, "/rewards/"+currentYear);
			for(String name:data.getFile().getKeys(false))
			{
				if(!data.getFile().getBoolean(name+".isrewarded"))
					names.add(name);
			}
			return names;
		}
		return new ArrayList<>();
	}
	public void rewardPlayer(Player player)
	{
		File file=new File(handle.getDataFolder()+"/rewards/"+currentYear,currentMonth+".yml");
		if(file.exists())
		{
			YMLManager data=new YMLManager(handle, currentMonth, "/rewards/"+currentYear);
			if(data.getFile().isSet(player.getName())&&!data.getFile().getBoolean(player.getName()+".isrewarded"))
			{
				for(String cmd:data.getFile().getStringList(player.getName()+".rewards"))
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
				for(String msg:data.getFile().getStringList(player.getName()+".messages"))
				player.sendMessage(CommonUtils.chat(msg));
				data.getFile().set(player.getName()+".isrewarded", true);
				data.save(true);
			}
		}
	}
	public void rewardPlayers(YMLManager data) 
	{
		if(!isEnabled())
			return;
		for(String playerName:data.getFile().getKeys(false))
		{
			if(!data.getFile().getBoolean(playerName+".isrewarded"))
			{
				if(Bukkit.getPlayerExact(playerName)!=null)
				{
					Player player=Bukkit.getPlayerExact(playerName);
					for(String cmd:data.getFile().getStringList(playerName+".rewards"))
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
					for(String msg:data.getFile().getStringList(playerName+".messages"))
					player.sendMessage(CommonUtils.chat(msg));
					data.getFile().set(playerName+".isrewarded", true);
				}
			}
		}
		data.save(true);
	}
	public ItemStack addLore(ItemStack item,int position)
	{
		if(!isEnabled())
			return item;
		if(!settings.getFile().isSet("rewards."+position))
			return item;
		ItemMeta im=item.getItemMeta();
		List<String> lore=new ArrayList<>();
		if(item.hasItemMeta()&&item.getItemMeta().hasLore())
			for(String s:item.getItemMeta().getLore())
				lore.add(s);
		for(String s:settings.getFile().getStringList("end-lore-on-top-gui"))
		{
			s=s.replace("%reward_leader%", String.valueOf(settings.getFile().get("rewards."+position+".leader.placeholder")));
			s=s.replace("%reward_member%", String.valueOf(settings.getFile().get("rewards."+position+".member.placeholder")));
			lore.add(CommonUtils.chat(s));
		}
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}


	private void breadcastMessage() 
	{
		if(settings.getFile().isSet("messages.broadcast-on-rewarding"))
		{
			for(String s:settings.getFile().getStringList("messages.broadcast-on-rewarding"))
			{
				if(s.contains("%pos%"))
				{
					
					for(int position=1;position<=20;position++)
					{
						if(topManager.getClan(Integer.valueOf(position))!=null)
						{
							if(settings.getFile().isSet("rewards."+position))
							{
								ClanData clan = topManager.getClan(Integer.valueOf(position));
								String temp=s;
								temp=temp.replace("%pos%", String.valueOf(position));
								temp=temp.replace("%clan_name%", clan.getClanName());
								temp=temp.replace("%clan_leader%", clan.getClanLeader());
								temp=temp.replace("%reward_leader%", String.valueOf(settings.getFile().get("rewards."+position+".leader.placeholder")));
								temp=temp.replace("%reward_member%", String.valueOf(settings.getFile().get("rewards."+position+".member.placeholder")));
								Bukkit.broadcastMessage(CommonUtils.chat(temp));
							}
						}
					}
					continue;
				}
				Bukkit.broadcastMessage(CommonUtils.chat(s));
			}
		}
	}
	private void resetStats() 
	{
		for(ClanData clan:clanManager.getAllClans())
		{
			if(settings.getFile().getBoolean("stats-cleared.points"))
				for(PlayerData member:clan.getMembersData().values())
					member.setPoints(0);
			if(settings.getFile().getBoolean("stats-cleared.kills"))
				for(PlayerData member:clan.getMembersData().values())
					member.setKills(0);
			if(settings.getFile().getBoolean("stats-cleared.deaths"))
				for(PlayerData member:clan.getMembersData().values())
					member.setDeaths(0);
			if(settings.getFile().getBoolean("stats-cleared.duels"))
			{
				((EpicClanData) clan).setWonDuels(0);
				((EpicClanData) clan).setLostDuels(0);
			}
			if(settings.getFile().getBoolean("stats-cleared.vaults"))
				((ClanVaultsData) clan.getVaults()).clearAllVaults();
			if(settings.getFile().getBoolean("stats-cleared.bank"))
				clan.setClanBalance(0);
			if(settings.getFile().getBoolean("stats-cleared.respect"))
			{
				((EpicClanData) clan).setFromNode(ClanNodes.RESPECT_ADITION, 0);
				((EpicClanData) clan).setFromNode(ClanNodes.RESPECT_SUBTRACTION, 0);
			}
			((EpicClanData) clan).save();
		}
		topManager.sortClans();
	}


	private boolean testTime() 
	{
		long time = System.currentTimeMillis();
		//test day
		SimpleDateFormat sdf = new SimpleDateFormat("dd");   
		Date resultdate = new Date(time);
		if(Integer.valueOf(sdf.format(resultdate))<settings.getFile().getInt("apply-at.day"))
			return false;
		//test hours
		sdf = new SimpleDateFormat("HH");   
		//Bukkit.broadcastMessage("Hours : "+sdf.format(resultdate) );
		if(Integer.valueOf(sdf.format(resultdate))<settings.getFile().getInt("apply-at.hour"))
			return false;
		//test Min
		sdf = new SimpleDateFormat("mm");   
		//Bukkit.broadcastMessage("Minutes : "+sdf.format(resultdate) );
		if(Integer.valueOf(sdf.format(resultdate))<settings.getFile().getInt("apply-at.min"))
			return false;
		return true;
	}
	private boolean tooLate() 
	{
		long time = System.currentTimeMillis();
		//test day
		SimpleDateFormat sdf = new SimpleDateFormat("dd");   
		Date resultdate = new Date(time);
		if(Integer.valueOf(sdf.format(resultdate))>settings.getFile().getInt("apply-at.day")+15)
			return true;
		return false;
	}
	private void saveRewardData(YMLManager file)
	{
		Map<Integer, ClanData> topClans = topManager.getSortedClans();
		for(int position=1;position<=20;position++)
		{
			if(settings.getFile().isSet("rewards."+position)&&topClans.containsKey(position)) //add player's data to file
			{
				ClanData clan = topClans.get(position);
				file.getFile().set(clan.getClanLeader()+".rewards", settings.getFile().getStringList("rewards."+position+".leader.commands"));
				List<String> messages=new ArrayList<String>();
				for(String s:settings.getFile().getStringList("messages.send-to-winners"))
				{
					s=s.replace("%reward%", String.valueOf(settings.getFile().get("rewards."+position+".leader.placeholder")));
					s=s.replace("%prefix%", handle.getSettings().getFile().getString("settings.prefix"));
					messages.add(s);
				}
				file.getFile().set(clan.getClanLeader()+".messages", messages);
				file.getFile().set(clan.getClanLeader()+".isrewarded",false);
				for(String member:((EpicClanData) clan).getClanMembersName())
				{
					if(!member.equals(clan.getClanLeader()))
					{
						file.getFile().set(member+".rewards", settings.getFile().getStringList("rewards."+position+".member.commands"));
						messages.clear();
						for(String s:settings.getFile().getStringList("messages.send-to-winners"))
						{
							s=s.replace("%reward%", String.valueOf(settings.getFile().get("rewards."+position+".member.placeholder")));
							s=s.replace("%prefix%", handle.getSettings().getFile().getString("settings.prefix"));
							messages.add(s);
						}
						file.getFile().set(member+".messages", messages);
						file.getFile().set(member+".isrewarded",false);
					}
				}
			}
		}
		file.save(true);
	}
}
