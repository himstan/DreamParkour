package hu.stan.dreamparkour.common.constant;

import java.time.format.DateTimeFormatter;

public final class PluginConstants {

  private PluginConstants() {}

  public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
  public static final DateTimeFormatter SHORT_TIME_FORMAT = DateTimeFormatter.ofPattern("mm:ss.SSS");
}
