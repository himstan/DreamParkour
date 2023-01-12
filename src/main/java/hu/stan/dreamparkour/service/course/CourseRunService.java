package hu.stan.dreamparkour.service.course;

import hu.stan.dreamparkour.cache.course.CourseRunCache;
import hu.stan.dreamparkour.cache.time.StartTimeCache;
import hu.stan.dreamparkour.common.helper.CourseRunHelper;
import hu.stan.dreamparkour.exception.RunTimeNotStartedException;
import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.model.course.CourseRun;
import hu.stan.dreamparkour.util.CourseRunUtils;
import hu.stan.dreamplugin.annotation.core.Service;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.entity.Player;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseRunService {

  private final StartTimeCache startTimeCache;
  private final CourseRunCache courseRunCache;
  private final CourseRunHelper courseRunHelper;

  public CourseRun startCourseRun(final Player player, final Course course) {
    final var startTime = LocalTime.now();
    final var courseRun = courseRunHelper.buildCourseRun(player, course);
    startTimeCache.add(player.getUniqueId(), startTime);
    courseRunCache.add(player.getUniqueId(), courseRun);
    return courseRun;
  }

  public CourseRun getCourseRun(final Player player) {
    return courseRunCache.get(player.getUniqueId());
  }

  public LocalTime stopRunTimer(final Player player) {
    if (!startTimeCache.exists(player.getUniqueId())) {
      throw new RunTimeNotStartedException();
    }
    final var runTime = getRunTime(player);
    cancelRun(player);
    return runTime;
  }

  public void cancelRun(final Player player) {
    startTimeCache.remove(player.getUniqueId());
    courseRunCache.remove(player.getUniqueId());
    courseRunCache.remove(player.getUniqueId());
  }

  public boolean hasRunningTime(final Player player) {
    return courseRunCache.exists(player.getUniqueId());
  }

  public LocalTime getRunTime(final Player player) {
    final var startTime = startTimeCache.get(player.getUniqueId());
    return calculateRunTime(startTime);
  }

  private LocalTime calculateRunTime(final LocalTime startTime) {
    final var timeNow = LocalTime.now();
    return CourseRunUtils.calculateRunTime(startTime, timeNow);
  }
}
