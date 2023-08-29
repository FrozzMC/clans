package me.backstabber.epicsetclans.api.data;



public interface PlayerData 
{
	public String getName();
	public int getKills();
	public void setKills(int value);
	public int getDeaths();
	public void setDeaths(int value);
	public int getPoints();
	public void setPoints(int value);
	public int getDuelsPlayed();
	public int getDuelsWon();
	public String getTag();
	public void setTag(String value);
	public String getComment();
	public void setComment(String value);
	public void addPermission(String perm);
	public void removePermission(String perm);
	public boolean hasPermission(String permission);
	public boolean isLeader();
}
