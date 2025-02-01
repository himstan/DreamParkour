package hu.stan.dreamparkour.configuration;

import hu.stan.dreamweaver.annotation.configuration.Configuration;
import hu.stan.dreamweaver.annotation.configuration.ConfigurationField;

@Configuration(configFileName = "mysql-config")
public class DatabaseConfiguration {

  @ConfigurationField(defaultValue = "false", description = "If true then MYSQL saving will be enabled, otherwise it won't be.")
  public Boolean enabled;

  @ConfigurationField(defaultValue = "<REQUIRED>", description = "The MYSQL server's hostname. Example: 192.168.0.1")
  public String hostname;

  @ConfigurationField(defaultValue = "3306", description = "The MYSQL server's port. Example: 3306")
  public Integer port;

  @ConfigurationField(defaultValue = "<REQUIRED>", description = "The database's name the plugin will use. Example: dream_parkour")
  public String database;

  @ConfigurationField(defaultValue = "<REQUIRED>", description = "The username needed to access the database. Example: root")
  public String username;

  @ConfigurationField(defaultValue = "<REQUIRED>", description = "The password needed to access the database. Example: P4ssword")
  public String password;
}
