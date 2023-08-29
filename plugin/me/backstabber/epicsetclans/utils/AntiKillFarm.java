package me.backstabber.epicsetclans.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.clanhandles.manager.YMLManager;

public class AntiKillFarm 
{
	private EpicSetClans plugin;
	private  EpicClanManager clanManager;
	private Map<String, Map<String, Integer>> data=new HashMap<String, Map<String,Integer>>();
	private YMLManager file;
	@SuppressWarnings("unused")
	private BukkitTask timer;

	public AntiKillFarm(final EpicSetClans plugin, final EpicClanManager clanManager) {
		this.plugin = plugin;
		this.clanManager = clanManager;
	}

	public void setup() {
		file=plugin.getFiles().get("antifarming");
		timer=new BukkitRunnable() 
		{
			@Override
			public void run() 
			{
				data.clear();
			}
		}.runTaskTimer(plugin, file.getFile().getInt("record-kills.time"), file.getFile().getInt("record-kills.time"));
	}
	public boolean isfarmed(Player alpha,Player bravo) //alpha is the killer & bravo is the victim
	{
		//test ip
		if(file.getFile().getBoolean("same-ip.enabled"))
		{
			String ipAlpha=alpha.getAddress().getHostName();
			String ipBravo=bravo.getAddress().getHostName();
			if(ipAlpha.equals(ipBravo))
			{
				int respect=file.getFile().getInt("same-ip.penalty.respect");
				if(clanManager.getClanData(alpha.getName())!=null)
					((EpicClanData) clanManager.getClanData(alpha.getName())).removeRespect(respect);
				if(clanManager.getClanData(bravo.getName())!=null)
					((EpicClanData) clanManager.getClanData(bravo.getName())).removeRespect(respect);
				alpha.sendMessage(CommonUtils.chat(file.getFile().getString("same-ip.message")));
				bravo.sendMessage(CommonUtils.chat(file.getFile().getString("same-ip.message")));
				for(String cmd:file.getFile().getStringList("same-ip.penalty.commands"))
				{
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", alpha.getName()));
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", bravo.getName()));
				}
				return true;
			}
		}
		//test record
		if(file.getFile().getBoolean("record-kills.enabled"))
		{
			//get alpha's data
			if(!data.containsKey(alpha.getName()))
				data.put(alpha.getName(), new HashMap<>());
			Map<String, Integer> alphaData = data.get(alpha.getName());
			//check if it has bravo set
			if(!alphaData.containsKey(bravo.getName()))
				alphaData.put(bravo.getName(), 0);
			int timesKilled=alphaData.get(bravo.getName())+1;
			if(timesKilled>file.getFile().getInt("record-kills.max-kills")) //penalty
			{
				int respect=file.getFile().getInt("record-kills.penalty.respect");
				if(clanManager.getClanData(alpha.getName())!=null)
					((EpicClanData) clanManager.getClanData(alpha.getName())).removeRespect(respect);
				alpha.sendMessage(CommonUtils.chat(file.getFile().getString("record-kills.message")));
				for(String cmd:file.getFile().getStringList("record-kills.penalty.commands"))
				{
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", alpha.getName()));
				}
				return true;
			}
			alphaData.put(bravo.getName(), timesKilled);
			data.put(alpha.getName(), alphaData);
		}
		return false;
	}
}
