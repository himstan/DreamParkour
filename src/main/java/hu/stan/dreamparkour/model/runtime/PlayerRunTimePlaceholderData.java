package hu.stan.dreamparkour.model.runtime;

import java.time.LocalTime;

public record PlayerRunTimePlaceholderData(
    String playerName,
    String formattedRunTime,
    LocalTime localTime
) implements Comparable<PlayerRunTimePlaceholderData> {

  @Override
  public int compareTo(final PlayerRunTimePlaceholderData o) {
    return this.localTime.compareTo(o.localTime);
  }
}
