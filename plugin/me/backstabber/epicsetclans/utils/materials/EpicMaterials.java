package me.backstabber.epicsetclans.utils.materials;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class EpicMaterials implements Versionable{
	private boolean useLatest;
	private UMaterials umaterial;
	private Material vanilla;
	public static EpicMaterials valueOf(String material) {
		EpicMaterials mat=new EpicMaterials();
		if(!SIXTEEN) {
			mat.useLatest=false;
			mat.umaterial=UMaterials.valueOf(material);
			mat.vanilla=Material.AIR;
		}
		else {
			mat.useLatest=true;
			mat.umaterial=UMaterials.AIR;
			mat.vanilla=Material.valueOf(material);
		}
		return mat;
	}
	public static EpicMaterials valueOf(ItemStack item) {
		EpicMaterials mat=new EpicMaterials();
		if(!SIXTEEN) {
			mat.useLatest=false;
			mat.umaterial=UMaterials.match(item);
			mat.vanilla=Material.AIR;
		}
		else {
			mat.useLatest=true;
			mat.umaterial=UMaterials.AIR;
			mat.vanilla=item.getType();
		}
		return mat;
	}
	public static EpicMaterials valueOf(UMaterials umat) {
		EpicMaterials mat=new EpicMaterials();
		if(!SIXTEEN) {
			mat.useLatest=false;
			mat.umaterial=umat;
			mat.vanilla=Material.AIR;
		}
		else {
			mat.useLatest=true;
			mat.umaterial=UMaterials.AIR;
			mat.vanilla=umat.getMaterial();
		}
		return mat;
	}
	public static EpicMaterials valueOf(Block block) {
		EpicMaterials mat=new EpicMaterials();
		if(!SIXTEEN) {
			mat.useLatest=false;
			mat.umaterial=UMaterials.getItem(block);
			mat.vanilla=Material.AIR;
		}
		else {
			mat.useLatest=true;
			mat.umaterial=UMaterials.AIR;
			mat.vanilla=block.getType();
		}
		return mat;
	}
	public Material getMaterial() {
		return (useLatest) ? vanilla : umaterial.getMaterial();
	}
	public ItemStack getItemStack() {
		return (useLatest) ? new ItemStack(vanilla) : umaterial.getItemStack();
	}
	public String name() {
		return (useLatest) ? vanilla.name() : umaterial.name();
	}
}
