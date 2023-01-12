package hu.stan.dreamparkour.service.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import hu.stan.dreamparkour.cache.scoreboard.ScoreboardCache;
import hu.stan.dreamplugin.annotation.core.Service;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Service
@RequiredArgsConstructor
public class ScoreboardService {

  final ScoreboardCache scoreboardCache;

  public FastBoard addScoreboard(final Player player) {
    final var scoreboard = new FastBoard(player);
    scoreboardCache.add(player.getUniqueId(), scoreboard);
    return scoreboard;
  }

  public void removeScoreboard(final Player player) {
    scoreboardCache.remove(player.getUniqueId());
  }

  public FastBoard getScoreboard(final Player player) {
    return scoreboardCache.get(player.getUniqueId());
  }
}
