package hu.stan.dreamparkour.repository.util;

import hu.stan.dreamparkour.configuration.DatabaseConfiguration;
import hu.stan.dreamparkour.model.entity.*;
import hu.stan.dreamplugin.core.configuration.registry.ConfigurationRegistrar;
import hu.stan.dreamplugin.exception.DreamPluginException;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.SessionFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
    try (final Connection conn = DriverManager.getConnection(getInstance().getSessionConfigFactory().getUrl() + ";SHUTDOWN=TRUE")) {
    } catch (final SQLException ignored) {
    }
  }

  private void setupLiquibase(final SessionConfigFactory sessionConfigFactory) {
    try {
      final DatabaseConnection connection = new JdbcConnection(DriverManager.getConnection(
          sessionConfigFactory.getUrl()));
      final Database database = DatabaseFactory.getInstance()
          .findCorrectDatabaseImplementation(connection);
      updateLiquibase(database);
    } catch (final Exception e) {
      throw new DreamPluginException("There was an error while running liquibase updates.", e);
    }
  }

  private void updateLiquibase(final Database database) throws Exception {
    try (final Liquibase liquibase = new Liquibase("db/master.xml",
        new ClassLoaderResourceAccessor(), database)) {
      liquibase.update(new Contexts());
    } catch (final ClassCastException ignored) {
      // There is a liquibase bug which we just ignore because it doesn't really affect anything.
    }
  }

  private SessionFactory setupSessionFactory() {
    sessionConfigFactory = getSessionConfigFactory();
    setupLiquibase(sessionConfigFactory);
    return sessionConfigFactory.getConfiguration().buildSessionFactory();
  }

  private SessionConfigFactory getSessionConfigFactory() {
    final List<Class<?>> classes = List.of(DbCourse.class, DbCheckpoint.class, DbLocation.class, DbTotalRunTime.class, DbSplitRunTime.class);
    return configuration.enabled
            ? new MySqlSessionConfigFactory(classes, configuration)
            : new H2SessionConfigFactory(classes);
  }
}
