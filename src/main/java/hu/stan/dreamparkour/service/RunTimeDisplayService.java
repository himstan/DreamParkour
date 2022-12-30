package hu.stan.dreamparkour.service;

import static hu.stan.dreamparkour.common.constant.PluginConstants.TIME_FORMAT;

import hu.stan.dreamparkour.cache.time.StartTimeCache;
import hu.stan.dreamparkour.common.helper.ActionbarHelper;
import hu.stan.dreamparkour.configuration.ParkourConfiguration;
import hu.stan.dreamparkour.util.CourseRunUtils;
import hu.stan.dreamplugin.DreamPlugin;
import hu.stan.dreamplugin.annotation.core.Service;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@Slf4j
@Service
@RequiredArgsConstructor
public class RunTimeDisplayService {

  private final Set<UUID> playersToDisplay = new HashSet<>();
  private final StartTimeCache startTimeCache;
  private final ParkourConfiguration parkourConfiguration;
  private BukkitTask task;

  public void startTrackDisplayForPlayer(final Player player) {
    if (parkourConfiguration.actionbarTimeDisplayEnabled) {
      playersToDisplay.add(player.getUniqueId());
      if (!isTaskRunning()) {
        startDisplayTask();
      }
    }
  }

  public void displayTotalTimeToPlayer(final Player player, final LocalTime totalTime) {
    if (parkourConfiguration.actionbarTimeDisplayEnabled) {
      ActionbarHelper.sendToPlayer(player, totalTime.format(TIME_FORMAT));
    }
  }

  private void displaySplitTimeToPlayer(final UUID playerId) {
    if (startTimeCache.exists(playerId) && isOnline(playerId)) {
      final var player = DreamPlugin.getInstance().getServer().getPlayer(playerId);
      final var currentTime = CourseRunUtils.calculateRunTime(startTimeCache.get(playerId), LocalTime.now());
      ActionbarHelper.sendToPlayer(player, currentTime.format(TIME_FORMAT));
    } else {
      playersToDisplay.remove(playerId);
      stopDisplayTaskIfNeeded();
    }
  }

  private void startDisplayTask() {
    task = Bukkit.getScheduler().runTaskTimerAsynchronously(DreamPlugin.getInstance(),
        () -> playersToDisplay.forEach(this::displaySplitTimeToPlayer),
        0,
        parkourConfiguration.actionBarTickSpeed);
  }

  private void stopDisplayTaskIfNeeded() {
    if (playersToDisplay.isEmpty()) {
      task.cancel();
      task = null;
    }
  }

  private boolean isTaskRunning() {
    return Objects.nonNull(task) && !task.isCancelled();
  }

  private boolean isOnline(final UUID playerId) {
    return Objects.nonNull(DreamPlugin.getInstance().getServer().getPlayer(playerId));
  }
}
