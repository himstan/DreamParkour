package hu.stan.dreamparkour.repository.impl;

import hu.stan.dreamparkour.model.CheckpointNode;
import hu.stan.dreamparkour.model.Course;
import hu.stan.dreamparkour.model.TotalRunTime;
import hu.stan.dreamparkour.repository.RunTimeRepository;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;

public class EmptyRunTimeRepository implements RunTimeRepository {

  @Override
  public void save(final TotalRunTime totalRunTime) {

  }

  @Override
  public void save(final CheckpointNode checkpointNode, final TotalRunTime totalRunTime) {

  }

  @Override
  public CheckpointNode getBestSplitTimesForCourseAndPlayer(final Course course, final Player player) {
    return null;
  }

  @Override
  public List<TotalRunTime> getBestTimesForCoursePerPlayer(final Course course) {
    return Collections.emptyList();
  }
}
