package hu.stan.dreamparkour.service;

import hu.stan.dreamparkour.cache.bestrun.BestRunSplitCache;
import hu.stan.dreamparkour.cache.bestrun.BestRunTotalTimeCache;
import hu.stan.dreamparkour.model.CheckpointNode;
import hu.stan.dreamparkour.model.Course;
import hu.stan.dreamplugin.annotation.core.Service;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@Service
@AllArgsConstructor
public class BestTimeService {

  private final BestRunTotalTimeCache bestRunTotalTimeCache;
  private final BestRunSplitCache bestRunSplitCache;

  public void recordBestTotalRunTime(final Player player, final Course course, final LocalTime bestRun) {
    recordBestTotalRunTime(player.getUniqueId(), course.getCourseId(), bestRun);
  }

  public void recordBestTotalRunTime(final UUID playerId, final UUID courseID, final LocalTime bestRun) {
    bestRunTotalTimeCache.recordBestRunForCourse(playerId, courseID, bestRun);
  }

  public void recordBestRunSplitTimes(final Player player, final Course course, final CheckpointNode bestRun) {
    bestRunSplitCache.recordBestRunForCourse(player, course, bestRun);
  }

  public CheckpointNode getBestRunSplitTimes(final Player player, final Course course) {
    return bestRunSplitCache.getBestRunForCourse(player, course);
  }

  public boolean hasBestRunSplitTimes(final Player player, final Course course) {
    return bestRunSplitCache.hasRunForCourse(player, course);
  }

  public LocalTime getBestRunTotalTime(final Player player, final Course course) {
    return bestRunTotalTimeCache.getBestRunForCourse(player, course);
  }

  public boolean hasBestRunTotalTime(final Player player, final Course course) {
    return bestRunTotalTimeCache.hasRunForCourse(player, course);
  }

  public void clearBestTimesForPlayer(final Player player) {
    bestRunTotalTimeCache.getAll().forEach(
        cache -> cache.remove(player.getUniqueId())
    );
  }
}
