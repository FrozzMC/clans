package me.backstabber.epicsetclans.clanhandles.manager;

import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.data.CostsData;

public class CostsManager {
	private EpicSetClans plugin;
	private HashMap<Integer, CostsData> costs=new HashMap<Integer, CostsData>();
	private CostsData defData;

	public CostsManager(final EpicSetClans plugin) {
		this.plugin = plugin;
	}

	public void setup()
	{
		ConfigurationSection file=plugin.getSettings().getFile().getConfigurationSection("creation-costs.groups");
		if(file==null)
			return;
		defData= new CostsData(plugin, plugin.getSettings(), "default");
		for(int i=0;i<=100;i++) 
		{
			if(file.isSet(i+""))
			{
				CostsData costData=new CostsData(plugin, plugin.getSettings(), String.valueOf(i));
				costs.put(i, costData);
			}
		}
		
	}
	public boolean apply(Player player)
	{
		for(int i=0;i<=100;i++)
			if(costs.containsKey(i)&&costs.get(i).isApplicable(player))
				return costs.get(i).apply(player);
		return defData.apply(player);
	}
}
