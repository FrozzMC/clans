package me.backstabber.epicsetclans.hooks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.EpicSetClans;
import me.backstabber.epicsetclans.api.data.ClanData;
import me.backstabber.epicsetclans.clanhandles.data.ClanPlayersData;
import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;
import me.backstabber.epicsetclans.clanhandles.manager.ClanTopManager;
import me.backstabber.epicsetclans.clanhandles.manager.EpicClanManager;
import me.backstabber.epicsetclans.utils.CommonUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PapiHook extends PlaceholderExpansion {
	private EpicSetClans plugin;
	private EpicClanManager clanManager;
	private ClanTopManager topManager;
	
	public PapiHook(final EpicSetClans plugin, final EpicClanManager clanManager, final ClanTopManager topManager) {
		this.plugin = plugin;
		this.clanManager = clanManager;
		this.topManager = topManager;
	}
	
	public void setup() 
	{
		ConsoleCommandSender console = Bukkit.getConsoleSender();
		console.sendMessage(CommonUtils.chat("&a[EpicSet-Clans] >&eFound PlaceholderAPI. &rRegistering placehlders."));

		this.register();
	}
	@Override
	public String onPlaceholderRequest(Player player, String identifier) 
	{
		if(identifier.equals("total_clans"))
			return CommonUtils.getDecimalFormatted(clanManager.getAllClans().size());
		if(identifier.startsWith("top_")) {
			int position=0;
			try {
				position=Integer.valueOf(identifier.substring(4,6));
			}catch (Exception e) {
				return "";
			}
			EpicClanData clan=(EpicClanData) topManager.getClan(position);
			if(clan==null)
				return "";
			identifier=identifier.substring(6);
			switch(identifier.toLowerCase()) {
			case ("_leader"):
				return clan.getClanLeader();
			case ("_name"):
				return clan.getClanName();
			case ("_name_raw"):
				return clan.getClanNameRaw();
			case ("_respect"):
				if(clan.getClanRespect()<0)
					return "-"+CommonUtils.getDecimalFormatted(Math.abs(clan.getClanRespect()));
				else
					return CommonUtils.getDecimalFormatted(clan.getClanRespect());
			case ("_respect_raw"):
				return String.valueOf(clan.getClanRespect());
			case ("_respect_formatted"):
				if(clan.getClanRespect()<0)
					return "-"+CommonUtils.getPrefixFormatted(Math.abs(clan.getClanRespect()));
				else
					return CommonUtils.getPrefixFormatted(clan.getClanRespect());
			case ("_balance"):
				return CommonUtils.getDecimalFormatted(clan.getClanBalance());
			case ("_balance_raw"):
				return clan.getClanBalance()+"";
			case ("_balance_formatted"):
				return CommonUtils.getPrefixFormatted(clan.getClanBalance());
			case ("_points"):
				return CommonUtils.getDecimalFormatted(clan.getClanPoints());
			case ("_points_raw"):
				return String.valueOf(clan.getClanPoints());
			case ("_points_formatted"):
				return CommonUtils.getPrefixFormatted(clan.getClanPoints());
			default:
				return "";
			}
		}
		if(!clanManager.isInClan(player.getName()))
			return "";
		EpicClanData clan = (EpicClanData) clanManager.getClanData(player.getName());
		if(identifier.equals("leader"))
			return clan.getClanLeader();
		if(identifier.equals("clan"))
			return clan.getClanName();
		if(identifier.equals("clan_raw"))
			return clan.getClanNameRaw();
		if(identifier.equals("clan_tag"))
			return CommonUtils.chat(clan.getClanTag());
		if(identifier.equals("clan_tag_raw"))
			return ChatColor.stripColor(CommonUtils.chat(clan.getClanTag()));
		if(identifier.equals("tag"))
			return CommonUtils.chat(clan.getMemberData(player.getName()).getTag());
		if(identifier.equals("tag_raw"))
			return ChatColor.stripColor(CommonUtils.chat(clan.getMemberData(player.getName()).getTag()));
		if (identifier.equals("comment"))
			return CommonUtils.chat(((ClanPlayersData) clan.getMemberData(player.getName())).getInfo());
		if (identifier.equals("comment_raw"))
			return ChatColor.stripColor(CommonUtils.chat(((ClanPlayersData) clan.getMemberData(player.getName())).getInfo()));
		if(identifier.equals("position"))
			return String.valueOf(topManager.getPosition(clan));
		if(identifier.equals("respect"))
		{
			if(clan.getClanRespect()<0)
				return "-"+CommonUtils.getDecimalFormatted(Math.abs(clan.getClanRespect()));
			else
				return CommonUtils.getDecimalFormatted(clan.getClanRespect());
		}
		if(identifier.equals("respect_raw"))
			return String.valueOf(clan.getClanRespect());
		if(identifier.equals("respect_formatted"))
		{
			if(clan.getClanRespect()<0)
				return "-"+CommonUtils.getPrefixFormatted(Math.abs(clan.getClanRespect()));
			else
				return CommonUtils.getPrefixFormatted(clan.getClanRespect());
		}
		if(identifier.equals("points"))
			return CommonUtils.getDecimalFormatted(clan.getMemberData(player.getName()).getPoints());
		if(identifier.equals("points_raw"))
			return String.valueOf(clan.getMemberData(player.getName()).getPoints());
		if(identifier.equals("points_formatted"))
			return CommonUtils.getPrefixFormatted(clan.getMemberData(player.getName()).getPoints());
		if(identifier.equals("balance"))
			return CommonUtils.getDecimalFormatted(clan.getClanBalance());
		if(identifier.equals("balance_raw"))
			return clan.getClanBalance()+"";
		if(identifier.equals("balance_formatted"))
			return CommonUtils.getPrefixFormatted(clan.getClanBalance());
		if(identifier.equals("kills"))
			return CommonUtils.getDecimalFormatted(clan.getMemberData(player.getName()).getKills());
		if(identifier.equals("deaths"))
			return CommonUtils.getDecimalFormatted(clan.getMemberData(player.getName()).getDeaths());
		if(identifier.equals("allies")) {
			String allies="";
			for(ClanData data:clan.getClanAllies())
				allies=allies+","+data.getClanName();
			return allies.substring(1);
		}
		if(identifier.equals("truces")) {
			String truces="";
			for(ClanData data:clan.getClanTruce())
				truces=truces+","+data.getClanName();
			return truces.substring(1);
		}
		return identifier;
  }
	@Override
	public boolean canRegister() {
		return true;
	}
	@Override
	public String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}
	@Override
	public String getIdentifier() {
		return "epicset-clans";
	}
	@Override
	public String getVersion() {
		return plugin.getDescription().getVersion();
	}
}
