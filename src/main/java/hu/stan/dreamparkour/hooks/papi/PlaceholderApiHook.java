package hu.stan.dreamparkour.hooks.papi;

import hu.stan.dreamparkour.hooks.papi.extension.TopRunnersExtension;
import hu.stan.dreamparkour.service.course.CourseService;
import hu.stan.dreamparkour.service.toprunners.TopRunnersService;
import hu.stan.dreamplugin.annotation.core.ExternalHook;
import hu.stan.dreamplugin.core.pluginhook.PluginHook;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;

@ExternalHook(pluginName = "PlaceholderAPI")
@RequiredArgsConstructor
public class PlaceholderApiHook implements PluginHook {

  private final CourseService courseService;
  private final TopRunnersService topRunnersService;

  @Override
  public void registerHook() {
    PlaceholderAPI.registerExpansion(new TopRunnersExtension(courseService, topRunnersService));
  }

  @Override
  public void unregisterHook() {
    PlaceholderAPI.unregisterExpansion(new TopRunnersExtension(courseService, topRunnersService));
  }
}
