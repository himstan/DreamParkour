package hu.stan.dreamparkour.model.checkpoint;

import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class CheckpointNode {

  private UUID nodeId;
  private UUID playerId;
  private Checkpoint checkpoint;
  private LocalTime lastCheckpointTime;

  @Setter
  @Getter
  private CheckpointNode lastCheckpoint;
  @Setter
  @Getter
  private CheckpointNode nextCheckpoint;

  public void recordCheckpointTime(final LocalTime checkpointTime) {
    this.lastCheckpointTime = checkpointTime;
  }

  public boolean isFirstCheckpoint() {
    return Objects.isNull(lastCheckpoint);
  }

  public boolean isLastCheckpoint() {
    return Objects.isNull(nextCheckpoint);
  }
}
