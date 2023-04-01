package hu.stan.dreamparkour.service.course;

import hu.stan.dreamparkour.cache.course.CheckpointBuilderCache;
import hu.stan.dreamparkour.cache.course.CourseBuilderCache;
import hu.stan.dreamparkour.model.checkpoint.Checkpoint;
import hu.stan.dreamparkour.model.checkpoint.CheckpointLocation;
import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamplugin.annotation.core.Service;
import hu.stan.dreamplugin.core.translation.Translate;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CourseBuilderService {

  private final CourseService courseService;
  private final CourseBuilderCache courseBuilderCache;
  private final CheckpointBuilderCache checkpointBuilderCache;

  public boolean isPlayerCourseBuilder(final Player player) {
    return courseBuilderCache.exists(player.getUniqueId());
  }

  public void setPlayerAsCourseBuilder(final Player player, final Course course) {
    courseBuilderCache.add(player.getUniqueId(), course.getCourseId());
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
            roundCheckpointLocations(checkpoint);
            checkpoint.setCourse(course);
            course.addCheckpoint(checkpoint);
            courseService.saveCourse(course);
            removeCheckpointFromBuilder(player);
            Translate.sendTo(player,"course.checkpoint-created");
          },
          () -> Translate.sendTo(player,"course.not-present-anymore")
      );
    }
  }

  private void roundCheckpointLocations(final Checkpoint checkpoint) {
    checkpoint.getStartLocation().getLocation().setX(Math.floor(checkpoint.getStartLocation().getLocation().getX()));
    checkpoint.getStartLocation().getLocation().setY(Math.floor(checkpoint.getStartLocation().getLocation().getY()));
    checkpoint.getStartLocation().getLocation().setZ(Math.floor(checkpoint.getStartLocation().getLocation().getZ()));
    checkpoint.getEndLocation().getLocation().setX(Math.floor(checkpoint.getEndLocation().getLocation().getX() + 1));
    checkpoint.getEndLocation().getLocation().setY(Math.floor(checkpoint.getEndLocation().getLocation().getY() + 1));
    checkpoint.getEndLocation().getLocation().setZ(Math.floor(checkpoint.getEndLocation().getLocation().getZ() + 1));
  }

  private void handleFlatCheckpoint(final Checkpoint checkpoint) {
    if (getHeighDifferenceBetweenLocations(checkpoint) < 1) {
      final var y = checkpoint.getStartLocation().getLocation().getBlockY();
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
