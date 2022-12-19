package hu.stan.dreamparkour.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class CourseRun {

  private final Course course;
  private final Player player;

  @Setter
  private CheckpointNode currentCheckpoint;
  @Setter
  private CheckpointNode currentBestRunCheckpoint;
}
