package hu.stan.dreamparkour;

import hu.stan.dreamparkour.repository.util.HibernateUtils;
import hu.stan.dreamparkour.service.toprunners.TopRunnersService;
import hu.stan.dreamweaver.DreamWeaver;
import hu.stan.dreamweaver.annotation.core.Plugin;
import hu.stan.dreamweaver.core.dependency.injector.DependencyInjector;

@Plugin
public final class DreamParkour extends DreamWeaver {

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
