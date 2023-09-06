package net.velex.clans.config.model;

import org.jetbrains.annotations.NotNull;
import space.arim.dazzleconf.annote.*;
import space.arim.dazzleconf.serialiser.URLValueSerialiser;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

@ConfSerialisers(URLValueSerialiser.class)
@ConfHeader("""
  Formulas Config | By Qekly
  Modify and set formula values for the plugin options.
  """)
public interface FormulasConfModel {
  @AnnotationBasedSorter.Order(10)
  @ConfKey("points-per-kill")
  @ConfComments("Points awarded on killing to an player.")
  @ConfDefault.DefaultInteger(10)
  byte pointAwardPerKill();

  @AnnotationBasedSorter.Order(20)
  @ConfKey("points-decrease-per-death")
  @ConfComments("Points decrease when death.")
  @ConfDefault.DefaultInteger(0)
  byte pointDecreasePerDeath();

  @AnnotationBasedSorter.Order(30)
  @ConfKey("respect-formula-calc")
  @ConfComments("Calculation formula for the clan respect level.")
  @ConfDefault.DefaultString("(<kills>/10)-(<deaths>/20)+<duel-wins>-(<duel-loses>/2)")
  @NotNull String respectFormulaValue();
}
