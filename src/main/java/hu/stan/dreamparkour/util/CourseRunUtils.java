package hu.stan.dreamparkour.util;

import hu.stan.dreamparkour.model.CheckpointNode;
import java.time.LocalTime;

public final class CourseRunUtils {

  private CourseRunUtils() {}

  public static CheckpointNode rewindCheckpointToStart(final CheckpointNode checkpointNode) {
    var checkpoint = checkpointNode;
    while (!checkpoint.isFirstCheckpoint()) {
      checkpoint = checkpoint.getLastCheckpoint();
    }
    return checkpoint;
  }

  public static LocalTime calculateRunTime(final LocalTime startTime, final LocalTime endTime) {
    return LocalTime.ofNanoOfDay(Math.abs(endTime.toNanoOfDay() - startTime.toNanoOfDay()));
  }
}
