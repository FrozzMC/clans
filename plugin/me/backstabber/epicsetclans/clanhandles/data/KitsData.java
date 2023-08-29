package me.backstabber.epicsetclans.clanhandles.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import me.backstabber.epicsetclans.clanhandles.manager.YMLManager;
import me.backstabber.epicsetclans.utils.materials.EpicMaterials;
import me.backstabber.epicsetclans.utils.materials.UMaterials;

public class KitsData 
{
	private String kitName;
	private List<ItemStack> kitItems=new ArrayList<ItemStack>();
	private ItemStack kitViewitem=EpicMaterials.valueOf(UMaterials.DIAMOND_SWORD).getItemStack();
	public static KitsData createNew(String kitName,List<ItemStack> kitItems)
	{
		KitsData data=new KitsData();
		data.kitName=kitName;
		data.kitItems=kitItems;
		return data;
	}
	public static KitsData load(ConfigurationSection section,String kitName)
	{
		KitsData data=new KitsData();
		data.kitName=kitName;
		if(section.isSet("view-item"))
			data.kitViewitem=section.getItemStack("view-item");
		if(section.isSet("kit-items"))
		{
			List<ItemStack> items=new ArrayList<>();
			for(String s:section.getConfigurationSection("kit-items").getKeys(false))
				items.add(section.getItemStack("kit-items."+s));
			data.kitItems=items;
		}
		return data;
	}
	public void setViewItem(ItemStack item)
	{
		this.kitViewitem=item.clone();
	}
	public ItemStack getViewItem()
	{
		return this.kitViewitem.clone();
	}
	public String getName()
	{
		return this.kitName;
	}
	public List<ItemStack> getKit()
	{
		return this.kitItems;
	}
	public void save(YMLManager file)
	{
		file.getFile().set(kitName+".view-item", this.kitViewitem);
		int index=1;
		for(ItemStack item:this.kitItems)
		{
			file.getFile().set(kitName+".kit-items."+index, item);
			index++;
		}
		file.save(true);
	}
	public void remove(YMLManager file)
	{
		file.getFile().set(kitName, null);
	}
}
