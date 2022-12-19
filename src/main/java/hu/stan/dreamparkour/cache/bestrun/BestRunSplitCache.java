package hu.stan.dreamparkour.cache.bestrun;

import hu.stan.dreamparkour.model.CheckpointNode;
import hu.stan.dreamparkour.model.Course;
import hu.stan.dreamparkour.util.CourseRunUtils;
import hu.stan.dreamplugin.core.cache.BaseCache;
import java.util.UUID;
import org.bukkit.entity.Player;

public class BestRunSplitCache extends BaseCache<UUID, BestRunCourseSplitCache> {

  public CheckpointNode getBestRunForCourse(final Player player, final Course course) {
    final var courseTimeCache = this.get(course.getCourseId());
    return courseTimeCache.get(player.getUniqueId());
  }

  public void recordBestRunForCourse(final Player player, final Course course, final CheckpointNode checkpointNode) {
    recordBestRunForCourse(player.getUniqueId(), course.getCourseId(), CourseRunUtils.rewindCheckpointToStart(checkpointNode));
  }

  public void recordBestRunForCourse(final UUID playerId, final UUID courseId, final CheckpointNode checkpointNode) {
    if (!courseHasAnyRun(courseId)) {
      this.add(courseId, new BestRunCourseSplitCache());
    }
    this.get(courseId).add(playerId, CourseRunUtils.rewindCheckpointToStart(checkpointNode));
  }

  public boolean hasRunForCourse(final Player player, final Course course) {
    return courseHasAnyRun(course) && this.get(course.getCourseId()).exists(player.getUniqueId());
  }

  public boolean courseHasAnyRun(final Course course) {
    return courseHasAnyRun(course.getCourseId());
  }

  public boolean courseHasAnyRun(final UUID courseId) {
    return this.exists(courseId);
  }
}
