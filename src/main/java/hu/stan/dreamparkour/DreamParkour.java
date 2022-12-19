package hu.stan.dreamparkour;

import hu.stan.dreamparkour.command.SetStartCommand;
import hu.stan.dreamparkour.service.CourseBuilderService;
import hu.stan.dreamparkour.service.CourseService;
import hu.stan.dreamplugin.DreamPlugin;
import hu.stan.dreamplugin.annotation.core.Plugin;
import hu.stan.dreamplugin.core.dependency.injector.DependencyInjector;

@Plugin
public final class DreamParkour extends DreamPlugin {

  @Override
  public void onPluginEnable() {
    final var service = DependencyInjector.getInstance().initializeClass(CourseService.class);
    final var builderService = DependencyInjector.getInstance().initializeClass(
        CourseBuilderService.class);
    getCommand("dreamparkour").setExecutor(
        new SetStartCommand((CourseService) service, (CourseBuilderService) builderService));
  }

  @Override
  public void onPluginDisable() {

  }
}
