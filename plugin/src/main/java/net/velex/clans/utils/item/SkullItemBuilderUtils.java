package net.velex.clans.utils.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.UUID;

public class SkullItemBuilderUtils {
  private static final StringBuilder STRING_BUILDER = new StringBuilder(150);
  private static final ItemStack PLAYER_HEAD_ITEM = new ItemStack(Material.PLAYER_HEAD, 1);
  
  /**
   * Creates a player skull based on a player's name.
   *
   * @param item The item to apply the name to
   * @param name The Player's name
   * @return The head of the Player
   *
   * @deprecated names don't make for good identifiers
   */
  @Deprecated
  public static @NotNull ItemStack itemWithName(final @NotNull ItemStack item, final @NotNull String name) {
    return Bukkit.getUnsafe().modifyItemStack(item, STRING_BUILDER.append("{SkullOwner:")
      .append(name)
      .append("\"}")
      .toString()
    );
  }
  
  /**
   * Creates a player skull with a UUID. 1.13 only.
   *
   * @param id The Player's UUID
   * @return The head of the Player
   */
  public static @NotNull ItemStack itemFromUUID(final @NotNull UUID id) {
    return itemWithUUID(PLAYER_HEAD_ITEM, id);
  }
  
  /**
   * Creates a player skull based on a UUID. 1.13 only.
   *
   * @param item The item to apply the name to
   * @param id The Player's UUID
   * @return The head of the Player
   */
  public static @NotNull ItemStack itemWithUUID(final @NotNull ItemStack item, final @NotNull UUID id) {
    final var skullMeta = (SkullMeta) item.getItemMeta();
    assert skullMeta != null;
    skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(id));
    item.setItemMeta(skullMeta);
    return item;
  }
  
  /**
   * Creates a player skull based on a Mojang server URL.
   *
   * @param url The URL of the Mojang skin
   * @return The head associated with the URL
   */
  public static @NotNull ItemStack itemFromURL(final @NotNull String url) {
    return itemWithURL(PLAYER_HEAD_ITEM, url);
  }
  
  
  /**
   * Creates a player skull based on a Mojang server URL.
   *
   * @param item The item to apply the skin to
   * @param url The URL of the Mojang skin
   * @return The head associated with the URL
   */
  public static @NotNull ItemStack itemWithURL(final @NotNull ItemStack item, final @NotNull String url) {
    return itemWithBase64(item, urlToBase64(url));
  }
  
  /**
   * Creates a player skull based on a base64 string containing the link to the skin.
   *
   * @param base64 The base64 string containing the texture
   * @return The head with a custom texture
   */
  public static @NotNull ItemStack itemFromBase64(final @NotNull String base64) {
    return itemWithBase64(PLAYER_HEAD_ITEM, base64);
  }
  
  /**
   * Applies the base64 string to the ItemStack.
   *
   * @param item The ItemStack to put the base64 onto
   * @param base64 The base64 string containing the texture
   * @return The head with a custom texture
   */
  @SuppressWarnings("deprecation")
  public static @NotNull ItemStack itemWithBase64(final @NotNull ItemStack item, final @NotNull String base64) {
    return Bukkit.getUnsafe().modifyItemStack(item, STRING_BUILDER.append("{SkullOwner:{Id:")
      .append(new UUID(base64.hashCode(), base64.hashCode()))
      .append(",Properties:{textures:[{Value:")
      .append(base64)
      .append("}]}}}")
      .toString()
    );
  }
  
  /**
   * Sets the block to a skull with the given name.
   *
   * @param block The block to set
   * @param name The player to set it to
   *
   * @deprecated names don't make for good identifiers
   */
  @Deprecated
  public static void blockWithName(final @NotNull Block block, final @NotNull String name) {
    block.setType(Material.PLAYER_HEAD);
    ((SkullMeta) block.getState())
      .setOwningPlayer(Bukkit.getOfflinePlayer(name));
  }
  
  /**
   * Sets the block to a skull with the given UUID.
   *
   * @param block The block to set
   * @param id The player to set it to
   */
  public static void blockWithUuid(final @NotNull Block block, final @NotNull UUID id) {
    block.setType(Material.PLAYER_HEAD);
    ((Skull) block.getState())
      .setOwningPlayer(Bukkit.getOfflinePlayer(id));
  }
  
  /**
   * Sets the block to a skull with the given UUID.
   *
   * @param block The block to set
   * @param url The mojang URL to set it to use
   */
  public static void blockWithUrl(final @NotNull Block block, final @NotNull String url) {
    blockWithBase64(block, urlToBase64(url));
  }
  
  /**
   * Sets the block to a skull with the given UUID.
   *
   * @param block The block to set
   * @param base64 The base64 to set it to use
   */
  public static void blockWithBase64(final @NotNull Block block, final @NotNull String base64) {
    final var args = String.format("%d %d %d %s",
      block.getX(), block.getY(), block.getZ(),
      STRING_BUILDER.append("{Owner:{Id:")
        .append(new UUID(base64.hashCode(), base64.hashCode()))
        .append(",Properties:{textures:[{Value:")
        .append(base64)
        .append("}]}}}")
    );
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "data merge block " + args);
  }

  private static @NotNull String urlToBase64(final @NotNull String url) {
    URI actualUrl;
    try {
      actualUrl = new URI(url);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    return Base64.getEncoder().encodeToString(STRING_BUILDER.append("{\"textures\":{\"SKIN\":{\"url\":\"")
      .append(actualUrl)
      .append("\"}}}")
      .toString()
      .getBytes());
  }
}
