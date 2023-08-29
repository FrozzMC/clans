package me.backstabber.epicsetclans.clanhandles.manager;

public class ClanTagManager 
{
	public static String generateTag(String name)
	{
		//test for multiple words
		if(name.split(" ").length>1)
		{
			String tag="";
			if(name.split(" ").length==2)
			{
				if(name.split(" ")[0].length()>1)
					tag=tag+name.split(" ")[0].substring(0,1).toUpperCase()+name.split(" ")[0].substring(1,2).toLowerCase()+name.split(" ")[1].substring(0,1).toUpperCase();
				else if(name.split(" ")[1].length()>1)
					tag=tag+name.split(" ")[0].substring(0,1).toUpperCase()+name.split(" ")[1].substring(0,1).toUpperCase()+name.split(" ")[1].substring(1,2).toLowerCase();
				else
					tag=tag+name.split(" ")[0].substring(0,1).toUpperCase()+name.split(" ")[1].substring(0,1).toUpperCase();
			}
			else
			{
				for(String s:name.split(" "))
					tag=tag+s.substring(0,1).toUpperCase();
			}
			return tag;
		}
		//test for current length
		else if(name.length()>3)
		{
			int length=name.length();
			int middle=(int)((double)length/2D);
			return name.substring(0,1).toUpperCase()+name.substring(middle,middle+1).toLowerCase()+name.substring(name.length()-1,name.length()).toUpperCase();
		}
		else
			return name;
	}
}
