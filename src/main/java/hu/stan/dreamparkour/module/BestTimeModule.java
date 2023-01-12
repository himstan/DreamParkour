package hu.stan.dreamparkour.module;

import hu.stan.dreamparkour.service.runtime.RunTimeService;
import hu.stan.dreamplugin.annotation.core.Module;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Module
@AllArgsConstructor
public class BestTimeModule implements Listener {

  private RunTimeService runTimeService;

  @EventHandler
  public void onPlayerJoin(final PlayerJoinEvent event) {
    runTimeService.initBestTimeForPlayer(event.getPlayer());
  }

  @EventHandler
  public void onPlayerQuit(final PlayerQuitEvent event) {
    runTimeService.clearBestTimeCacheForPlayer(event.getPlayer());
  }
}
