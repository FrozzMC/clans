package me.backstabber.epicsetclans.enums;

import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;

public enum UEnchants 
{
	//FORMAT ("bukkit enum",min compatible version,"backup enum")
	//all enchants in 1.8
	AQUA_AFFINITY("WATER_WORKER"),
	BANE_OF_ARTHROPODS("DAMAGE_ARTHROPODS"),
	BLAST_PROTECTION("PROTECTION_EXPLOSION"),
	DEPTH_STRIDER("DEPTH_STRIDER"),
	EFFICENCY("DIG_SPEED"),
	FEATHER_FALLING("PROTECTION_FALL"),
	FIRE_ASPECT("FIRE_ASPECT"),
	FIRE_PROTECTION("PROTECTION_FIRE"),
	FLAME("ARROW_FIRE"),
	FORTUNE("LOOT_BONUS_BLOCKS"),
	INFINITY("ARROW_INFINITE"),
	KNOCKBACK("KNOCKBACK"),
	LOOTING("LOOT_BONUS_MOBS"),
	LUCK_OF_THE_SEA("LUCK"),
	LURE("LURE"),
	POWER("ARROW_DAMAGE"),
	PROJECTILE_PROTECTION("PROTECTION_PROJECTILE"),
	PROTECTION("PROTECTION_ENVIRONMENTAL"),
	PUNCH("ARROW_KNOCKBACK"),
	RESPIRATION("OXYGEN"),
	SHARPNESS("DAMAGE_ALL"),
	SILK_TOUCH("SILK_TOUCH"),
	SMITE("DAMAGE_UNDEAD"),
	THORNS("THORNS"),
	UNBREAKING("DURABILITY"),
	//enchants added in 1.9
	FROST_WALKER("FROST_WALKER",9.0,"DEPTH_STRIDER"),
	MENDING("MENDING",9.0,"UNBREAKING"),
	//enchants added in 1.11
	CURSE_OF_BINDING("BINDING_CURSE",11.0,"THORNS"),
	CURSE_OF_VANISHING("VANISHING_CURSE",11.0,"THORNS"),
	SWEEPING_EDGE("SWEEPING_EDGE",11.1,"SHARPNESS"),
	//enchants added in 1.13
	CHANNELING("CHANNELING",13.0,"SWEEPING_EDGE"),
	IMPALING("IMPALING",13.0,"SHARPNESS"),
	LOYALTY("LOYALTY",13.0,"CURSE_OF_BINDING"),
	RIPTIDE("RIPTIDE",13.0,"SMITE"),
	//enchants added in 1.14
	MULTISHOT("MULTISHOT",14.0,"INFINITY"),
	PIERCING("PIERCING",14.0,"POWER"),
	QUICK_CHARGE("QUICK_CHARGE",14.0,"PUNCH");
	
	private static double version=Double.valueOf(Bukkit.getServer().getBukkitVersion().substring(2,5));
	String bukkitEnum;
	double minVersion;
	String backupEnum;
	UEnchants(String bukkitEnum)
	{
		this.minVersion=8;
		this.backupEnum=null;
		this.bukkitEnum=bukkitEnum;
	}
	UEnchants(String bukkitEnum,double minVersion,String backupEnum)
	{
		this.bukkitEnum=bukkitEnum;
		this.minVersion=minVersion;
		this.backupEnum=backupEnum;
	}
	public static UEnchants getByName(String name)
	{
		name=name.replace(" ", "_");
		name=name.toUpperCase();
		//check for bukkit name
		for(UEnchants enchant:UEnchants.values())
			if(enchant.bukkitEnum.equalsIgnoreCase(name))
				return enchant;
		//check for vanilla name
		for(UEnchants enchant:UEnchants.values())
			if(enchant.name().equalsIgnoreCase(name))
				return enchant;
		return null;
	}
	@SuppressWarnings("deprecation")
	public static UEnchants valueOf(Enchantment bEnchant)
	{
		String name=bEnchant.getName().toUpperCase();
		//check for bukkit name
		for(UEnchants enchant:UEnchants.values())
			if(enchant.bukkitEnum.equalsIgnoreCase(name))
				return enchant;
		return null;
	}
	public static boolean isEnchantLore(String loreline)
	{
		String line=ChatColor.stripColor(loreline);
		for(UEnchants enchant:UEnchants.values())
			if(line.startsWith(ChatColor.stripColor(enchant.getFormattedName())))
				return true;
		return false;
	}
	public static UEnchants getFromEnchantLore(String loreline)
	{
		String line=ChatColor.stripColor(loreline);
		for(UEnchants enchant:UEnchants.values())
			if(line.startsWith(ChatColor.stripColor(enchant.getFormattedName())))
				return enchant;
		return null;
	}
	@SuppressWarnings("deprecation")
	public Enchantment getEnchant()
	{
		if(version<8)
			throw new IllegalArgumentException("Versions below 1.8 are not supported");
		if(version>=minVersion)
			return Enchantment.getByName(bukkitEnum);
		else
			return UEnchants.valueOf(backupEnum).getEnchant();
	}
	public String getFormattedName()
	{
		String name="";
		for(String s:this.name().split("_"))
			name=name+" "+s.substring(0,1).toUpperCase()+s.substring(1).toLowerCase();
		name=name.substring(1);
		return name;
	}
	public String getFormattedLore(int level)
	{
		String name=getFormattedName();
		String lvl=toRoman(level);
		return ChatColor.translateAlternateColorCodes('&', "&7"+name+" "+lvl);
	}
	private String toRoman(int number) 
	{
		TreeMap<Integer, String> map=new TreeMap<>();
		map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
        int l =  map.floorKey(number);
        if ( number == l ) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number-l);
    }
}
