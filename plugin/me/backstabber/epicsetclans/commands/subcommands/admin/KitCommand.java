package me.backstabber.epicsetclans.commands.subcommands.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.data.KitsData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.AdminCommands;
import me.backstabber.epicsetclans.utils.CommonUtils;
import me.backstabber.epicsetclans.utils.materials.EpicMaterials;
import me.backstabber.epicsetclans.utils.materials.UMaterials;

public class KitCommand extends AdminCommands
{
	private String name;
	public KitCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager) 
	{
		super(plugin, clanManager, duelManager);
		this.name="kit";
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onCommandByPlayer(Player sender, String[] sub) 
	{
		if(sub.length==2&&sub[1].equalsIgnoreCase("list"))
		{
			List<KitsData> kits = duelManager.getAllKits();
			sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fFollowing kits are loaded:"));
			int index=1;
			for(KitsData kit:kits)
			{
				sender.sendMessage(CommonUtils.chat("&7"+index+"&f) "+kit.getName() )); 
				index++;
			}
			return;
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("delete"))
		{
			String kitName=sub[2];
			for(int i=3;i<sub.length;i++)
				kitName=kitName+" "+sub[i];
			if(duelManager.getKit(kitName)!=null)
			{
				KitsData kit = duelManager.getKit(kitName);
				duelManager.removeKit(kit);
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fDeleted kit "+kitName+" sucessfully"));
				return;
			}
			else
			{
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fKit not found."));
				return;
			}
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("create"))
		{
			String kitName=sub[2];
			for(int i=3;i<sub.length;i++)
				kitName=kitName+" "+sub[i];
			if(duelManager.getKit(kitName)==null)
			{
				List<ItemStack> items=new ArrayList<ItemStack>();
				for(ItemStack item:sender.getInventory().getContents())
					if(item!=null&&!item.getType().equals(EpicMaterials.valueOf(UMaterials.AIR).getMaterial()))
						items.add(item);
				duelManager.addKit(kitName, items);
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully create a new kit."));
				return;
			}
			else
			{
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fKit already exists."));
				return;
			}
		}
		if(sub.length>2&&sub[1].equalsIgnoreCase("setitem"))
		{
			String kitName=sub[2];
			for(int i=3;i<sub.length;i++)
				kitName=kitName+" "+sub[i];
			if(duelManager.getKit(kitName)!=null)
			{
				KitsData kit = duelManager.getKit(kitName);
				ItemStack item=sender.getInventory().getItemInHand();
				if(item==null||item.getType().equals(EpicMaterials.valueOf(UMaterials.AIR).getMaterial()))
				{
					sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlease hold an item to add."));
					return;
				}
				kit.setViewItem(item);
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSet "+CommonUtils.getItemName(item)+"&f as kit item."));
				return;
			}
			else
			{
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fKit not found."));
				return;
			}
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin kit list   &7List all kits"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin kit setitem [kitName]   &7Set display item for kit."));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin kit delete [kitName]   &7Delete a kit."));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin kit create [kitName]   &7Create a new kit."));
	}

	@Override
	public void onCommandByConsole(CommandSender sender, String[] sub) 
	{
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fThis command isnt supported by console yet."));
	}

	@Override
	public String getName() 
	{
		return this.name;
	}

	@Override
	public List<String> getAliases() 
	{
		List<String> aliases=new ArrayList<String>();
		aliases.add(name);
		return aliases;
	}

	@Override
	public List<String> getCompletor(int length, String hint) 
	{
		if(length==2)
		{
			List<String> result=new ArrayList<>();
			List<String> names=new ArrayList<>();
			names.add("list");
			names.add("setitem");
			names.add("delete");
			names.add("create");
			StringUtil.copyPartialMatches(hint, names, result);
			Collections.sort(result);
	        return result;
		}
		if(length==3)
		{
			List<String> result=new ArrayList<>();
			List<String> names=new ArrayList<>();
			for(KitsData kit:duelManager.getAllKits())
				names.add(kit.getName());
			StringUtil.copyPartialMatches(hint, names, result);
			Collections.sort(result);
	        return result;
		}
		return new ArrayList<>();
	}

}
