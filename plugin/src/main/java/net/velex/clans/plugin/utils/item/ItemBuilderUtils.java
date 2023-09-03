package net.velex.clans.plugin.utils.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class ItemBuilderUtils {
	private final String material;
	
	private String displayName;
	private List<String> lore;
	
	private ItemBuilderUtils(final @NotNull String material) {
		this.material = Objects.requireNonNull(material, "Material name cannot be null.");
	}
	
	public static @NotNull ItemBuilderUtils of(final @NotNull String material) {
		return new ItemBuilderUtils(material);
	}
	
	public @NotNull ItemBuilderUtils name(final @NotNull String displayName) {
		this.displayName = Objects.requireNonNull(displayName, "Display name for item cannot be null.");
		return this;
	}
	
	public @NotNull ItemBuilderUtils lore(final @NotNull List<String> lore) {
		this.lore = Objects.requireNonNull(lore, "Lore content for item cannot be null.");
		return this;
	}
	
	public @NotNull ItemStack build() {
		Material bukkitMaterial = Material.getMaterial(material);
		// If a material with that name doesn't exist, set 'STONE' as default material
		// for the item.
		if (bukkitMaterial == null) {
			bukkitMaterial = Material.STONE;
		}
		// Check if the display name or the lore were established. If not there is, return an
		// item without modified attributes.
		if ((displayName == null) || (lore == null)) {
      return new ItemStack(bukkitMaterial, 1);
    }
		final var item = new ItemStack(bukkitMaterial, 1);
		final var meta = item.getItemMeta();
		assert meta != null;
		// Modify current item properties such as, flags, name, etc.
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setUnbreakable(true);
		meta.setDisplayName(displayName);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
