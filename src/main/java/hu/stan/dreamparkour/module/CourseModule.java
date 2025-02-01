package hu.stan.dreamparkour.module;

import static hu.stan.dreamparkour.common.constant.PluginConstants.SHORT_TIME_FORMAT;
import static hu.stan.dreamparkour.common.constant.PluginConstants.TIME_FORMAT;
import static hu.stan.dreamparkour.util.CourseRunUtils.calculateRunTime;

import hu.stan.dreamparkour.common.helper.CourseHelper;
import hu.stan.dreamparkour.event.checkpoint.PlayerHitCheckpoint;
import hu.stan.dreamparkour.model.checkpoint.Checkpoint;
import hu.stan.dreamparkour.model.checkpoint.CheckpointNode;
import hu.stan.dreamparkour.model.course.Course;
import hu.stan.dreamparkour.model.course.CourseRun;
import hu.stan.dreamparkour.service.course.CourseRunService;
import hu.stan.dreamparkour.service.runtime.BestTimeService;
import hu.stan.dreamparkour.service.runtime.RunTimeDisplayService;
import hu.stan.dreamparkour.service.runtime.RunTimeService;
import hu.stan.dreamweaver.annotation.core.Module;
import java.time.LocalTime;
import java.util.Objects;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Slf4j
@Module
@RequiredArgsConstructor
public class CourseModule implements Listener {

  private final RunTimeDisplayService runTimeDisplayService;
  private final CourseHelper courseHelper;
  private final BestTimeService bestTimeService;
  private final RunTimeService runTimeService;
  private final CourseRunService courseRunService;

  @EventHandler
  public void cancelPlayerRunWhenLeaving(final PlayerQuitEvent event) {
    final var player = event.getPlayer();
    if (courseRunService.hasRunningTime(player)) {
      log.info("{} has their run cancelled.", player.getName());
      courseRunService.cancelRun(player);
    }
  }

  @EventHandler
  public void handleCheckpointHit(final PlayerMoveEvent event) {
    final var player = event.getPlayer();
    if (hasNotMovedFullBlock(event.getFrom(), event.getTo())) {
      return;
    }
    Checkpoint checkpoint = null;
    if (!courseRunService.hasRunningTime(player)) {
      final var optCourse = courseHelper.getIfPlayerIsStandingOnFirstCheckpoint(player);
      if (optCourse.isEmpty()) {
        return;
      }
      checkpoint = courseRunService.startCourseRun(player, optCourse.get()).getCurrentCheckpoint().getCheckpoint();
      runTimeDisplayService.startTrackDisplayForPlayer(player);
    } else {
      checkpoint = courseRunService.getCourseRun(player).getCurrentCheckpoint().getCheckpoint();
    }
    if (checkpoint.isEnabled() && courseHelper.isPlayerInCheckpoint(player, checkpoint)) {
      Bukkit.getPluginManager().callEvent(new PlayerHitCheckpoint(player, courseRunService.getCourseRun(player)));
    }
  }

  @EventHandler
  public void onCheckpointHit(final PlayerHitCheckpoint event) {
    final var player = event.getPlayer();
    final var courseRun = event.getCourseRun();
    log.info("Player [{}] has hit a checkpoint! CheckpointId: [{}]",
        player.getName(), courseRun.getCurrentCheckpoint().getCheckpoint().getCheckpointId());
    final var currentCheckpoint = courseRun.getCurrentCheckpoint();
    final var bestCheckpoint = courseRun.getCurrentBestRunCheckpoint();
    final var checkPointTime = courseRunService.getRunTime(player);
    currentCheckpoint.recordCheckpointTime(checkPointTime);
    handleFirstCheckpoint(player, courseRun, currentCheckpoint, bestCheckpoint);
    if (courseRun.getCurrentCheckpoint().isLastCheckpoint()) {
      handleLastCheckpoint(player, courseRun);
    } else {
      handleNextCheckpoint(player, courseRun);
    }
  }

  private void handleNextCheckpoint(final Player player, final CourseRun courseRun) {
    final var nextCurrentCheckpointNode = courseRunService.getCourseRun(player).getCurrentCheckpoint().getNextCheckpoint();
    final var nextBestCheckpointNode = courseRunService.getCourseRun(player).getCurrentBestRunCheckpoint().getNextCheckpoint();
    courseRun.setCurrentCheckpoint(nextCurrentCheckpointNode);
    courseRun.setCurrentBestRunCheckpoint(nextBestCheckpointNode);
    log.info("Setting new checkpoint for player [{}]. New checkpoint ID: [{}]",
        player.getName(), nextCurrentCheckpointNode.getCheckpoint().getCheckpointId());
  }

  private void handleLastCheckpoint(final Player player, final CourseRun courseRun) {
    final var runTime = courseRunService.stopRunTimer(player);
    runTimeDisplayService.displayTotalTimeToPlayer(player, runTime);
    log.info("Player [{}] has finished a run! Time: [{}]",
        player.getName(), runTime);
    runTimeService.recordRunTime(player, courseRun, runTime);
  }

  private void handleFirstCheckpoint(
      final Player player, final CourseRun courseRun, final CheckpointNode currentCheckpoint,
      final CheckpointNode bestCheckpoint) {
    if (currentCheckpoint.isFirstCheckpoint()) {
      log.info("Player: [{}] started a run at: [{}]", player.getName(), courseRun.getCourse().getCourseName());
    } else {
      notifyPlayerAboutSplitTime(player, courseRun.getCourse(), currentCheckpoint, bestCheckpoint);
    }
  }

  private void notifyPlayerAboutSplitTime(
      final Player player,
      final Course course,
      final CheckpointNode currentRun,
      final CheckpointNode bestRun) {
    final var currentTime = currentRun.getLastCheckpointTime();
    final var bestRunTime = bestRun.getLastCheckpointTime();
    final var currentTimeFormatted = getFormattedTime(currentTime);
    if (Objects.nonNull(bestRunTime) && bestTimeService.hasBestRunSplitTimes(player, course)) {
      final var difference = calculateRunTime(bestRunTime, currentTime);
      final var differenceFormatted =
          getSplitTimePrefix(currentTime, bestRunTime) + difference.format(SHORT_TIME_FORMAT);
      player.sendRawMessage(String.format("%s (%s%s)", currentTimeFormatted, differenceFormatted, ChatColor.WHITE));
    } else {
      player.sendRawMessage(currentTimeFormatted);
    }
  }

  private String getFormattedTime(final LocalTime localTime) {
    return localTime.format(TIME_FORMAT);
  }

  private boolean hasNotMovedFullBlock(final Location from, @Nullable final Location to) {
    return !Objects.nonNull(to)
        || (from.getBlockX() == to.getBlockX()
        && from.getBlockY() == to.getBlockY()
        && from.getBlockZ() == to.getBlockZ());
  }

  private String getSplitTimePrefix(final LocalTime currentTime, final LocalTime bestTime) {
    return currentTime.isBefore(bestTime) ? ChatColor.GREEN + "-" : ChatColor.RED + "+";
  }
}
