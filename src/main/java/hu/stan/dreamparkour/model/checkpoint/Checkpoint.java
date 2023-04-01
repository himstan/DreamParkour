package hu.stan.dreamparkour.model.checkpoint;

import hu.stan.dreamparkour.event.checkpoint.UpdateCheckpointEvent;
import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.model.hologram.HologramReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public final class Checkpoint implements HologramReference {

  public Checkpoint(final CheckpointLocation startLocation, final CheckpointLocation endLocation) {
    this(false, startLocation, endLocation);
  }

  public Checkpoint(final boolean isEnabled, final CheckpointLocation startLocation, final CheckpointLocation endLocation) {
    this(UUID.randomUUID(), null, isEnabled, startLocation, endLocation, LocalDateTime.now(), false);
  }

  public Checkpoint(final Course course, final CheckpointLocation startLocation, final CheckpointLocation endLocation) {
    this(course, false, startLocation, endLocation);
  }

  public Checkpoint(final Course course, final boolean isEnabled, final CheckpointLocation startLocation, final CheckpointLocation endLocation) {
    this(UUID.randomUUID(), course, isEnabled, startLocation, endLocation, LocalDateTime.now(), false);
  }

  public Checkpoint(UUID checkpointId, boolean isEnabled, CheckpointLocation startLocation, CheckpointLocation endLocation, LocalDateTime createdAt, boolean deleted) {
    this(checkpointId, null, isEnabled, startLocation, endLocation, createdAt, deleted);
  }

  private final UUID checkpointId;

  @Getter
  @Setter
  private Course course;

  @Getter
  private boolean isEnabled;

  @Getter
  @Setter
  private CheckpointLocation startLocation;

  @Getter
  @Setter
  private CheckpointLocation endLocation;

  @Getter
  private LocalDateTime createdAt;

  @Setter
  @Getter
  private boolean deleted;

  public void setEnabled(boolean enabled) {
    this.isEnabled = enabled;
    Bukkit.getPluginManager().callEvent(new UpdateCheckpointEvent(this));
  }

  @Override
  public Object getKey() {
    return checkpointId;
  }
}
