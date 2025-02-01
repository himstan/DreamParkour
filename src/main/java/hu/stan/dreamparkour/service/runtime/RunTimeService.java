package hu.stan.dreamparkour.service.runtime;

import hu.stan.dreamparkour.model.TotalRunTime;
import hu.stan.dreamparkour.model.checkpoint.CheckpointNode;
import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.model.course.CourseRun;
import hu.stan.dreamparkour.repository.RunTimeRepository;
import hu.stan.dreamparkour.repository.impl.JpaRunTimeRepository;
import hu.stan.dreamparkour.service.course.CourseService;
import hu.stan.dreamparkour.service.toprunners.TopRunnersService;
import hu.stan.dreamweaver.DreamWeaver;
import hu.stan.dreamweaver.annotation.core.Service;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class RunTimeService {

  private final BestTimeService bestTimeService;
  private final RunTimeRepository runTimeRepository;
  private final CourseService courseService;
  private final TopRunnersService topRunnersService;

  public RunTimeService(
      final BestTimeService bestTimeService,
      final CourseService courseService,
      final TopRunnersService topRunnersService) {
    this.bestTimeService = bestTimeService;
    this.courseService = courseService;
    this.topRunnersService = topRunnersService;
    this.runTimeRepository = new JpaRunTimeRepository();
    initBestTimes();
  }

  public void recordRunTime(final Player player, final CourseRun courseRun, final LocalTime runTime) {
    final var course = courseRun.getCourse();
    if (!bestTimeService.hasBestRunTotalTime(player, course)) {
      bestTimeService.recordBestTotalRunTime(player, course, runTime);
      bestTimeService.recordBestRunSplitTimes(player, course, courseRun.getCurrentCheckpoint());
      log.info("Player [{}] has ran their first time! Time: [{}]",
          player.getName(), runTime);
    }
    final var bestTime = bestTimeService.getBestRunTotalTime(player, course);
    if (bestTimeService.hasBestRunTotalTime(player, course) && runTime.isBefore(bestTime)) {
      bestTimeService.recordBestTotalRunTime(player, course, runTime);
      bestTimeService.recordBestRunSplitTimes(player, course, courseRun.getCurrentCheckpoint());
      log.info("Player [{}] has ran a new best time in course: [{}]! Time: [{}]",
          player.getName(), course.getCourseName(), runTime);
    }
    topRunnersService.recordRun(player, courseRun.getCourse(), runTime);
    persistRunTime(player, runTime, course, courseRun.getCurrentCheckpoint());
  }

  public void initBestTimeForPlayer(final Player player) {
    Bukkit.getScheduler().runTaskAsynchronously(DreamWeaver.getInstance(),
        () -> courseService.findAll().forEach(course -> {
          final var bestSplitTime = runTimeRepository.getBestSplitTimesForCourseAndPlayer(course, player);
          if (Objects.nonNull(bestSplitTime)) {
            bestTimeService.recordBestRunSplitTimes(player, course, bestSplitTime);
          }
        }));
  }

  public void clearBestTimeCacheForPlayer(final Player player) {
    Bukkit.getScheduler().runTaskAsynchronously(DreamWeaver.getInstance(),
        () -> clearBestTimeCacheForPlayer(player));
  }

  private void persistRunTime(
      final Player player,
      final LocalTime runTime,
      final Course course,
      final CheckpointNode currentRun) {
    Bukkit.getScheduler().runTaskAsynchronously(DreamWeaver.getInstance(), () -> {
      final var totalRunTime = buildTotalRunTime(player, course, runTime);
      runTimeRepository.save(totalRunTime);
      runTimeRepository.save(currentRun, totalRunTime);
    });
  }

  private TotalRunTime buildTotalRunTime(
      final Player player, final Course cource, final LocalTime runTime) {
    return new TotalRunTime(UUID.randomUUID(), cource.getCourseId(), player.getUniqueId(), runTime);
  }

  private void initBestTimes() {
    Bukkit.getScheduler().runTaskAsynchronously(DreamWeaver.getInstance(),
        () -> courseService.findAll().forEach(course -> {
          initBestTimesForCourse(course);
          initBestSplitTimesForOnlinePlayers(course);
        }));
  }

  private void initBestSplitTimesForOnlinePlayers(final Course course) {
    Bukkit.getServer().getOnlinePlayers().forEach(
        player -> {
          final var bestSplitTime = runTimeRepository.getBestSplitTimesForCourseAndPlayer(course, player);
          if (Objects.nonNull(bestSplitTime)) {
            bestTimeService.recordBestRunSplitTimes(player, course, bestSplitTime);
          }
        }
    );
  }

  private void initBestTimesForCourse(final Course course) {
    runTimeRepository.getBestTimesForCoursePerPlayer(course).forEach(
        bestTotalTime -> bestTimeService.recordBestTotalRunTime(
            bestTotalTime.playerId(), bestTotalTime.courseId(), bestTotalTime.runTime())
    );
  }
}
