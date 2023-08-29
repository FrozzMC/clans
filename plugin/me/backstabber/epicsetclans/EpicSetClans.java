package me.backstabber.epicsetclans;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.backstabber.epicsetclans.clanhandles.manager.*;
import me.backstabber.epicsetclans.clanhandles.saving.LocalSaving;
import me.backstabber.epicsetclans.clanhandles.saving.MySqlSaving;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.backstabber.epicsetclans.api.manager.ClanManager;
import me.backstabber.epicsetclans.clanhandles.ClanChatHandle;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.data.ClanDuelData;
import me.backstabber.epicsetclans.clanhandles.data.ClanVaultsData;
import me.backstabber.epicsetclans.clanhandles.data.DuelArenaData;
import me.backstabber.epicsetclans.clanhandles.saving.SavingManager;
import me.backstabber.epicsetclans.commands.ClanAdminCommand;
import me.backstabber.epicsetclans.commands.ClanCommand;
import me.backstabber.epicsetclans.commands.subcommands.admin.ArenaCommand;
import me.backstabber.epicsetclans.guis.ClanDuel;
import me.backstabber.epicsetclans.guis.ClanManage;
import me.backstabber.epicsetclans.guis.ClanShop;
import me.backstabber.epicsetclans.guis.ClanStats;
import me.backstabber.epicsetclans.guis.ClanTop;
import me.backstabber.epicsetclans.guis.Guiable;
import me.backstabber.epicsetclans.hooks.NameTagEditHook;
import me.backstabber.epicsetclans.hooks.PapiHook;
import me.backstabber.epicsetclans.hooks.worldguard.SupportedWG;
import me.backstabber.epicsetclans.hooks.worldguard.UnsupportedWG;
import me.backstabber.epicsetclans.hooks.worldguard.WorldguardHook;
import me.backstabber.epicsetclans.listeners.BukkitListeners;
import me.backstabber.epicsetclans.utils.AntiKillFarm;
import me.backstabber.epicsetclans.utils.CommonUtils;
import net.milkbowl.vault.economy.Economy;

public class EpicSetClans extends JavaPlugin
{
	private static EpicSetClans plugin;
	
	private EpicClanManager clanManager;
	private ClanTopManager topManager;
	private ClanDuelManager duelManager;
	private WorldguardHook wgHook;
	private ClanChatHandle chatHandle;
	private ArenaCommand arenaCommand;
	private CostsManager costsManager;
	private SavingManager savingManager;
	private AntiKillFarm antiFarm;
	private DuelArenaData arenaData;
	private MonthlyRewardsManager monthlyRewardsManager;
	private Map<String, YMLManager> files=new HashMap<>();
	private Map<String, Guiable> guis=new HashMap<>();
	private Economy eco;
	
	public static EpicSetClans get() {
		return plugin;
	}
	
	@Override
	public void onLoad() 
	{
		plugin = this;
		if(Bukkit.getPluginManager().getPlugin("WorldGuard") !=null )
		{
			try {
				wgHook=new SupportedWG();
			} catch (final NoClassDefFoundError e) {
				Bukkit.getConsoleSender().sendMessage(CommonUtils.chat("&a[EpicSet-Clans] >&cYou are using an old version of WorldGuard. Flag feature will be disabled."));
				Bukkit.getConsoleSender().sendMessage(CommonUtils.chat("&a[EpicSet-Clans] >&cInstall WorldGuard version 6.1.2 or above to use flags."));
				wgHook=new UnsupportedWG();
			}

			wgHook.attemptLoad();
		}

		//load all files
		files.put("formulas", new YMLManager(this, "Formulas",null));
		files.put("commands", new YMLManager(this, "Commands",null));
		files.put("messages", new YMLManager(this, "Messages",null));
		files.put("config", new YMLManager(this, "Config",null));
		files.put("duelsettings", new YMLManager(this, "DuelSettings",null));
		files.put("manage", new YMLManager(this, "Manage","guis"));
		files.put("shop", new YMLManager(this, "Shop", "guis"));
		files.put("stats", new YMLManager(this, "Stats", "guis"));
		files.put("top", new YMLManager(this, "Top", "guis"));
		files.put("antifarming", new YMLManager(this, "AntiFarming",null));
		files.put("monthlyrewards", new YMLManager(this, "MonthlyRewards",null));

		arenaData = new DuelArenaData(this);
		costsManager = new CostsManager(this);
		if(!files.get("config").getBoolean("mysql.enabled")) {
			savingManager = new SavingManager(this, new LocalSaving(this, clanManager, topManager = new ClanTopManager(this, savingManager)));
		} else {
			savingManager = new SavingManager(this, new MySqlSaving(this, clanManager, topManager = new ClanTopManager(this, savingManager), new MySqlManager()));
		}
		clanManager = new EpicClanManager(this, costsManager, savingManager, topManager);
		antiFarm = new AntiKillFarm(this, clanManager);
		duelManager = new ClanDuelManager(this, clanManager, arenaData, new ClanDuelData(this, clanManager, arenaData));
		arenaCommand = new ArenaCommand(this, clanManager, duelManager);
		chatHandle = new ClanChatHandle(this, clanManager);
	}
	@Override
	public void onEnable() 
	{
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if(rsp==null)
		{
			Bukkit.getConsoleSender().sendMessage(CommonUtils.chat("&a[EpicSet-Clans] >&cNo Economy plugin is hooked to Vaults."));
			Bukkit.getConsoleSender().sendMessage(CommonUtils.chat("&a[EpicSet-Clans] >&aPlugin Disabled."));
			this.setEnabled(false);
		}
		else
		{
			//register economy
			eco = rsp.getProvider();

			antiFarm.setup();
			savingManager.setup();
			topManager.setup();
			duelManager.setup();

			//setup commands
			ClanCommand command=new ClanCommand(this, clanManager, duelManager);
			command.setup();
			getCommand("clan").setExecutor(command);
			getCommand("clan").setTabCompleter(command);

			ClanAdminCommand adminCommand = new ClanAdminCommand(arenaCommand, this, clanManager, duelManager);
			adminCommand.setup();
			getCommand("clanadmin").setExecutor(adminCommand);
			getCommand("clanadmin").setTabCompleter(adminCommand);

			//load all guis
			guis.put("manage", new ClanManage(this, clanManager, duelManager, topManager));
			guis.put("shop", new ClanShop(this, clanManager, duelManager, topManager));
			guis.put("stats", new ClanStats(this, clanManager, duelManager, topManager));
			guis.put("top", new ClanTop(this, clanManager, duelManager, topManager));
			guis.put("duel", new ClanDuel(this, clanManager, duelManager, topManager));
			//register listeners
			Bukkit.getPluginManager().registerEvents(new BukkitListeners(
				this,
				arenaCommand,
				duelManager,
				clanManager,
				wgHook,
				savingManager,
				antiFarm
			), this);
			if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
				new PapiHook(this, clanManager, topManager).setup();
		   }

			if (Bukkit.getPluginManager().isPluginEnabled("NametagEdit")) {
				Bukkit.getPluginManager().registerEvents(new NameTagEditHook(this), this);
		   }
			
			monthlyRewardsManager=new MonthlyRewardsManager(files.get("monthlyrewards"), this, clanManager, topManager);
			updateCosts();
		}
	}
	@Override
	public void onDisable() 
	{
		Bukkit.getScheduler().cancelTasks(this);
		if(eco!=null)
		{
			//save all data
			for(EpicClanData data:savingManager.getAllClans())
			{
				((ClanVaultsData) data.getVaults()).saveAll();
				data.saveFast();
			}
			//stop all duels
			for(ClanDuelData duel:duelManager.getRunningDuels())
				duel.endDuel();
			//save all arenas
			for(DuelArenaData arena:duelManager.getAllArenas().values())
				arena.saveToFile();
			//save all kits
			duelManager.saveAllKits();
			
			resetMeta();
		}
	}
	private void resetMeta() 
	{
		for(Player player:Bukkit.getOnlinePlayers())
		{
			if(player.hasMetadata("EpicDuel"))
				player.removeMetadata("EpicDuel", this);
			if(player.hasMetadata("EpicDuelRequest"))
				player.removeMetadata("EpicDuelRequest", this);
			if(player.hasMetadata("EpicDuelRequestSize"))
				player.removeMetadata("EpicDuelRequestSize", this);
			if(player.hasMetadata("EpicClan"))
				player.removeMetadata("EpicClan", this);
			if(player.hasMetadata("EpicClanAlly"))
				player.removeMetadata("EpicClanAlly", this);
			if(player.hasMetadata("EpicClanTruce"))
				player.removeMetadata("EpicClanTruce", this);
			if(player.hasMetadata("EpicSetClanChat"))
				player.removeMetadata("EpicSetClanChat", this);
        	if(player.hasMetadata("EpicChatSpy"))
				player.removeMetadata("EpicChatSpy", this);
        	if(player.hasMetadata("EpicSetKilled"))
				player.removeMetadata("EpicSetKilled", this);
    		if(player.hasMetadata("EpicSetClanTimer"))
    			player.removeMetadata("EpicSetClanTimer", this);
    		if(player.hasMetadata("EpicClanDelete"))
    			player.removeMetadata("EpicClanDelete", this);
    		
		}
	}
	
	public ClanDuelManager getClanDuelManager() {
		return this.duelManager;
	}
	public ClanManager getClanManager() {
		return this.clanManager;
	}
	public ClanTopManager getClanTopManager() {
		return this.topManager;
	}
	public MonthlyRewardsManager getMonthlyRewardManager()
	{
		return monthlyRewardsManager;
	}
	public boolean isCommandOpen(String cmd)
	{
		return files.get("commands").getFile().getBoolean(cmd);
	}
	public Economy getEconomy()
	{
		return eco;
	}
	public Map<String, Guiable> getGuis()
	{
		return guis;
	}
	public YMLManager getFormulas()
	{
		return files.get("formulas");
	}
	public YMLManager getDuelSettings()
	{
		return files.get("duelsettings");
	}
	public YMLManager getMessages()
	{
		return files.get("messages");
	}
	public YMLManager getSettings()
	{
		return files.get("config");
	}
	public Map<String, YMLManager> getFiles()
	{
		return files;
	}
	public List<String> getAllFiles(String location) 
	{
		File folder = new File(this.getDataFolder()+location);
        String[] fileNames = folder.list();
        ArrayList<String> names=new ArrayList<String>();
        if(fileNames!=null)
        {
        	for(String s:fileNames)
        	{
    			names.add(s.replace(".yml", ""));
        	}
        }
        return names;
	}
	public ClanChatHandle getChatHandle() {
		return chatHandle;
	}

	public void updateCosts()
	{
		costsManager.setup();
	}
}
