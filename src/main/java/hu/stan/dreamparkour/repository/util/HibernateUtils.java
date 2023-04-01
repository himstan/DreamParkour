package hu.stan.dreamparkour.repository.util;

import hu.stan.dreamparkour.configuration.DatabaseConfiguration;
import hu.stan.dreamparkour.model.entity.*;
import hu.stan.dreamplugin.DreamPlugin;
import hu.stan.dreamplugin.core.configuration.registry.ConfigurationRegistrar;
import hu.stan.dreamplugin.exception.DreamPluginException;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Objects;

public final class HibernateUtils {

  private final DatabaseConfiguration configuration;
  private final SessionFactory sessionFactory;
  private static HibernateUtils instance;

  private SessionConfigFactory sessionConfigFactory;

  private HibernateUtils() {
    this.configuration = ConfigurationRegistrar.getConfiguration(DatabaseConfiguration.class);
    this.sessionFactory = setupSessionFactory();
  }

  public SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  public static HibernateUtils getInstance() {
    if (Objects.isNull(instance)) {
      instance = new HibernateUtils();
    }
    return instance;
  }

  public static void shutdown() {
    final var sessionFactory = getInstance().getSessionFactory();
    sessionFactory.getCache().evictAllRegions();
    sessionFactory.close();
  }

  private void setupFlyway(final SessionConfigFactory sessionConfigFactory) {
    try {
      final var fluentConfiguration = new FluentConfiguration(DreamPlugin.class.getClassLoader())
          .baselineOnMigrate(true)
          .dataSource(sessionConfigFactory.getUrl(), sessionConfigFactory.userName(), sessionConfigFactory.password());
      final var flyway = new Flyway(fluentConfiguration);
      flyway.migrate();
    } catch (final Exception e) {
      throw new DreamPluginException("There was an error while running flyway updates.", e);
    }
  }

  private SessionFactory setupSessionFactory() {
    sessionConfigFactory = getSessionConfigFactory();
    setupFlyway(sessionConfigFactory);
    return sessionConfigFactory.getConfiguration().buildSessionFactory();
  }

  private SessionConfigFactory getSessionConfigFactory() {
    final List<Class<?>> classes = List.of(DbCourse.class, DbCheckpoint.class, DbLocation.class, DbTotalRunTime.class, DbSplitRunTime.class);
    return configuration.enabled
            ? new MySqlSessionConfigFactory(classes, configuration)
            : new SqliteSessionConfigFactory(classes);
  }
}
