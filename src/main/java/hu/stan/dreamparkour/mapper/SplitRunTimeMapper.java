package hu.stan.dreamparkour.mapper;

import hu.stan.dreamparkour.model.CheckpointNode;
import hu.stan.dreamparkour.model.entity.DbSplitRunTime;
import hu.stan.dreamparkour.model.entity.DbTotalRunTime;
import hu.stan.dreamparkour.util.CourseRunUtils;
import hu.stan.dreamplugin.annotation.core.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SplitRunTimeMapper {

  private final CheckpointMapper checkpointMapper;

  public CheckpointNode toCheckpointNode(final List<DbSplitRunTime> splitRunTimes) {
    return buildNewCheckpointNodes(splitRunTimes);
  }

  public List<DbSplitRunTime> toDbSplitRunTimes(final CheckpointNode checkpointNode, final UUID totalRunId) {
    var node = CourseRunUtils.rewindCheckpointToStart(checkpointNode);
    final var dbSplitRunTimes = new ArrayList<DbSplitRunTime>();
    do {
      dbSplitRunTimes.add(toDbSplitRunTime(node, totalRunId));
      node = node.getNextCheckpoint();
    } while (!node.isLastCheckpoint());
    dbSplitRunTimes.add(toDbSplitRunTime(node, totalRunId));
    return dbSplitRunTimes;
  }

  private CheckpointNode buildNewCheckpointNodes(final List<DbSplitRunTime> splitRunTimes) {
    final var checkpointNodes = mapToCheckpointNode(splitRunTimes);
    connectCheckpointNodes(checkpointNodes);
    return getFirstCheckpointNode(checkpointNodes);
  }

  private DbSplitRunTime toDbSplitRunTime(final CheckpointNode checkpointNode, final UUID totalRunId) {
    final DbSplitRunTime dbSplitRunTime = new DbSplitRunTime();
    dbSplitRunTime.setSplitRunId(checkpointNode.getNodeId().toString());
    dbSplitRunTime.setSplitTime(checkpointNode.getLastCheckpointTime());
    dbSplitRunTime.setPlayerId(checkpointNode.getPlayerId().toString());
    dbSplitRunTime.setCheckpoint(checkpointMapper.toDbCheckpoint(checkpointNode.getCheckpoint()));
    final var totalRun = new DbTotalRunTime();
    totalRun.setRunId(totalRunId.toString());
    dbSplitRunTime.setTotalRun(totalRun);
    return dbSplitRunTime;
  }

  private CheckpointNode getFirstCheckpointNode(final List<CheckpointNode> checkpointNodes) {
    return checkpointNodes.isEmpty() ? null : checkpointNodes.get(0);
  }

  private void connectCheckpointNodes(final List<CheckpointNode> checkpointNodes) {
    for (int i = 0; i < checkpointNodes.size(); i++) {
      if (i < checkpointNodes.size() - 1) {
        checkpointNodes.get(i).setNextCheckpoint(checkpointNodes.get(i + 1));
      }
      if (i > 0) {
        checkpointNodes.get(i).setLastCheckpoint(checkpointNodes.get(i - 1));
      }
    }
  }

  private List<CheckpointNode> mapToCheckpointNode(final List<DbSplitRunTime> splitRunTimes) {
    return splitRunTimes.stream().map(
            this::mapToCheckpoint)
        .toList();
  }

  private CheckpointNode mapToCheckpoint(final DbSplitRunTime splitRunTime) {
    final var checkpoint = checkpointMapper.toCheckpoint(splitRunTime.getCheckpoint());
    return new CheckpointNode(
        UUID.fromString(splitRunTime.getSplitRunId()),
        UUID.fromString(splitRunTime.getPlayerId()),
        checkpoint,
        splitRunTime.getSplitTime(),
        null,
        null);
  }
}
