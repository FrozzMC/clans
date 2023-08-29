package me.backstabber.epicsetclans.api.data;

import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.backstabber.epicsetclans.clanhandles.data.EpicClanData;

/**
 * The main container for almost every clan related operation
 * @author Backstabber
 *
 */
public interface ClanData 
{
	/**
	 * Get the main handler for this clan. This will expose sensitive parts of the clan.
	 * Not recomended to use unless you know what you are doing.
	 * @return the Main handler of the clan
	 */
	public EpicClanData getHandle();
	/**
	 * Get the total duels won by this clan
	 * @return number of duels won
	 */
	public int getWonDuels();
	/**
	 * Get the total duels lost by this clan
	 * @return number of duels lost
	 */
	public int getLostDuels();
	/**
	 * Get the name of the clan
	 * This includes any chat modifiers (colors) used
	 * @return clan name
	 */
	public String getClanName();
	/**
	 * Get the raw name of the clan
	 * This will remove any chat modifiers in the name
	 * @return the raw clan name
	 */
	public String getClanNameRaw();
	/**
	 * Set the name for this clan
	 * @param new name
	 */
	public void setClanName(String name);
	/**
	 * Get the name of the clan leader
	 * @return name of leader
	 */
	public String getClanLeader();
	/**
	 * Get individual data for each clan members
	 * @return data for each member
	 */
	public Map<String, PlayerData> getMembersData();
	/**
	 * Get data for a specific clan member
	 * @param player
	 * @return data of the clan member
	 */
	public PlayerData getMemberData(OfflinePlayer player);
	public List<OfflinePlayer> getClanMembers();
	public void addClanMember(Player player);
	public void removeClanMember(Player player);
	public List<ClanData> getClanAllies();
	public List<ClanData> getClanTruce();
	public long getClanBalance();
	public void setClanBalance(long balance);
	public VaultsData getVaults();
	public BaseData getBases();
	public long getClanRespect();
	public long getClanPoints();
}
