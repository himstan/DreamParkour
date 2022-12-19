package hu.stan.dreamparkour.common.helper;

import hu.stan.dreamparkour.model.Checkpoint;
import hu.stan.dreamparkour.model.CheckpointNode;
import hu.stan.dreamparkour.model.Course;
import hu.stan.dreamparkour.model.CourseRun;
import hu.stan.dreamparkour.service.BestTimeService;
import hu.stan.dreamplugin.annotation.core.Component;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Component
@RequiredArgsConstructor
public class CourseRunHelper {

  private final BestTimeService bestTimeService;

  public CourseRun buildCourseRun(final Player player, final Course course) {
    final var currentRun = buildNewCheckpointNodes(course, player);
    final var bestRun = getBestTime(player, course, currentRun);
    return new CourseRun(course, player, currentRun, bestRun);
  }

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
