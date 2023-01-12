package hu.stan.dreamparkour.cache.bestrun;

import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamplugin.core.cache.BaseCache;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class BestRunTotalTimeCache extends BaseCache<UUID, BestCourseRunTotalTimeCache> {

  public LocalTime getBestRunForCourse(final Player player, final Course course) {
    final var courseTimeCache = this.get(course.getCourseId());
    return courseTimeCache.get(player.getUniqueId());
  }

  public void recordBestRunForCourse(final Player player, final Course course, final LocalTime runTime) {
    recordBestRunForCourse(player.getUniqueId(), course.getCourseId(), runTime);
  }

  public void recordBestRunForCourse(final UUID playerId, final UUID courseId, final LocalTime runTime) {
    if (!courseHasAnyRun(courseId)) {
      this.add(courseId, new BestCourseRunTotalTimeCache());
    }
    this.get(courseId).add(playerId, runTime);
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
