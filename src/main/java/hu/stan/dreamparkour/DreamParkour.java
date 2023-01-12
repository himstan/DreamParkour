package hu.stan.dreamparkour;

import hu.stan.dreamparkour.repository.util.HibernateUtils;
import hu.stan.dreamplugin.DreamPlugin;
import hu.stan.dreamplugin.annotation.core.Plugin;

@Plugin
public final class DreamParkour extends DreamPlugin {

  @Override
  public void onPluginEnable() {

  }

  @Override
  public void onPluginDisable() {
    HibernateUtils.shutdown();
  }
}
