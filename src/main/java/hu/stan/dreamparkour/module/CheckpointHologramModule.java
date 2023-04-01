package hu.stan.dreamparkour.module;


import hu.stan.dreamparkour.DreamParkour;
import hu.stan.dreamparkour.event.checkpoint.CreateCheckpointEvent;
import hu.stan.dreamparkour.event.checkpoint.RemoveCheckpointEvent;
import hu.stan.dreamparkour.event.checkpoint.UpdateCheckpointEvent;
import hu.stan.dreamparkour.event.course.InitializeCourseEvent;
import hu.stan.dreamparkour.service.hologram.CheckpointHologramService;
import hu.stan.dreamplugin.annotation.core.Module;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

@Module
@RequiredArgsConstructor
public class CheckpointHologramModule implements Listener {

  private final CheckpointHologramService checkpointHologramService;

  @EventHandler
  public void onDisable(final PluginDisableEvent event) {
    if (event.getPlugin() instanceof DreamParkour) {
      checkpointHologramService.clearEveryDreamHologram();
    }
  }

  @EventHandler
  public void onCourseInitialize(final InitializeCourseEvent event) {
    checkpointHologramService.placeHolograms(event.getCourse());
  }

  @EventHandler
  public void onCheckpointCreate(final CreateCheckpointEvent event) {
    final var course = event.getCreatedCheckpoint().getCourse();
    checkpointHologramService.updateHolograms(course);
  }

  @EventHandler
  public void onCheckpointUpdate(final UpdateCheckpointEvent event) {
    final var course = event.getUpdatedCheckpoint().getCourse();
    checkpointHologramService.updateHolograms(course);
  }

  @EventHandler
  public void onCheckpointRemove(final RemoveCheckpointEvent event) {
    final var checkpoint = event.getRemovedCheckpoint();
    final var course = checkpoint.getCourse();
    checkpointHologramService.removeHologram(checkpoint);
    checkpointHologramService.updateHolograms(course);
  }
}
