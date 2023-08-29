package me.backstabber.epicsetclans.messager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.backstabber.epicsetclans.utils.CommonUtils;

public class MessageFormatter {

	
	public static void sendJSONMessage(Player to, JavaPlugin plugin,List<String> message)
	{
		new BukkitRunnable() 
		{
			@Override
			public void run() 
			{
				for(String data:message)
				{
					data=data.replace("%player%", to.getName());
					if(data.startsWith("<delay>"))
					{
						data=data.replace("<delay>", "");
						int delay=0;
						try
						{
							delay=Integer.valueOf(data);
						}
						catch(NumberFormatException|NullPointerException e)
						{
						}
						try 
						{
							Thread.sleep(delay);
						} catch (InterruptedException e) 
						{
							e.printStackTrace();
						}
						continue;
					}
					if(data.startsWith("<cmd>"))
					{
						final String cmd=data.replace("<cmd>", "");
						new BukkitRunnable() 
						{
							@Override
							public void run() 
							{

								Bukkit.dispatchCommand(to, cmd);
							}
						}.runTask(plugin);
						continue;
					}
					if(data.startsWith("<ccmd>"))
					{

						final String cmd=data.replace("<ccmd>", "");
						new BukkitRunnable() 
						{
							@Override
							public void run() 
							{
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
							}
						}.runTask(plugin);
						continue;
					}
					sendPart(to,data);
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	//extra part of above logic
	private static void sendPart(Player player, String data) 
	{
		JSONMessage msg=JSONMessage.create();
		ArrayList<Integer> title=null;
		boolean subtitle=false;
		String hotbar=null;
		for(String part:split(data,"<p>"))
		{
			JSONMessage jtip = null;
			String suggest = null;
			String cmd = null;
			if(part.contains("<t>")&&getCount(part, "<t>")==2)
			{
				jtip=JSONMessage.create();
				String tip=split(part,"<t>")[1];
				part=part.replace(tip, "").replace("<t>", "");
				String[] array = split(tip,"<n>");
				for(int i=0;i<array.length;i++)
				{
					jtip=jtip.then(CommonUtils.chat(array[i]));
					if(i<array.length-1)
						jtip=jtip.newline();
				}
			}
			if(part.contains("<cs>")&&getCount(part, "<cs>")==2)
			{
				String tip=split(part,"<cs>")[1];
				part=part.replace(tip, "").replace("<cs>", "");
				suggest=ChatColor.stripColor(chat(tip));
			}
			else if(part.contains("<cc>")&&getCount(part, "<cc>")==2)
			{
				String tip=split(part,"<cc>")[1];
				part=part.replace(tip, "").replace("<cc>", "");
				cmd=ChatColor.stripColor(chat(tip));
			}
			if(part.endsWith("<st>")&&getCount(part, "<st>")==2)
			{
				String tip=split(part,"<st>")[1];
				part=part.replace(tip, "").replace("<st>", "");
				tip=tip.replace(" ", "");
				String[] settings = split(tip,":");
				if(settings.length==3)
				{
					title=new ArrayList<Integer>();
					for(String s:settings)
					{
						try
						{
							title.add(Integer.valueOf(s));
						}
						catch(NumberFormatException|NullPointerException e)
						{
							title.add(0);
						}
					}
				}
			}
			else if(part.endsWith("<sst>"))
			{
				part=part.replace("<sst>", "");
				subtitle=true;
			}
			else if(part.endsWith("<sh>"))
			{
				part=part.replace("<sh>", "");
				hotbar=part;
			}
			msg=msg.then(CommonUtils.chat(part));
			if(jtip!=null)
				msg=msg.tooltip(jtip);
			if(suggest!=null&&suggest!="")
				msg=msg.suggestCommand(suggest);
			else if(cmd!=null&&cmd!="")
				msg=msg.runCommand(cmd);
		}
		if(title!=null&&title.size()==3)
			msg.title(title.get(0), title.get(1), title.get(2), player);
		else if(subtitle)
			msg.subtitle(player);
		else if(hotbar!=null)
			JSONMessage.actionbar(CommonUtils.chat(hotbar), player);
		else
			msg.send(player);
	}
	
	private static int getCount(String string ,String regex)
	{
		int count = StringUtils.countMatches(string, regex);
		return count;
	}
	private static String[] split(String msg,String at) {
		return msg.split(at);
	}
	private static String chat(String s) 
	{ 
		return ChatColor.translateAlternateColorCodes('&', s); 
	}
	@SuppressWarnings("unused")
	private static JSONMessage getParts(String text)  {
		JSONMessage msg=JSONMessage.create();
		 String regex = "[&§]{1}([a-fA-Fl-oL-O0-9]){1}";
         text = text.replaceAll(regex, "§$1");
         if(!Pattern.compile(regex).matcher(text).find()) {
            msg.then(text);
            return msg;
         }
         String[] words = text.split(regex);

         int index = words[0].length();
         for(String word : words) {
             try {
                 if(index != words[0].length())
             msg.then(word).code("§"+text.charAt(index - 1));
             } catch(Exception e){}
             index += word.length() + 2;
         }
         return msg;
	}
}
