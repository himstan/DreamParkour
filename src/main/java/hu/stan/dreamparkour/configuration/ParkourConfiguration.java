package hu.stan.dreamparkour.configuration;

import hu.stan.dreamplugin.annotation.configuration.Configuration;
import hu.stan.dreamplugin.annotation.configuration.ConfigurationField;

@Configuration(configFileName = "parkour-config")
public class ParkourConfiguration {

  @ConfigurationField(defaultValue = "true", description = "Controls if the current time should be displayed on the actionbar.")
  public Boolean actionbarTimeDisplayEnabled;

  @ConfigurationField(defaultValue = "1", description = "Controls how often the actionbar should be updated.")
  public Integer actionBarTickSpeed;

  @ConfigurationField(defaultValue = "10", description = "How many top runs should be cached for every course.")
  public Integer topRunsPerCourseInCache;
}
