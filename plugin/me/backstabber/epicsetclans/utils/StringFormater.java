package me.backstabber.epicsetclans.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;


public class StringFormater 
{
    //method for extracting locations from string
    public static Location stringToLocation(String str) 
    {
        String[] split = str.split(";");
        if (split.length > 4)
        {
            return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]),
                    Float.parseFloat(split[4]), Float.parseFloat(split[5]));
        }
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }

    //Make location to string
    public static String locationToString(Location loc, boolean saveYawAndPitch) 
    {
        if (saveYawAndPitch)
            return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch();
        return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ();
    }
}
