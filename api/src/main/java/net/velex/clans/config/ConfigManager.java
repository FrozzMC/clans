package net.velex.clans.config;

import net.velex.clans.api.config.model.*;
import net.velex.clans.config.model.*;
import org.jetbrains.annotations.NotNull;
import space.arim.dazzleconf.ConfigurationOptions;
import space.arim.dazzleconf.error.InvalidConfigException;
import space.arim.dazzleconf.ext.snakeyaml.CommentMode;
import space.arim.dazzleconf.ext.snakeyaml.SnakeYamlConfigurationFactory;
import space.arim.dazzleconf.ext.snakeyaml.SnakeYamlOptions;
import space.arim.dazzleconf.helper.ConfigurationHelper;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

import java.io.IOException;
import java.nio.file.Path;

public class ConfigManager {
  private final ConfigurationHelper<ConfModel> confModelHelper;
  private final ConfigurationHelper<AntiFarmConfModel> antiFarmConfModelHelper;
  private final ConfigurationHelper<CommandsConfModel> commandsConfModelHelper;
  private final ConfigurationHelper<DuelSettingsConfModel> duelSettingsConfModelHelper;
  private final ConfigurationHelper<FormulasConfModel> formulasConfModelHelper;
  private ConfModel confModel;
  private AntiFarmConfModel antiFarmConfModel;
  private CommandsConfModel commandsConfModel;
  private DuelSettingsConfModel duelSettingsConfModel;
  private FormulasConfModel formulasConfModel;
  
  public ConfigManager(final @NotNull Path dataFolder) {
    final var commentMode = new SnakeYamlOptions.Builder()
      .commentMode(CommentMode.fullComments())
      .build();
    final var options = new ConfigurationOptions.Builder()
      .sorter(new AnnotationBasedSorter())
      .build();
    confModelHelper = new ConfigurationHelper<>(dataFolder, "config.yml", SnakeYamlConfigurationFactory.create(
      ConfModel.class,
      options,
      commentMode
    ));
    antiFarmConfModelHelper = new ConfigurationHelper<>(dataFolder, "anti-farm.yml", SnakeYamlConfigurationFactory.create(
      AntiFarmConfModel.class,
      options,
      commentMode
    ));
    commandsConfModelHelper = new ConfigurationHelper<>(dataFolder, "commands.yml", SnakeYamlConfigurationFactory.create(
      CommandsConfModel.class,
      options,
      commentMode
    ));
    duelSettingsConfModelHelper = new ConfigurationHelper<>(dataFolder, "duel-settings.yml", SnakeYamlConfigurationFactory.create(
      DuelSettingsConfModel.class,
      options,
      commentMode
    ));
    formulasConfModelHelper = new ConfigurationHelper<>(dataFolder, "formulas.yml", SnakeYamlConfigurationFactory.create(
      FormulasConfModel.class,
      options,
      commentMode
    ));
  }
  
  /**
   * Checks if all the configuration models were loaded correctly.
   *
   * @return If the configuration were loaded correctly, will return a {@link ConfigManager.Result#SUCCESS} enum type.
   * <p>Else, will return a {@link ConfigManager.Result#NO_CONFIG_LOAD} enum type.
   */
  public @NotNull Result load() {
    try {
      confModel = confModelHelper.reloadConfigData();
      antiFarmConfModel = antiFarmConfModelHelper.reloadConfigData();
      commandsConfModel = commandsConfModelHelper.reloadConfigData();
      duelSettingsConfModel = duelSettingsConfModelHelper.reloadConfigData();
      formulasConfModel = formulasConfModelHelper.reloadConfigData();
    } catch (final IOException | InvalidConfigException exception) {
      exception.printStackTrace();
      return Result.NO_CONFIG_LOAD;
    }
    return Result.SUCCESS;
  }
  
  /**
   * Tries to return the current {@link ConfModel} reference, but, if the model isn't loaded will throw<p>
   * an exception {@link IllegalStateException} type.
   *
   * @return Must be a {@link ConfModel} object reference.
   */
  public @NotNull ConfModel config() {
    return confModel;
  }
  
  /**
   * Tries to return the current {@link CommandsConfModel} reference, but, if the model isn't loaded will throw<p>
   * an exception {@code IllegalStateException} type.
   *
   * @return Must be a {@link CommandsConfModel} object reference.
   */
  public @NotNull CommandsConfModel commands() {
    return commandsConfModel;
  }
  
  /**
   * Tries to return the current {@link FormulasConfModel} reference, but, if the model isn't loaded will throw<p>
   * an exception {@link IllegalStateException} type.
   *
   * @return Must be a {@link FormulasConfModel} object reference.
   */
  public @NotNull FormulasConfModel formulas() {
    return formulasConfModel;
  }
  
  /**
   * Tries to return the current {@link AntiFarmConfModel} reference, but, if the model isn't loaded will throw<p>
   * an exception {@link IllegalStateException} type.
   *
   * @return Must be a {@link AntiFarmConfModel} object reference.
   */
  public @NotNull AntiFarmConfModel antiFarm() {
    return antiFarmConfModel;
  }
  
  /**
   * Tries to return the current {@link DuelSettingsConfModel} reference, but, if the model isn't loaded will throw<p>
   * an exception {@link IllegalStateException} type.
   *
   * @return Must be a {@link DuelSettingsConfModel} object reference.
   */
  public @NotNull DuelSettingsConfModel duelSettings() {
    return duelSettingsConfModel;
  }

  public enum Result {
    /**
     * Indicates that the operation were completed correctly.
     */
    SUCCESS,
    /**
     * Indicates that some (or all) configuration models could not be loaded correctly.
     */
    NO_CONFIG_LOAD
  }
}
