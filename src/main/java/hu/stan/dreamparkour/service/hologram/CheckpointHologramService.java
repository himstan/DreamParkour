package hu.stan.dreamparkour.service.hologram;

import hu.stan.dreamparkour.model.checkpoint.Checkpoint;
import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.model.hologram.Hologram;
import hu.stan.dreamparkour.util.LocationUtils;
import hu.stan.dreamweaver.annotation.core.Service;

import java.util.List;

@Service
public class CheckpointHologramService extends HologramService<Checkpoint> {

  public void placeHolograms(final Course course) {
    final var checkpoints = course.getCheckpoints();
    checkpoints.forEach(this::placeHologram);
  }

  public void placeHologram(final Checkpoint checkpoint) {
    final var course = checkpoint.getCourse();
    final var checkpoints = course.getCheckpoints();
    final var checkpointCount = checkpoints.size();
    final var nextIndex = checkpoints.indexOf(checkpoint) + 1;
    final var hologram = createHologram(checkpoint, nextIndex, checkpointCount);
    createHologram(checkpoint, hologram);
  }

  public void removeHolograms(final Course course) {
    course.getCheckpoints().forEach(
        this::removeHologram
    );
  }

  public void updateHolograms(final Course course) {
    removeHolograms(course);
    placeHolograms(course);
  }

  private Hologram createHologram(final Checkpoint checkpoint, final int index, final int checkpointCount) {
    final var holoLocation = LocationUtils.getMidpoint(
        checkpoint.getStartLocation().getLocation(),
        checkpoint.getEndLocation().getLocation())
        .add(0, 2, 0);
    return new Hologram(holoLocation, List.of(String.format("%d/%d Checkpoint", index, checkpointCount)));
  }
}
