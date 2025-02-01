package hu.stan.dreamparkour.common.helper;

import hu.stan.dreamparkour.model.checkpoint.Checkpoint;
import hu.stan.dreamparkour.model.checkpoint.CheckpointNode;
import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.model.course.CourseRun;
import hu.stan.dreamparkour.service.runtime.BestTimeService;
import hu.stan.dreamweaver.annotation.core.Component;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Component
@RequiredArgsConstructor
public class CourseRunHelper {

  private final BestTimeService bestTimeService;

  /**
   * Builds a course run for the player on the given course.
   *
   * @param player The player we want to build a run for.
   * @param course The course we are building a run for.
   *
   * @return The built run which is containing the checkpoint nodes for the player's run.
   */
  public CourseRun buildCourseRun(final Player player, final Course course) {
    final var currentRun = buildNewCheckpointNodes(course, player);
    final var bestRun = getBestTime(player, course, currentRun);
    return new CourseRun(course, player, currentRun, bestRun);
  }

  /**
   * Connects the given checkpoints into a checkpoint node object.
   *
   * @param checkpoints The checkpoint list we want to connect together with nodes.
   * @param player      The player that the checkpoint nodes are assigned to.
   *
   * @return The checkpoint node that has the player's data and all the checkpoints connected in order.
   */
  public CheckpointNode buildNewCheckpointNodes(final List<Checkpoint> checkpoints, final Player player) {
    final var checkpointNodes = mapToCheckpointNode(checkpoints, player);
    connectCheckpointNodes(checkpointNodes);
    return getFirstCheckpointNode(checkpointNodes);
  }

  private CheckpointNode getBestTime(final Player player, final Course course, final CheckpointNode currentRun) {
    return bestTimeService.hasBestRunSplitTimes(player, course)
        ? bestTimeService.getBestRunSplitTimes(player, course)
        : currentRun;
  }

  private CheckpointNode buildNewCheckpointNodes(final Course course, final Player player) {
    return buildNewCheckpointNodes(course.getCheckpoints(), player);
  }

  private CheckpointNode getFirstCheckpointNode(final List<CheckpointNode> checkpointNodes) {
    return checkpointNodes.get(0);
  }

  private void connectCheckpointNodes(final List<CheckpointNode> checkpointNodes) {
    for (int i = checkpointNodes.size() - 1; i >= 0; i--) {
      if (i < checkpointNodes.size() - 1) {
        checkpointNodes.get(i).setNextCheckpoint(checkpointNodes.get(i + 1));
      }
      if (i > 0) {
        checkpointNodes.get(i).setLastCheckpoint(checkpointNodes.get(i - 1));
      }
    }
  }

  private List<CheckpointNode> mapToCheckpointNode(final List<Checkpoint> checkpoints, final Player player) {
    return checkpoints.stream().map(
        checkpoint -> mapToCheckpoint(checkpoint, player))
        .toList();
  }

  private CheckpointNode mapToCheckpoint(final Checkpoint checkpoint, final Player player) {
    return new CheckpointNode(
        UUID.randomUUID(),
        player.getUniqueId(),
        checkpoint,
        null,
        null,
        null);
  }
}
