package hu.stan.dreamparkour.common.helper;

import hu.stan.dreamparkour.model.checkpoint.Checkpoint;
import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.service.course.CourseService;
import hu.stan.dreamweaver.annotation.core.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Optional;

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

  /**
   * Checks if the player is inside the given checkpoint.
   *
   * @param player     The player we are checking.
   * @param checkpoint The checkpoint we are checking.
   *
   * @return true, if the player is inside the checkpoint, false otherwise.
   */
  public boolean isPlayerInCheckpoint(final Player player, final Checkpoint checkpoint) {
    if (!isPlayerAndCheckpointInTheSameWorld(player, checkpoint)) {
      return false;
    }
    final var playerLocation = player.getLocation();
    final var startLocation = checkpoint.getStartLocation().getLocation();
    final var endLocation = checkpoint.getEndLocation().getLocation();
    final double distanceToStart = Math.floor(playerLocation.distanceSquared(startLocation));
    final double distanceToEnd = Math.ceil(playerLocation.distanceSquared(endLocation));
    final double regionDistance = Math.floor(startLocation.distanceSquared(endLocation));
    return distanceToStart + distanceToEnd <= regionDistance;
  }

  private boolean isPlayerAndCheckpointInTheSameWorld(final Player player, final Checkpoint checkpoint) {
    final var playerWorld = player.getLocation().getWorld();
    final var checkpointWorld = checkpoint.getStartLocation().getLocation().getWorld();
    return Objects.equals(playerWorld, checkpointWorld);
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
