package hu.stan.dreamparkour.module;

import hu.stan.dreamparkour.service.course.CourseBuilderService;
import hu.stan.dreamplugin.annotation.core.Module;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

@Module
@RequiredArgsConstructor
public class CourseBuilderModule implements Listener {

  final CourseBuilderService courseBuilderService;

  @EventHandler
  public void onPlayerClickBlock(final PlayerInteractEvent event) {
    final var player = event.getPlayer();
    if (!courseBuilderService.isPlayerCourseBuilder(player)) {
      return;
    }
    switch (event.getAction()) {
      case LEFT_CLICK_BLOCK -> {
        final var block = event.getClickedBlock();
        courseBuilderService.setStartLocation(player, block.getLocation());
        event.setCancelled(true);
      }
      case RIGHT_CLICK_BLOCK -> {
        final var block = event.getClickedBlock();
        courseBuilderService.setEndLocation(player, block.getLocation());
        event.setCancelled(true);
      }
    }
  }
}
