package hu.stan.dreamparkour.model.course;

import hu.stan.dreamparkour.event.checkpoint.CreateCheckpointEvent;
import hu.stan.dreamparkour.event.checkpoint.RemoveCheckpointEvent;
import hu.stan.dreamparkour.model.checkpoint.Checkpoint;
import hu.stan.dreamplugin.exception.DreamPluginException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class Course {

  @Getter
  private final UUID courseId;

  @Getter
  @Setter
  private String courseName;

  private final List<Checkpoint> checkpoints;

  public Course(final String courseName) {
    this.courseId = UUID.randomUUID();
    this.courseName = courseName;
    this.checkpoints = new ArrayList<>();
  }

  public boolean isFirstCheckpointSet() {
    return !checkpoints.isEmpty();
  }

  public Checkpoint getFirstCheckpoint() {
    return checkpoints.get(0);
  }

  public List<Checkpoint> getCheckpoints() {
    return this.checkpoints.stream()
        .filter(checkpoint -> !checkpoint.isDeleted())
        .toList();
  }

  public List<Checkpoint> getCheckpointsWithDeleted() {
    return this.checkpoints;
  }

  public void addCheckpoint(final Checkpoint checkpoint) {
    checkpoints.add(checkpoint);
    Bukkit.getPluginManager().callEvent(new CreateCheckpointEvent(checkpoint));
  }

  public void removeCheckpoint(final Checkpoint checkpoint) {
    if (checkpoints.contains(checkpoint)) {
      checkpoint.setDeleted(true);
      Bukkit.getPluginManager().callEvent(new RemoveCheckpointEvent(checkpoint));
    } else {
      throw new DreamPluginException(String.format("Checkpoint: [%s] is not part of course: [%s]", checkpoint.getCheckpointId(), courseId));
    }
  }

  public void removeLastCheckpoint() {
    final var checkpoints = getCheckpoints();
    final var checkpointSize = checkpoints.size();
    final var checkpoint = checkpoints.get(checkpointSize - 1);
    removeCheckpoint(checkpoint);
  }
}
