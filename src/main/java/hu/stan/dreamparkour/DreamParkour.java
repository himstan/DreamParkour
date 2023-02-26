package hu.stan.dreamparkour;

import hu.stan.dreamparkour.repository.util.HibernateUtils;
import hu.stan.dreamparkour.service.toprunners.TopRunnersService;
import hu.stan.dreamplugin.DreamPlugin;
import hu.stan.dreamplugin.annotation.core.Plugin;
import hu.stan.dreamplugin.core.dependency.injector.DependencyInjector;

@Plugin
public final class DreamParkour extends DreamPlugin {

  @Override
  public void onPluginEnable() {
    final var topRunnersService =
        DependencyInjector.getInstance().getInstanceOf(TopRunnersService.class);
    topRunnersService.initTopRuns();
  }

  @Override
  public void onPluginDisable() {
    HibernateUtils.shutdown();
  }
}
