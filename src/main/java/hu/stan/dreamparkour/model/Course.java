package hu.stan.dreamparkour.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Course {

  @Getter
  private final UUID courseId;

  @Getter
  @Setter
  private String courseName;

  @Getter
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

  public void addCheckpoint(final Checkpoint checkpoint) {
    checkpoints.add(checkpoint);
  }

  public void removeLastCheckpoint() {
    checkpoints.remove(checkpoints.size() - 1);
  }
}
