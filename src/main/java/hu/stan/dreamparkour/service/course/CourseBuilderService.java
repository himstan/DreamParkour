package hu.stan.dreamparkour.service.course;

import hu.stan.dreamparkour.cache.course.CheckpointBuilderCache;
import hu.stan.dreamparkour.cache.course.CourseBuilderCache;
import hu.stan.dreamparkour.model.checkpoint.Checkpoint;
import hu.stan.dreamparkour.model.checkpoint.CheckpointLocation;
import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamplugin.annotation.core.Service;
import hu.stan.dreamplugin.core.translation.Translate;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Service
@RequiredArgsConstructor
public class CourseBuilderService {

  private final CourseService courseService;
  private final CourseBuilderCache courseBuilderCache;
  private final CheckpointBuilderCache checkpointBuilderCache;

  public boolean isPlayerBuildingCheckpoint(final Player player) {
    return checkpointBuilderCache.exists(player.getUniqueId());
  }

  public boolean isPlayerCourseBuilder(final Player player) {
    return courseBuilderCache.exists(player.getUniqueId());
  }

  public void setPlayerAsCourseBuilder(final Player player, final Course course) {
    courseBuilderCache.add(player.getUniqueId(), course.getCourseId());
  }

  public void removePlayerFromCourseBuilder(final Player player) {
    courseBuilderCache.remove(player.getUniqueId());
  }

  public void setStartLocation(final Player player, final Location location) {
    final var checkpoint = getCheckpoint(player);
    checkpoint.setStartLocation(new CheckpointLocation(location));
    saveCheckpoint(player, checkpoint);
    Translate.sendTo(player,"course.set-start-checkpoint-location");
    addCheckpointIfFinished(player, checkpoint);
  }

  public void setEndLocation(final Player player, final Location location) {
    final var checkpoint = getCheckpoint(player);
    checkpoint.setEndLocation(new CheckpointLocation(location));
    saveCheckpoint(player, checkpoint);
    Translate.sendTo(player,"course.set-end-checkpoint-location");
    addCheckpointIfFinished(player, checkpoint);
  }

  public void saveCheckpoint(final Player player, final Checkpoint checkpoint) {
    checkpointBuilderCache.add(player.getUniqueId(), checkpoint);
  }

  public void removeCheckpointFromBuilder(final Player player) {
    courseBuilderCache.remove(player.getUniqueId());
    checkpointBuilderCache.remove(player.getUniqueId());
  }

  public Checkpoint getCheckpoint(final Player player) {
    return checkpointBuilderCache.exists(player.getUniqueId())
        ? checkpointBuilderCache.get(player.getUniqueId())
        : new Checkpoint(null, null);
  }

  private void addCheckpointIfFinished(final Player player, final Checkpoint checkpoint) {
    if (bothLocationsAreSet(checkpoint)) {
      final var courseId = courseBuilderCache.get(player.getUniqueId());
      courseService.findCourseBy(courseId).ifPresentOrElse(
          course -> {
            handleFlatCheckpoint(checkpoint);
            course.addCheckpoint(checkpoint);
            courseService.saveCourse(course);
            removeCheckpointFromBuilder(player);
            Translate.sendTo(player,"course.checkpoint-created");
          },
          () -> Translate.sendTo(player,"course.not-present-anymore")
      );
    }
  }

  private void handleFlatCheckpoint(final Checkpoint checkpoint) {
    if (getHeighDifferenceBetweenLocations(checkpoint) < 1) {
      final var y = checkpoint.getStartLocation().getLocation().getBlockY();
      checkpoint.getStartLocation().getLocation().setY(y - 1d);
      checkpoint.getEndLocation().getLocation().setY(y + 1d);
    }
  }

  private int getHeighDifferenceBetweenLocations(final Checkpoint checkpoint) {
    final var firstLocation = checkpoint.getStartLocation().getLocation().getBlockY();
    final var endLocation = checkpoint.getEndLocation().getLocation().getBlockY();
    return Math.abs(endLocation - firstLocation);
  }

  private boolean bothLocationsAreSet(final Checkpoint checkpoint) {
    return Objects.nonNull(checkpoint.getStartLocation()) && Objects.nonNull(checkpoint.getEndLocation());
  }
}
