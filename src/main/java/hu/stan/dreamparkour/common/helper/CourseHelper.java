package hu.stan.dreamparkour.common.helper;

import hu.stan.dreamparkour.model.Checkpoint;
import hu.stan.dreamparkour.model.Course;
import hu.stan.dreamparkour.service.CourseService;
import hu.stan.dreamplugin.annotation.core.Component;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.entity.Player;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseHelper {

  private final CourseService courseService;

  /**
   * Gets a course if the player is standing in one of the first checkpoints.
   *
   * @return The course the player is trying to start at.
   */
  public Optional<Course> getIfPlayerIsStandingOnFirstCheckpoint(final Player player) {
    return courseService.findAll().stream()
        .filter(course -> isPlayerStandingOnFirstCheckpoint(player, course))
        .findFirst();
  }

  public boolean isPlayerInCheckpoint(final Player player, final Checkpoint checkpoint) {
    final var playerLocation = player.getLocation();
    final var startLocation = checkpoint.getStartLocation().getLocation();
    final var endLocation = checkpoint.getEndLocation().getLocation();
    final double distanceToStart = Math.floor(playerLocation.distanceSquared(startLocation));
    final double distanceToEnd = Math.floor(playerLocation.distanceSquared(endLocation));
    final double regionDistance = Math.floor(startLocation.distanceSquared(endLocation));
    return distanceToStart + distanceToEnd <= regionDistance;
  }

  private boolean isPlayerStandingOnFirstCheckpoint(final Player player, final Course course) {
    if (!course.isFirstCheckpointSet() || isCourseDisabled(course)) {
      return false;
    }
    return isPlayerInCheckpoint(player, course.getFirstCheckpoint());
  }

  private boolean isCourseDisabled(final Course course) {
    return course.getCheckpoints().stream()
        .anyMatch(checkpoint -> !checkpoint.isEnabled());
  }
}
