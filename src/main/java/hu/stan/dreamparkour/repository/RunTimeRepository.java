package hu.stan.dreamparkour.repository;

import hu.stan.dreamparkour.model.TotalRunTime;
import hu.stan.dreamparkour.model.checkpoint.CheckpointNode;
import hu.stan.dreamparkour.model.course.Course;
import java.util.List;
import org.bukkit.entity.Player;

public interface RunTimeRepository {

  void save(final TotalRunTime totalRunTime);

  void save(final CheckpointNode checkpointNode, final TotalRunTime totalRunTime);

  CheckpointNode getBestSplitTimesForCourseAndPlayer(Course course, Player player);

  List<TotalRunTime> getBestTimesForCoursePerPlayer(final Course course);
}
