package hu.stan.dreamparkour.hooks.papi;

import hu.stan.dreamparkour.hooks.papi.extension.TopRunnersExtension;
import hu.stan.dreamparkour.service.course.CourseService;
import hu.stan.dreamparkour.service.toprunners.TopRunnersService;
import hu.stan.dreamplugin.annotation.core.ExternalHook;
import hu.stan.dreamplugin.core.pluginhook.PluginHook;
import lombok.RequiredArgsConstructor;

@ExternalHook(pluginName = "PlaceholderAPI")
@RequiredArgsConstructor
public class PlaceholderApiHook implements PluginHook {

  private final CourseService courseService;
  private final TopRunnersService topRunnersService;

  @Override
  public void registerHook() {
    final var extension = new TopRunnersExtension(courseService, topRunnersService);
    extension.register();
  }

  @Override
  public void unregisterHook() {
    final var extension = new TopRunnersExtension(courseService, topRunnersService);
    extension.unregister();
  }
}
