package hu.stan.dreamparkour.model.checkpoint;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public final class Checkpoint {

  public Checkpoint(final CheckpointLocation startLocation, final CheckpointLocation endLocation) {
    this(false, startLocation, endLocation);
  }

  public Checkpoint(final boolean isEnabled, final CheckpointLocation startLocation, final CheckpointLocation endLocation) {
    this(UUID.randomUUID(), isEnabled, startLocation, endLocation, LocalDateTime.now());
  }

  private final UUID checkpointId;
  @Getter
  @Setter
  private boolean isEnabled;
  @Getter
  @Setter
  private CheckpointLocation startLocation;
  @Getter
  @Setter
  private CheckpointLocation endLocation;
  @Getter
  private LocalDateTime createdAt;
}
