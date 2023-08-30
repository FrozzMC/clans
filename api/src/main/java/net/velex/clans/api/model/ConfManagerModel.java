package net.velex.clans.api.model;

import net.velex.clans.api.enums.Result;
import net.velex.clans.api.model.config.*;
import org.jetbrains.annotations.NotNull;

public interface ConfManagerModel {
  /**
   * Checks if all the configuration models were loaded correctly.
   *
   * @return A {@link Result} enum type depending on the operation success.
   */
  @NotNull Result load();
  
  /**
   * Tries to return the current {@link ConfModel} reference, but, if the model isn't loaded will throw<p>
   * an exception {@link IllegalStateException} type.
   *
   * @return Must be a {@link ConfModel} object reference.
   */
  @NotNull ConfModel config();
  
  /**
   * Tries to return the current {@link CommandsConfModel} reference, but, if the model isn't loaded will throw<p>
   * an exception {@code IllegalStateException} type.
   *
   * @return Must be a {@link CommandsConfModel} object reference.
   */
  @NotNull CommandsConfModel commands();

  /**
   * Tries to return the current {@link FormulasConfModel} reference, but, if the model isn't loaded will throw<p>
   * an exception {@link IllegalStateException} type.
   *
   * @return Must be a {@link FormulasConfModel} object reference.
   */
  @NotNull FormulasConfModel formulas();

  /**
   * Tries to return the current {@link AntiFarmConfModel} reference, but, if the model isn't loaded will throw<p>
   * an exception {@link IllegalStateException} type.
   *
   * @return Must be a {@link AntiFarmConfModel} object reference.
   */
  @NotNull AntiFarmConfModel antiFarm();
}
