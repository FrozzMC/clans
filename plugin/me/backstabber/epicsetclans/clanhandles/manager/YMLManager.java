package me.backstabber.epicsetclans.clanhandles.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteStreams;



public class YMLManager 
{
	
	public void delete()
	{
		loc.delete();
	}
	
	public void set(String node,Object object)
	{
		file.set(node, object);
	}
	public boolean isSet(String string)
	{
		return file.isSet(string);
	}
	public boolean getBoolean(String string)
	{
		return file.getBoolean(string);
	}
	public String getString(String string)
	{
		return file.getString(string);
	}
	public int getInt(String string)
	{
		return file.getInt(string);
	}
	public List<String> getStringList(String string)
	{
		return file.getStringList(string);
	}
	public boolean save()
	{
		try 
		{
			file.save(loc);
		} 
		catch (IOException e) 
		{
			return false;
		}
		return true;
	}
	public void save(File loc2) 
	{
		try {
			file.save(loc2);
		} catch (IOException e) 
		{
			
			e.printStackTrace();
		}
	}
	private JavaPlugin plugin;
	private FileConfiguration file;
	private File loc;
	private String internalName;
	private String internalLocation;
	private String externalName;
	private String externalLocation;
	public YMLManager(JavaPlugin plugin,String name)
	{
		this.plugin=plugin;
		this.internalName=name;
		this.externalName=name;
		this.internalLocation=null;
		this.externalLocation=null;
		load();
	}
	public YMLManager(JavaPlugin plugin,String name,String location)
	{
		this.plugin=plugin;
		this.internalName=name;
		this.externalName=name;
		this.internalLocation=location;
		this.externalLocation=location;
		load();
	}
	public YMLManager(JavaPlugin plugin,String name,String location,String newName)
	{
		this.plugin=plugin;
		this.internalName=name;
		this.externalName=newName;
		this.internalLocation=location;
		this.externalLocation=location;
		load();
	}
	public YMLManager(JavaPlugin plugin,String name,String location,String newName,String newLocation)
	{
		this.plugin=plugin;
		this.internalName=name;
		this.externalName=newName;
		this.internalLocation=location;
		this.externalLocation=newLocation;
		load();
	}
	public static boolean isReadable(File file)
	{
		YamlConfiguration yamlConfiguration = new YamlConfiguration();
	    try 
	    {
	      yamlConfiguration.load(file);
	      return true;
	    } 
	    catch (IOException e) 
	    {
	      return false;
	    } 
	    catch (InvalidConfigurationException e) 
	    {
	      return false;
	    } 
	}
	public void addComments(Map<Integer, String> comments)
	{
		save(true);
		//load all data from file
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(loc);
		} catch (FileNotFoundException e) 
		{
			return;
		}
	    Reader reader=new InputStreamReader(stream);
	    BufferedReader input = (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
	    List<String> toSave=new ArrayList<String>();
	    try 
	    {
	    	String temp;
	    	try 
	    	{
	    		while ((temp = input.readLine()) != null) 
	    		{
	    			toSave.add(temp);
	    		}
	    	} 
	    	catch (IOException e) 
	    	{
	    		return;
	    	} 
	    } 
	    finally 
	    {
	    	try 
	    	{
				input.close();
			} 
	    	catch (IOException e) 
	    	{
				return;
			}
		}
	    //add comments to the data
	    int maxIndex=Integer.MIN_VALUE;
	    for(int i:comments.keySet())
	    	if(i>maxIndex)
	    		maxIndex=i;
	    while(maxIndex>toSave.size())
	    	toSave.add("");
	    for(int i=0;i<=maxIndex;i++)
	    {
	    	if(comments.containsKey(i))
	    	{
	    		String comment=comments.get(i);
	    		if(!comment.startsWith("#"))
	    			comment="#"+comment;
	    		toSave.add(i, comment);
	    	}
	    }
	    //make string
	    StringBuilder builder = new StringBuilder();
	    for(String s:toSave)
	    {
	    	builder.append(s);
	    	builder.append("\n");
	    }
	    //save it
	    try
	    {
			Files.write(loc.toPath(), builder.toString().getBytes(), StandardOpenOption.WRITE);
		} 
	    catch (IOException e) 
	    {
			return;
		}
	}
	public void addComment(String comment,int line)
	{
		save(true);
		if(!comment.startsWith("#"))
			comment="#"+comment;
		//load all data from file
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(loc);
		} catch (FileNotFoundException e) 
		{
			return;
		}
	    Reader reader=new InputStreamReader(stream);
	    BufferedReader input = (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
	    List<String> toSave=new ArrayList<String>();
	    try 
	    {
	    	String temp;
	    	try 
	    	{
	    		while ((temp = input.readLine()) != null) 
	    		{
	    			toSave.add(temp);
	    		}
	    	} 
	    	catch (IOException e) 
	    	{
	    		return;
	    	} 
	    } 
	    finally 
	    {
	    	try 
	    	{
				input.close();
			} 
	    	catch (IOException e) 
	    	{
				return;
			}
		}
	    //add comments to the data
	    while(line>toSave.size())
	    	toSave.add("");
		toSave.add(line, comment);
	    //make string
	    StringBuilder builder = new StringBuilder();
	    for(String s:toSave)
	    {
	    	builder.append(s);
	    	builder.append("\n");
	    }
	    //save it
	    try
	    {
			Files.write(loc.toPath(), builder.toString().getBytes(), StandardOpenOption.WRITE);
		} 
	    catch (IOException e) 
	    {
			return;
		}
	}
	public void addComment(String comment)
	{
		save(true);
		if(!comment.startsWith("#"))
			comment="#"+comment;
		//load all data from file
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(loc);
		} catch (FileNotFoundException e) 
		{
			return;
		}
	    Reader reader=new InputStreamReader(stream);
	    BufferedReader input = (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
	    List<String> toSave=new ArrayList<String>();
	    try 
	    {
	    	String temp;
	    	try 
	    	{
	    		while ((temp = input.readLine()) != null) 
	    		{
	    			toSave.add(temp);
	    		}
	    	} 
	    	catch (IOException e) 
	    	{
	    		return;
	    	} 
	    } 
	    finally 
	    {
	    	try 
	    	{
				input.close();
			} 
	    	catch (IOException e) 
	    	{
				return;
			}
		}
	    //add comments to the data
		toSave.add(comment);
	    //make string
	    StringBuilder builder = new StringBuilder();
	    for(String s:toSave)
	    {
	    	builder.append(s);
	    	builder.append("\n");
	    }
	    //save it
	    try
	    {
			Files.write(loc.toPath(), builder.toString().getBytes(), StandardOpenOption.WRITE);
		} 
	    catch (IOException e) 
	    {
			return;
		}
	}
	public FileConfiguration getFile()
	{
		return this.file;
	}
	public void save(boolean preserveComments)
	{
		
			try 
			{
				file.save(loc);
			} 
			catch (IOException e) 
			{
			}
	}
	public void save(File location,boolean preserveComments)
	{
		
			try 
			{
				file.save(loc);
			} 
			catch (IOException e) 
			{
			}
	}
	public void reload()
	{
		load();
	}
	public void refresh()
	{
		if(!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();
		//make directories upto external location
		if(externalLocation!=null)
		{
			File temp=new File(plugin.getDataFolder()+File.separator+externalLocation);
			if(!temp.exists())
				temp.mkdirs();
			loc=new File(plugin.getDataFolder()+File.separator+externalLocation,externalName+".yml");
		}
		else
			loc=new File(plugin.getDataFolder(),externalName+".yml");
		//create a new file
		try
		{
			loc.createNewFile();
			//check if there is a file within plugin
			if(internalLocation==null&&plugin.getResource(internalName+".yml")!=null) //exists
			{
				InputStream is =plugin.getResource(internalName+".yml");
				OutputStream os = new FileOutputStream(loc);
				ByteStreams.copy(is, os);
			}
			else if(plugin.getClass().getResourceAsStream(File.separator+internalLocation+File.separator+internalName+".yml")!=null)//exists
			{
				InputStream is =plugin.getClass().getResourceAsStream(File.separator+internalLocation+File.separator+internalName+".yml");
				OutputStream os = new FileOutputStream(loc);
				ByteStreams.copy(is, os);
			}
		}
		catch(IOException e)
		{}
		//load the yml file
		file=YamlConfiguration.loadConfiguration(loc);
	}
	
	public File getLoc() 
	{
		return loc;
	}
	private void load() 
	{
		if(!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();
		//make directories upto external location
		if(externalLocation!=null)
		{
			File temp=new File(plugin.getDataFolder()+File.separator+externalLocation);
			if(!temp.exists())
				temp.mkdirs();
			loc=new File(plugin.getDataFolder()+File.separator+externalLocation,externalName+".yml");
		}
		else
			loc=new File(plugin.getDataFolder(),externalName+".yml");
		//check if the yml file already exists
		if(!loc.exists())
		{
			//create a new file
			try
			{
				loc.createNewFile();
				//check if there is a file within plugin
				if(plugin.getResource(internalName+".yml")!=null) //exists
				{
					InputStream is =plugin.getResource(internalName+".yml");
					OutputStream os = new FileOutputStream(loc);
					ByteStreams.copy(is, os);
				}
				else if(plugin.getClass().getResourceAsStream("/"+internalLocation+"/"+internalName+".yml")!=null)//exists
				{
					InputStream is =plugin.getClass().getResourceAsStream("/"+internalLocation+"/"+internalName+".yml");
					OutputStream os = new FileOutputStream(loc);
					ByteStreams.copy(is, os);
				}
			}
			catch(IOException e)
			{}
		}
		//load the yml file
		file=YamlConfiguration.loadConfiguration(loc);
	}
}
