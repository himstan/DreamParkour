package hu.stan.dreamparkour.service.toprunners;

import hu.stan.dreamparkour.cache.toprunners.CourseTopRunnerCache;
import hu.stan.dreamparkour.common.constant.PluginConstants;
import hu.stan.dreamparkour.configuration.ParkourConfiguration;
import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.model.runtime.PlayerRunTimePlaceholderData;
import hu.stan.dreamparkour.repository.RunTimeRepository;
import hu.stan.dreamparkour.repository.impl.JpaRunTimeRepository;
import hu.stan.dreamparkour.service.course.CourseService;
import hu.stan.dreamweaver.DreamWeaver;
import hu.stan.dreamweaver.annotation.core.Service;
import hu.stan.dreamweaver.core.dependency.injector.DependencyInjector;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class TopRunnersService {

  private final CourseService courseService;
  private final CourseTopRunnerCache courseTopRunnerCache;
  private final ParkourConfiguration parkourConfiguration;
  private final RunTimeRepository runTimeRepository;

  public TopRunnersService(
      final CourseService courseService,
      final CourseTopRunnerCache courseTopRunnerCache,
      final ParkourConfiguration parkourConfiguration) {
    this.courseService = courseService;
    this.courseTopRunnerCache = courseTopRunnerCache;
    this.parkourConfiguration = parkourConfiguration;
    this.runTimeRepository =
        (RunTimeRepository) DependencyInjector.getInstance().initializeClass(JpaRunTimeRepository.class);
  }

  public PlayerRunTimePlaceholderData getRunForCourseAtPlace(final UUID courseId, final int place) {
    if (!isCourseAndPlaceValid(courseId, place)) {
      return getInvalidPlayerTotalRunTime();
    }
    return courseTopRunnerCache.get(courseId).get(place - 1);
  }

  public void recordRun(final OfflinePlayer player, final Course course, final LocalTime runTime) {
    if (!courseTopRunnerCache.exists(course.getCourseId())) {
      courseTopRunnerCache.add(course.getCourseId(), new ArrayList<>());
    }
    final var topRunnerCache = courseTopRunnerCache.get(course.getCourseId());
    topRunnerCache.add(getRunTimeData(player, runTime));
    topRunnerCache.sort(PlayerRunTimePlaceholderData::compareTo);
    if (topRunnerCache.size() > parkourConfiguration.topRunsPerCourseInCache) {
      topRunnerCache.remove(parkourConfiguration.topRunsPerCourseInCache + 1);
    }
  }

  public void initTopRuns() {
    runAsync(() -> {
      final var courses = courseService.findAll();
      courses.forEach(
          this::addCourseTopRuns
      );
    });
  }

  private void addCourseTopRuns(final Course course) {
    final var topRuns = runTimeRepository.getTopRunsForCourse(course);
    topRuns.forEach(topRun -> {
      final var player = Bukkit.getOfflinePlayer(topRun.playerId());
      recordRun(player, course, topRun.runTime());
    });
  }

  private PlayerRunTimePlaceholderData getInvalidPlayerTotalRunTime() {
    return new PlayerRunTimePlaceholderData(
        "EMPTY",
        "0:00",
        null
    );
  }

  private PlayerRunTimePlaceholderData getRunTimeData(final OfflinePlayer player, final LocalTime runTime) {
    return new PlayerRunTimePlaceholderData(
        player.getName(),
        PluginConstants.TIME_FORMAT.format(runTime),
        runTime
    );
  }

  private boolean isCourseAndPlaceValid(final UUID courseId, final int place) {
    return courseTopRunnerCache.exists(courseId)
        && place > 0
        && courseTopRunnerCache.get(courseId).size() >= place;
  }

  private void runAsync(final Runnable runnable) {
    Bukkit.getScheduler().runTaskAsynchronously(DreamWeaver.getInstance(), runnable);
  }
}
