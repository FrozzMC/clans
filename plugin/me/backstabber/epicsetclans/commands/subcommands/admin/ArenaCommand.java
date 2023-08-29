package me.backstabber.epicsetclans.commands.subcommands.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.StringUtil;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.clanhandles.data.DuelArenaData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanDuelManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.commands.subcommands.AdminCommands;
import me.backstabber.epicsetclans.messager.JSONMessage;
import me.backstabber.epicsetclans.utils.CommonUtils;
import me.backstabber.epicsetclans.utils.Cuboid;
import me.backstabber.epicsetclans.utils.materials.EpicMaterials;
import me.backstabber.epicsetclans.utils.materials.UMaterials;
import me.backstabber.epicsetclans.utils.particles.FastParticle;
import me.backstabber.epicsetclans.utils.particles.ParticleType;

public class ArenaCommand extends AdminCommands
{
	private String name;
	private Map<Player, ArenaWizzard> wizzards=new HashMap<Player, ArenaCommand.ArenaWizzard>();
	public ArenaCommand(EpicSetClans plugin,EpicClanManager clanManager,ClanDuelManager duelManager) 
	{
		super(plugin, clanManager, duelManager);
		this.name="arena";
	}
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player=event.getPlayer();
		if(!wizzards.containsKey(player))
			return;
		if(!event.getAction().equals(Action.LEFT_CLICK_BLOCK))
			return;
		event.setCancelled(true);
		Location location=event.getClickedBlock().getLocation();
		ArenaWizzard arenaWizzard = wizzards.get(player);
		if(arenaWizzard.getCuboidAlpha()==null)
		{
			arenaWizzard.addCuboidAlpha(location);
			JSONMessage.create(CommonUtils.chat("&bClanAdmin &7: &fFirst corner added.")).send(player);
			JSONMessage.create(CommonUtils.chat("&bClanAdmin &7: &fLeft-click a block or &lClick Here&f")).tooltip(CommonUtils.chat("&7Click to add second corner.")).runCommand("/clanadmin arena alOhdWku").send(player);
			return;
		}
		if(arenaWizzard.getCuboidBravo()==null)
		{
			arenaWizzard.addCuboidBravo(location);
			JSONMessage.create(CommonUtils.chat("&bClanAdmin &7: &fSecond Corner Added.")).send(player);
			JSONMessage.create(CommonUtils.chat("&bClanAdmin &7: &fLeft-click a block or &lClick Here&f")).tooltip(CommonUtils.chat("&7Click to add spawn.")).runCommand("/clanadmin arena alOhdWku").send(player);
			return;
		}
		if(arenaWizzard.getSpawnAlpha()==null)
		{
			arenaWizzard.setSpawnAlpha(location.add(0,1,0));
			JSONMessage.create(CommonUtils.chat("&bClanAdmin &7: &fFirst spawn added.")).send(player);
			JSONMessage.create(CommonUtils.chat("&bClanAdmin &7: &fLeft-click a block or &lClick Here&f")).tooltip(CommonUtils.chat("&7Click to add spawn.")).runCommand("/clanadmin arena alOhdWku").send(player);
			return;
		}
		if(arenaWizzard.getSpawnBravo()==null)
		{
			arenaWizzard.setSpawnBravo(location.add(0,1,0));
			JSONMessage.create(CommonUtils.chat("&bClanAdmin &7: &fSecond spawn added.")).send(player);
			JSONMessage.create(CommonUtils.chat("&bClanAdmin &7: &fType /clan arena finish or &lClick Here&f")).tooltip(CommonUtils.chat("&7Click to end wizzard.")).runCommand("/clanadmin arena finish").send(player);
			return;
		}
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onCommandByPlayer(Player sender, String[] sub) 
	{
		if(sub.length==2&&sub[1].equalsIgnoreCase("list"))
		{
			Set<String> arenas = duelManager.getAllArenas().keySet();
			sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fFollowing arenas are loaded:"));
			int index=1;
			for(String arena:arenas)
			{
				sender.sendMessage(CommonUtils.chat("&7"+index+"&f) "+arena )); 
				index++;
			}
			return;
		}
		if(sub.length==3&&sub[1].equalsIgnoreCase("setitem"))
		{
			String arenaName=sub[2];
			if(duelManager.getArena(arenaName)!=null)
			{
				DuelArenaData arena = duelManager.getArena(arenaName);
				ItemStack item=sender.getInventory().getItemInHand();
				if(item==null||item.getType().equals(EpicMaterials.valueOf(UMaterials.AIR).getMaterial()))
				{
					sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fPlease hold an item to set."));
					return;
				}
				arena.setItem(item.clone());
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSet "+CommonUtils.getItemName(item)+"&f as arena item."));
				return;
			}
			else
			{
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fArena not found."));
				return;
			}
		}
		if(sub.length==3&&sub[1].equalsIgnoreCase("delete"))
		{
			String arenaName=sub[2];
			if(duelManager.getArena(arenaName)!=null)
			{
				DuelArenaData arena = duelManager.getArena(arenaName);
				duelManager.deleteArena(arena);
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fDeleted arena "+arenaName+" sucessfully"));
				return;
			}
			else
			{
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fArena not found."));
				return;
			}
		}
		if(sub.length==3&&sub[1].equalsIgnoreCase("create"))
		{
			if(this.wizzards.containsKey(sender))
			{
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fYou are already creating a arena. Type /clanadmin arena finish to end it."));
				return;
			}
			String arenaName=sub[2];
			if(duelManager.getArena(arenaName)==null)
			{
				this.wizzards.put(sender, new ArenaWizzard(sender, arenaName));
				JSONMessage.create(CommonUtils.chat("&bClanAdmin &7: &fArena creation started.")).send(sender);
				JSONMessage.create(CommonUtils.chat("&bClanAdmin &7: &fLeft-click a block or &lClick Here&f")).tooltip(CommonUtils.chat("&7Click to add corner.")).runCommand("/clanadmin arena alOhdWku").send(sender);
				return;
			}
			else
			{
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fArena by that name already exists."));
				return;
			}
		}
		if(sub.length==2&&sub[1].equals("alOhdWku"))
		{
			if(wizzards.containsKey(sender))
			{
				ArenaWizzard arenaWizzard = wizzards.get(sender);
				if(arenaWizzard.getCuboidAlpha()==null)
				{
					arenaWizzard.addCuboidAlpha(sender.getLocation());
					JSONMessage.create(CommonUtils.chat("&bClanAdmin &7: &fFirst corner added.")).send(sender);
					JSONMessage.create(CommonUtils.chat("&bClanAdmin &7: &fLeft-click a block or &lClick Here&f")).tooltip(CommonUtils.chat("&7Click to add second corner.")).runCommand("/clanadmin arena alOhdWku").send(sender);
					return;
				}
				if(arenaWizzard.getCuboidBravo()==null)
				{
					arenaWizzard.addCuboidBravo(sender.getLocation());
					JSONMessage.create(CommonUtils.chat("&bClanAdmin &7: &fSecond Corner Added.")).send(sender);
					JSONMessage.create(CommonUtils.chat("&bClanAdmin &7: &fLeft-click a block or &lClick Here&f")).tooltip(CommonUtils.chat("&7Click to add spawn.")).runCommand("/clanadmin arena alOhdWku").send(sender);
					return;
				}
				if(arenaWizzard.getSpawnAlpha()==null)
				{
					arenaWizzard.setSpawnAlpha(sender.getLocation());
					JSONMessage.create(CommonUtils.chat("&bClanAdmin &7: &fFirst spawn added.")).send(sender);
					JSONMessage.create(CommonUtils.chat("&bClanAdmin &7: &fLeft-click a block or &lClick Here&f")).tooltip(CommonUtils.chat("&7Click to add spawn.")).runCommand("/clanadmin arena alOhdWku").send(sender);
					return;
				}
				if(arenaWizzard.getSpawnBravo()==null)
				{
					arenaWizzard.setSpawnBravo(sender.getLocation());
					JSONMessage.create(CommonUtils.chat("&bClanAdmin &7: &fSecond spawn added.")).send(sender);
					JSONMessage.create(CommonUtils.chat("&bClanAdmin &7: &fType /clan arena finish or &lClick Here&f")).tooltip(CommonUtils.chat("&7Click to end wizzard.")).runCommand("/clanadmin arena finish").send(sender);
					return;
				}
			}
			return;
		}
		if(sub.length==2&&sub[1].equalsIgnoreCase("finish"))
		{
			if(wizzards.containsKey(sender))
			{
				ArenaWizzard arenaWizzard = wizzards.get(sender);
				if(arenaWizzard.trySave())
				{
					this.wizzards.remove(sender);
					sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fSucessfully create a new arena."));
					return;
				}
				else
				{
					this.wizzards.remove(sender);
					sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fArena creation unsucessfull"));
					return;
				}
			}
			else
			{
				sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fNothing to finish"));
				return;
			}
		}
		sender.sendMessage(CommonUtils.chat("&bClanAdmin &7: &fUsage:"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin arena list   &7List all arenas"));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin arena setitem [arenaName]   &7Set display item for arena."));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin arena delete [arenaName]   &7Delete a arena."));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin arena create [arenaName]   &7Create a new arena."));
		sender.sendMessage(CommonUtils.chat("&7- &f/clanadmin arena finish   &7Finish arena creation."));
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
		if(length==1)
			return new ArrayList<>();
		if(length==2)
		{
			List<String> result=new ArrayList<>();
			List<String> names=new ArrayList<>();
			names.add("list");
			names.add("setitem");
			names.add("delete");
			names.add("create");
			names.add("finish");
			StringUtil.copyPartialMatches(hint, names, result);
			Collections.sort(result);
	        return result;
		}
		if(length==3)
		{
			List<String> result=new ArrayList<>();
			List<String> names=new ArrayList<>();
			for(String arenaName:duelManager.getAllArenas().keySet())
				names.add(arenaName);
			StringUtil.copyPartialMatches(hint, names, result);
			Collections.sort(result);
	        return result;
		}
		return new ArrayList<>();
	}
	public class ArenaWizzard
	{
		private Player creator;
		private String arenaName;
		private Location cuboidAlpha;
		private Location cuboidBravo;
		private Location spawnAlpha;
		private Location spawnBravo;
		private BukkitTask particle;
		public ArenaWizzard(Player creator,String arenaName)
		{
			this.creator=creator;
			this.arenaName=arenaName;
			particle=new BukkitRunnable() 
			{
				@Override
				public void run() 
				{
					if(cuboidAlpha!=null)
					{
						if(cuboidBravo!=null)
							spawnBoundaryParticles(creator,cuboidAlpha,cuboidBravo);
						else
							spawnBoundaryParticles(creator,cuboidAlpha,creator.getLocation());
					}
					spawnSpawnParticles();
				}
			}.runTaskTimer(plugin, 0, 5);
		}
	    protected void spawnSpawnParticles() 
	    {
			if(this.spawnAlpha!=null)
			{
		        Color color= CommonUtils.getColorFromName("green");
	            FastParticle.spawnParticle(creator, ParticleType.REDSTONE, spawnAlpha.clone().add(0,1,0), 1, color);
			}
			if(this.spawnBravo!=null)
			{
		        Color color= CommonUtils.getColorFromName("green");
	            FastParticle.spawnParticle(creator, ParticleType.REDSTONE, spawnBravo.clone().add(0,1,0), 1, color);
			}
		}
		private void spawnBoundaryParticles(Player player, Location corner1, Location corner2)
		{
			//get min coords
	        double xMin = Math.min(corner1.getX(), corner2.getX());
	        double yMin = Math.min(corner1.getY(), corner2.getY());
	        double zMin = Math.min(corner1.getZ(), corner2.getZ());
	        //get max coords
	        double xMax = Math.max(corner1.getX(), corner2.getX());
	        double yMax = Math.max(corner1.getY(), corner2.getY());
	        double zMax = Math.max(corner1.getZ(), corner2.getZ());
	        Color color= CommonUtils.getColorFromName("orange");
	        //display particles for both locations while changing only 1 axis
	        for (double i = xMin; i <= xMax; i += 0.4) 
	        {
	            FastParticle.spawnParticle(player, ParticleType.REDSTONE, i, yMax, zMax, 1, color);
	            FastParticle.spawnParticle(player, ParticleType.REDSTONE, i, yMin, zMin, 1, color);
	            FastParticle.spawnParticle(player, ParticleType.REDSTONE, i, yMax, zMin, 1, color);
	            FastParticle.spawnParticle(player, ParticleType.REDSTONE, i, yMin, zMax, 1, color);
	        }
	        for (double i = yMin; i <= yMax; i += 0.4) {
	            FastParticle.spawnParticle(player, ParticleType.REDSTONE, xMax, i, zMax, 1, color);
	            FastParticle.spawnParticle(player, ParticleType.REDSTONE, xMin, i, zMin, 1, color);
	            FastParticle.spawnParticle(player, ParticleType.REDSTONE, xMin, i, zMax, 1, color);
	            FastParticle.spawnParticle(player, ParticleType.REDSTONE, xMax, i, zMin, 1, color);
	        }
	        for (double i = zMin; i <= zMax; i += 0.4) {
	            FastParticle.spawnParticle(player, ParticleType.REDSTONE, xMax, yMax, i, 1, color);
	            FastParticle.spawnParticle(player, ParticleType.REDSTONE, xMin, yMin, i, 1, color);
	            FastParticle.spawnParticle(player, ParticleType.REDSTONE, xMin, yMax, i, 1, color);
	            FastParticle.spawnParticle(player, ParticleType.REDSTONE, xMax, yMin, i, 1, color);
	        }
		}
		public void addCuboidAlpha(Location cuboidAlpha)
		{
			this.cuboidAlpha=cuboidAlpha;
		}
		public Location getCuboidAlpha()
		{
			return this.cuboidAlpha;
		}
		public void addCuboidBravo(Location cuboidBravo)
		{
			this.cuboidBravo=cuboidBravo;
		}
		public Location getCuboidBravo()
		{
			return this.cuboidBravo;
		}
		public void setSpawnAlpha(Location spawnAlpha)
		{
			this.spawnAlpha=spawnAlpha;
		}
		public Location getSpawnAlpha()
		{
			return this.spawnAlpha;
		}
		public void setSpawnBravo(Location spawnBravo)
		{
			this.spawnBravo=spawnBravo;
		}
		public Location getSpawnBravo()
		{
			return this.spawnBravo;
		}
		public String getArenaName()
		{
			return this.arenaName;
		}
		public boolean trySave()
		{
			if(this.cuboidAlpha!=null&&this.cuboidBravo!=null&&this.spawnAlpha!=null&&this.spawnBravo!=null)
			{
				duelManager.registerNewArena(arenaName, Cuboid.create(plugin,cuboidAlpha, cuboidBravo), spawnAlpha, spawnBravo);
				if(this.particle!=null)
					this.particle.cancel();
				return true;
			}
			return false;
		}
	}
}
