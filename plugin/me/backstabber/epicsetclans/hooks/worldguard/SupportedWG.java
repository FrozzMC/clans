package me.backstabber.epicsetclans.hooks.worldguard;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import me.backstabber.epicsetclans.utils.CommonUtils;

public class SupportedWG implements WorldguardHook
{
	private boolean canUse=false;
	private StateFlag IGNORE_CLANS;
	private StateFlag IGNORE_ALLIES;
	private StateFlag IGNORE_TRUCES;
	@Override
	public void attemptLoad()
	{
		canUse=true;
		Bukkit.getConsoleSender().sendMessage(CommonUtils.chat("&a[EpicSet-Clans] >&eFound WorldGuard.&rRegistering Flags."));
		WorldGuardPlugin wgpl = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
		FlagRegistry registry;
		if (wgpl != null) {
			try {
				registry = wgpl.getFlagRegistry();
			} catch (NoSuchMethodError e) {
				Bukkit.getConsoleSender().sendMessage(CommonUtils.chat("&a[EpicSet-Clans] >&cUnable to register Flags."));
				canUse=false;
				return;
			}
			Bukkit.getConsoleSender().sendMessage(CommonUtils.chat("&a[EpicSet-Clans] >&rRegistring Flag &eignore-clans&r."));
			try
			{
				StateFlag flag = new StateFlag("ignore-clans", false);
				registry.register(flag);
				IGNORE_CLANS = flag;
			}
			catch (FlagConflictException e)
			{
				
				Flag<?> existing = registry.get("ignore-clans");
				if (existing instanceof StateFlag)
				{
					IGNORE_CLANS = (StateFlag) existing;
				}
			}
			catch (IllegalStateException e)
			{
				Flag<?> existing = registry.get("ignore-clans");
				if (existing instanceof StateFlag)
				{
					IGNORE_CLANS = (StateFlag) existing;
				}
				else
				{
					Bukkit.getConsoleSender().sendMessage(CommonUtils.chat("&a[EpicSet-Clans] >&cUnable to register flag &eignore-clans."));
					canUse=false;
				}
			}
			Bukkit.getConsoleSender().sendMessage(CommonUtils.chat("&a[EpicSet-Clans] >&rRegistring Flag &eignore-allies&r."));
			try
			{
				StateFlag flag = new StateFlag("ignore-allies", false);
				registry.register(flag);
				IGNORE_ALLIES = flag;
			}
			catch (FlagConflictException e)
			{
				
				Flag<?> existing = registry.get("ignore-allies");
				if (existing instanceof StateFlag)
				{
					IGNORE_ALLIES = (StateFlag) existing;
				}
			}
			catch (IllegalStateException e)
			{
				Flag<?> existing = registry.get("ignore-allies");
				if (existing instanceof StateFlag)
				{
					IGNORE_ALLIES = (StateFlag) existing;
				}
				else
				{
					Bukkit.getConsoleSender().sendMessage(CommonUtils.chat("&a[EpicSet-Clans] >&cUnable to register flag &eignore-allies."));
					canUse=false;
				}
			}
			Bukkit.getConsoleSender().sendMessage(CommonUtils.chat("&a[EpicSet-Clans] >&rRegistring Flag &eignore-truce&r."));
			try
			{
				StateFlag flag = new StateFlag("ignore-truce", false);
				registry.register(flag);
				IGNORE_TRUCES = flag;
			}
			catch (FlagConflictException e)
			{
				
				Flag<?> existing = registry.get("ignore-truce");
				if (existing instanceof StateFlag)
				{
					IGNORE_TRUCES = (StateFlag) existing;
				}
			}
			catch (IllegalStateException e)
			{
				Flag<?> existing = registry.get("ignore-truce");
				if (existing instanceof StateFlag)
				{
					IGNORE_TRUCES = (StateFlag) existing;
				}
				else
				{
					Bukkit.getConsoleSender().sendMessage(CommonUtils.chat("&a[EpicSet-Clans] >&cUnable to register flag &eignore-truce."));
					canUse=false;
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean allowedClans(Location location)
	{
		if(canUse)
		{
			WorldGuardPlugin wgpl = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
			if (wgpl != null) {
				ApplicableRegionSet set = wgpl.getRegionManager(location.getWorld()).getApplicableRegions(location);
				
				return !set.allows(IGNORE_CLANS);
			}
		}
		return true;
	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean allowedAllies(Location location)
	{
		if(canUse)
		{
			WorldGuardPlugin wgpl = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
			if (wgpl != null) {
				ApplicableRegionSet set = wgpl.getRegionManager(location.getWorld()).getApplicableRegions(location);
				
				return !set.allows(IGNORE_CLANS) && !set.allows(IGNORE_ALLIES);
			}
		}
		return true;
	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean allowedTruce(Location location)
	{
		if(canUse)
		{
			WorldGuardPlugin wgpl = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
			if (wgpl != null) {
				ApplicableRegionSet set = wgpl.getRegionManager(location.getWorld()).getApplicableRegions(location);
				
				return !set.allows(IGNORE_CLANS) && !set.allows(IGNORE_TRUCES);
			}
		}
		return true;
	}
	
}
