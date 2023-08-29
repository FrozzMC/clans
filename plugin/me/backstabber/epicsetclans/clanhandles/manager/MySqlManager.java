package me.backstabber.epicsetclans.clanhandles.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.backstabber.epicsetclans.enums.ClanNodes;
import me.backstabber.epicsetclans.enums.ClanPlayerNodes;


public class MySqlManager {
	
	private String host;
	private String port;
	private String database;
	private String username;
	private String password;
	private boolean isUseable=false;
	
	private Connection connection;

	public void create(String host,String port,String database,String username,String password) {
		this.host=host;
		this.port=port;
		this.database=database;
		this.username=username;
		this.password=password;
		openConnection();
	}
	public boolean isUseable() {
		return this.isUseable;
	}
	public boolean isClanLeader(String leader) {
		try {
			ResultSet r = connection
		        .createStatement()
		        .executeQuery(
		          "SELECT * FROM `clans` WHERE "+ClanNodes.CLAN_LEADER.node()+" = '" + 
		        		  leader + "'");
		      boolean contains = r.next();
		      r.close();
		      return contains;
	    } 
	    catch (SQLException|NullPointerException e) 
	    {
	    	return false;
	    } 
	}
	public boolean isInClan(String player) {
		try {
			ResultSet r = connection
		        .createStatement()
		        .executeQuery(
		          "SELECT * FROM `clanmembers` WHERE name = '" + 
		        		  player + "'");
		      boolean contains = r.next();
		      r.close();
		      return contains;
		    } 
		    catch (SQLException|NullPointerException e) 
		    {
		      e.printStackTrace();
		      return false;
		    } 
	}
	public String getLeader(String player) {
		try {
			ResultSet r = connection
		        .createStatement()
		        .executeQuery(
		          "SELECT * FROM `clanmembers` WHERE name = '" + 
		        		  player + "'");
		    if(r.next())
		    {
		    	return r.getString("leader");
		    }
		    r.close();
	    } 
	    catch (SQLException|NullPointerException e) 
	    {
	    	e.printStackTrace();
    	} 
		return "";
	}
	public boolean isClanName(String name) {
		String nameraw = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', name));
		try {
			ResultSet r = connection
		        .createStatement()
		        .executeQuery(
		          "SELECT * FROM `clans` WHERE "+ClanNodes.CLAN_NAME_RAW.node()+" = '" + 
		        		  nameraw + "'");
		      boolean contains = r.next();
		      r.close();
		      return contains;
	    } 
	    catch (SQLException|NullPointerException e) 
	    {
	    	return false;
	    } 
	}
	public void deleteClan(String leader) {
		FileConfiguration config=downloadClan(leader);
		if(config==null)
			return;
		if(!isUseable)
			openConnection();
		for(String player:config.getStringList(ClanNodes.CLAN_MEMBERS.node()))
		{
			try {
				connection.createStatement().execute("DELETE FROM `clanmembers` WHERE `clanmembers`.`name` = '"+player+"'");
			}
			catch(SQLException e) {
				
			}
		}
		leader="'"+config.getString(ClanNodes.CLAN_LEADER.node())+"'";
		try {
			connection.createStatement().execute("DELETE FROM `clans` WHERE `clans`.`"+ClanNodes.CLAN_LEADER.node()+"` = "+leader);
		}
		catch(SQLException e) {
			
		}
		
	}
	public List<String> getAllLeaders() {
		if(!isUseable)
			openConnection();
		List<String> names=new ArrayList<String>();
		try {
			ResultSet r = connection
			        .createStatement()
			        .executeQuery(
			          "SELECT * FROM `clans` ");
			while(r.next())
			{
				names.add(r.getString(ClanNodes.CLAN_LEADER.node()));
			}
			r.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return names;
	}
	public FileConfiguration downloadClan(String leader) {
		FileConfiguration file=new YamlConfiguration();
		if(!isUseable)
			openConnection();
		if(!isClanLeader(leader))
		{
			if(isInClan(leader))
				leader=getLeader(leader);
			else
				return null;
		}
		try {
			ResultSet r = connection
		        .createStatement()
		        .executeQuery(
		          "SELECT * FROM `clans` WHERE "+ClanNodes.CLAN_LEADER.node()+" = '" + 
		        		  leader + "'");
			if (r.next()) 
			{
				for(ClanNodes node:ClanNodes.values())
				{
					if(node.equals(ClanNodes.CLAN_VAULT)||node.equals(ClanNodes.CLAN_BASES))
						continue;
					if(node.value() instanceof List<?>)
						file.set(node.node(), deserialize(r.getString(node.node())));
					else if(node.value() instanceof String)
						file.set(node.node(), r.getString(node.node()));
					else if(node.value() instanceof Double)
						file.set(node.node(), (double)r.getInt(node.node()));
					else
						file.set(node.node(), r.getInt(node.node()));
				}
			}
			r.close();
		} 
	    catch (SQLException e) 
	    {
	    	e.printStackTrace();
	    } 
		for(String player:file.getStringList(ClanNodes.CLAN_MEMBERS.node()))
		{
			try {
				ResultSet r = connection
			        .createStatement()
			        .executeQuery(
			          "SELECT * FROM `clanmembers` WHERE `name` = '" + 
			        		  player + "'");
				if (r.next()) 
				{
					for(ClanPlayerNodes node:ClanPlayerNodes.values())
					{
						if(node.value() instanceof List<?>)
							file.set(node.node(player), deserialize(r.getString(node.node())));
						else if(node.value() instanceof String)
							file.set(node.node(player), r.getString(node.node()));
						else if(node.value() instanceof Double)
							file.set(node.node(player), (double)r.getInt(node.node()));
						else
							file.set(node.node(player), r.getInt(node.node()));
					}
				}
				r.close();
			} 
		    catch (SQLException e) 
		    {
		    	e.printStackTrace();
		    } 
		}
		return file;
	}
	public FileConfiguration downloadClan(String leader,FileConfiguration file) {
		if(!isUseable)
			openConnection();
		if(!isClanLeader(leader))
		{
			if(isInClan(leader))
				leader=getLeader(leader);
			else
				return null;
		}
		try {
			ResultSet r = connection
		        .createStatement()
		        .executeQuery(
		          "SELECT * FROM `clans` WHERE "+ClanNodes.CLAN_LEADER.node()+" = '" + 
		        		  leader + "'");
			if (r.next()) 
			{
				for(ClanNodes node:ClanNodes.values())
				{
					if(node.equals(ClanNodes.CLAN_VAULT)||node.equals(ClanNodes.CLAN_BASES)||node.equals(ClanNodes.CLAN_NAME_RAW))
						continue;
					if(node.value() instanceof List<?>)
						file.set(node.node(), deserialize(r.getString(node.node())));
					else if(node.value() instanceof String)
						file.set(node.node(), r.getString(node.node()));
					else if(node.value() instanceof Double)
						file.set(node.node(), (double)r.getInt(node.node()));
					else
						file.set(node.node(), r.getInt(node.node()));
				}
			}
			r.close();
		} 
	    catch (SQLException e) 
	    {
	    	e.printStackTrace();
	    } 
		for(String player:file.getStringList(ClanNodes.CLAN_MEMBERS.node()))
		{
			try {
				ResultSet r = connection
			        .createStatement()
			        .executeQuery(
			          "SELECT * FROM `clanmembers` WHERE `name` = '" + 
			        		  player + "'");
				if (r.next()) 
				{
					for(ClanPlayerNodes node:ClanPlayerNodes.values())
					{
						if(node.value() instanceof List<?>)
							file.set(node.node(player), deserialize(r.getString(node.node())));
						else if(node.value() instanceof String)
							file.set(node.node(player), r.getString(node.node()));
						else if(node.value() instanceof Double)
							file.set(node.node(player), (double)r.getInt(node.node()));
						else
							file.set(node.node(player), r.getInt(node.node()));
					}
				}
				r.close();
			} 
		    catch (SQLException e) 
		    {
		    	e.printStackTrace();
		    } 
		}
		return file;
	}
	public void uploadClan(FileConfiguration file) {
		for(ClanNodes clanNode :ClanNodes.values())
			if(!file.isSet(clanNode.node()))
				file.set(clanNode.node(), clanNode.value());
		for(String player:file.getStringList(ClanNodes.CLAN_MEMBERS.node()))
			for(ClanPlayerNodes node:ClanPlayerNodes.values())
				if(!file.isSet(node.node(player)))
					file.set(node.node(player), node.value());
		FileConfiguration config=file;
		if(!isUseable)
			openConnection();
		try {
			String leader="'"+config.getString(ClanNodes.CLAN_LEADER.node())+"'";
			String nameraw="'"+config.getString(ClanNodes.CLAN_NAME.node())+"'";
			nameraw=ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', nameraw));
			String columns=ClanNodes.CLAN_LEADER.node()+","+ClanNodes.CLAN_NAME_RAW.node();
			String values=leader+","+nameraw;
			
			try {
				connection.createStatement().execute("DELETE FROM `clans` WHERE `clans`.`"+ClanNodes.CLAN_LEADER.node()+"` = "+leader);
			}
			catch(SQLException e) {
				
			}
			
			
			for(ClanNodes node:ClanNodes.values())
			{
				if(node.equals(ClanNodes.CLAN_LEADER)||node.equals(ClanNodes.CLAN_NAME_RAW)||node.equals(ClanNodes.CLAN_VAULT)||node.equals(ClanNodes.CLAN_BASES))
					continue;
				String data;
				if(node.value() instanceof List<?>)
					data=serialize(config.getStringList(node.node()));
				else if(node.value() instanceof String)
					data="'"+config.getString(node.node())+"'";
				else
					data=String.valueOf(config.get(node.node()));
				columns=columns+" , "+node.node();
				values=values+" , "+data;
			}
			String statement="INSERT INTO `clans` ("+columns+") VALUES ("+values+")";
			connection.createStatement().execute(statement);
			for(String player:config.getStringList(ClanNodes.CLAN_MEMBERS.node()))
			{
				try {
					connection.createStatement().execute("DELETE FROM `clanmembers` WHERE `clanmembers`.`name` = '"+player+"'");
				}
				catch(SQLException e) {
					
				}
				columns="name,leader";
				values="'"+player+"',"+leader;
				for(ClanPlayerNodes node:ClanPlayerNodes.values())
				{
					String data;
					if(node.value() instanceof List<?>)
						data=serialize(config.getStringList(node.node(player)));
					else if(node.value() instanceof String)
						data="'"+config.getString(node.node(player))+"'";
					else
						data=String.valueOf(config.get(node.node(player)));
					columns=columns+" , "+node.node();
					values=values+" , "+data;
				}
				statement="INSERT INTO `clanmembers` ("+columns+") VALUES ("+values+")";
				connection.createStatement().execute(statement);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void createTable() {
		if(!isUseable)
			openConnection();
		try {
			connection.createStatement().execute(
					"CREATE TABLE IF NOT EXISTS `clans` (\n"
					+ "`"+ClanNodes.CLAN_LEADER.node()+"` varchar(36) NOT NULL,\n"
					+ "`"+ClanNodes.CLAN_NAME_RAW.node()+"` varchar(36) ,\n"
					+ "`"+ClanNodes.CLAN_NAME.node()+"` varchar(36) ,\n"
					+ "`"+ClanNodes.CLAN_MEMBERS.node()+"` varchar(600),\n"
					+ "`"+ClanNodes.CLAN_ALLIES.node()+"` varchar(600) ,\n"
					+ "`"+ClanNodes.CLAN_TRUCES.node()+"` varchar(600) ,\n"
					+ "`"+ClanNodes.CLAN_BALANCE.node()+"` BIGINT(50) unsigned ,\n"
					
					
					+ "`"+ClanNodes.DUELS_WON.node()+"` int(11) unsigned ,\n"
					+ "`"+ClanNodes.DUELS_LOST.node()+"` int(11) unsigned ,\n"

					+ "`"+ClanNodes.UPGRADE_ALLIES.node()+"` int(11) unsigned ,\n"
					+ "`"+ClanNodes.UPGRADE_DUELS.node()+"` int(11) unsigned ,\n"
					+ "`"+ClanNodes.UPGRADE_HOME.node()+"` int(11) unsigned ,\n"
					+ "`"+ClanNodes.UPGRADE_MEMBERS.node()+"` int(11) unsigned ,\n"
					+ "`"+ClanNodes.UPGRADE_VAULTS.node()+"` int(11) unsigned ,\n"

					+ "`"+ClanNodes.RESPECT_ADITION.node()+"` int(11) unsigned ,\n"
					+ "`"+ClanNodes.RESPECT_SUBTRACTION.node()+"` int(11) unsigned ,\n"
					
					+ "`"+ClanNodes.RENAME_TIME.node()+"` int(11) unsigned ,\n"
					+ "`"+ClanNodes.CLAN_TAG.node()+"` varchar(11) ,\n"
					+ "PRIMARY KEY  (`leader`)\n)"
					);
			connection.createStatement().execute(
					"CREATE TABLE IF NOT EXISTS `clanmembers` (\n"
					+ "`name` varchar(255) NOT NULL,\n"
					+ "`leader` varchar(255) NOT NULL,\n"
					+ "`"+ClanPlayerNodes.CLAN_DEATHS.node()+"` int(11) unsigned,\n"
					+ "`"+ClanPlayerNodes.CLAN_INFO.node()+"` varchar(255) ,\n"
					+ "`"+ClanPlayerNodes.CLAN_KILLS.node()+"` int(11) ,\n"
					+ "`"+ClanPlayerNodes.CLAN_POINTS.node()+"` int(11) ,\n"
					+ "`"+ClanPlayerNodes.CLAN_PERMS.node()+"` varchar(600) ,\n"
					+ "`"+ClanPlayerNodes.CLAN_TAG.node()+"` varchar(255) ,\n"
					+ "`"+ClanPlayerNodes.DUELS_PLAYED.node()+"` int(11) ,\n"
					+ "`"+ClanPlayerNodes.DUELS_WON.node()+"` int(11) ,\n"
					+ "PRIMARY KEY  (`name`)\n)"
					);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void openConnection() {
		
	    try 
	    {
	    	connection = DriverManager.getConnection("jdbc:mysql://" + 
	          host + ":" + 
	          port + "/" + 
	          database, 
	          username, 
	          password);
	    	isUseable=true;
	    } 
	    catch (SQLException |NullPointerException e) 
	    {
	      e.printStackTrace();
	      isUseable=false;
	    }
	}
	public void closeConnection() {
		try 
	    {
	      if (!connection.isClosed() || connection != null)
	        connection.close(); 
	    } 
		catch (SQLException e) 
	    {
	      e.printStackTrace();
	    } 
		isUseable=false;
	}
	private String serialize(List<String> list) {
		String serialized="";
		for(String s:list)
			serialized=serialized+":"+s;
		if(serialized.length()>1)
			serialized=serialized.substring(1);
		return "'"+serialized+"'";
	}
	private List<String> deserialize(String string) {
		List<String> deserialized=new ArrayList<String>();
		for(String s:string.split(":"))
			deserialized.add(s);
		return deserialized;
	}
}
