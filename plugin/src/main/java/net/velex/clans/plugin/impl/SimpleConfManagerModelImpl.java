package net.velex.clans.plugin.impl;

import net.velex.clans.api.enums.Result;
import net.velex.clans.api.model.ConfManagerModel;
import net.velex.clans.api.model.config.AntiFarmConfModel;
import net.velex.clans.api.model.config.CommandsConfModel;
import net.velex.clans.api.model.config.ConfModel;
import net.velex.clans.api.model.config.FormulasConfModel;
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

public class SimpleConfManagerModelImpl implements ConfManagerModel {
  private final ConfigurationHelper<ConfModel> confModelHelper;
  private final ConfigurationHelper<CommandsConfModel> commandsConfModelHelper;
  private final ConfigurationHelper<FormulasConfModel> formulasConfModelHelper;
  private final ConfigurationHelper<AntiFarmConfModel> antiFarmConfModelHelper;

  private ConfModel confModel;
  private CommandsConfModel commandsConfModel;
  private FormulasConfModel formulasConfModel;
  private AntiFarmConfModel antiFarmConfModel;
  
  public SimpleConfManagerModelImpl(final @NotNull Path dataFolder) {
    final var annotationOptions = new ConfigurationOptions.Builder()
      .sorter(new AnnotationBasedSorter())
      .build();
    final var commentModeType = new SnakeYamlOptions.Builder()
        .commentMode(CommentMode.fullComments())
        .build();
    confModelHelper = new ConfigurationHelper<>(dataFolder, "config.yml", SnakeYamlConfigurationFactory.create(
      ConfModel.class,
      annotationOptions,
      commentModeType
    ));
    commandsConfModelHelper = new ConfigurationHelper<>(dataFolder, "commands.yml", SnakeYamlConfigurationFactory.create(
      CommandsConfModel.class,
      annotationOptions,
      commentModeType
    ));
    formulasConfModelHelper = new ConfigurationHelper<>(dataFolder, "formulas.yml", SnakeYamlConfigurationFactory.create(
      FormulasConfModel.class,
      annotationOptions,
      commentModeType
    ));
    antiFarmConfModelHelper = new ConfigurationHelper<>(dataFolder, "anti-farming.yml", SnakeYamlConfigurationFactory.create(
      AntiFarmConfModel.class,
      annotationOptions,
      commentModeType
    ));
  }

  @Override
  public @NotNull Result load() {
    try {
      confModel = confModelHelper.reloadConfigData();
      commandsConfModel = commandsConfModelHelper.reloadConfigData();
      formulasConfModel = formulasConfModelHelper.reloadConfigData();
      antiFarmConfModel = antiFarmConfModelHelper.reloadConfigData();
    } catch (final InvalidConfigException | IOException exception) {
      exception.printStackTrace();
      return Result.NO_CONFIG_LOAD;
    }
    return Result.SUCCESS;
  }

  @Override
  public @NotNull ConfModel config() {
    return confModel;
  }

  @Override
  public @NotNull CommandsConfModel commands() {
    return commandsConfModel;
  }

  @Override
  public @NotNull FormulasConfModel formulas() {
    return formulasConfModel;
  }

  @Override
  public @NotNull AntiFarmConfModel antiFarm() {
    return antiFarmConfModel;
  }
}
