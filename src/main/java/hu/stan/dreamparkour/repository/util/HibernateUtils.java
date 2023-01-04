package hu.stan.dreamparkour.repository.util;

import hu.stan.dreamparkour.configuration.DatabaseConfiguration;
import hu.stan.dreamparkour.model.entity.DbCheckpoint;
import hu.stan.dreamparkour.model.entity.DbCourse;
import hu.stan.dreamparkour.model.entity.DbLocation;
import hu.stan.dreamparkour.model.entity.DbSplitRunTime;
import hu.stan.dreamparkour.model.entity.DbTotalRunTime;
import hu.stan.dreamplugin.core.configuration.registry.ConfigurationRegistrar;
import hu.stan.dreamplugin.exception.DreamPluginException;
import java.sql.DriverManager;
import java.util.Objects;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtils {

  private final DatabaseConfiguration configuration;
  private final SessionFactory sessionFactory;
  private static HibernateUtils instance;

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

  private void setupLiquibase() {
    try {
      final DatabaseConnection connection = new JdbcConnection(DriverManager.getConnection(
          getDatabaseUrl(),
          configuration.username,
          configuration.password));
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

  private String getDatabaseUrl() {
    return String.format("jdbc:mysql://%s:%d/%s",
        configuration.hostname,
        configuration.port,
        configuration.database);
  }

  private SessionFactory setupSessionFactory() {
    setupLiquibase();
    final Configuration config = new Configuration();
    config.addAnnotatedClass(DbCourse.class);
    config.addAnnotatedClass(DbCheckpoint.class);
    config.addAnnotatedClass(DbLocation.class);
    config.addAnnotatedClass(DbTotalRunTime.class);
    config.addAnnotatedClass(DbSplitRunTime.class);
    config.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
    config.setProperty("hibernate.connection.url", getDatabaseUrl());
    config.setProperty("hibernate.connection.username", configuration.username);
    config.setProperty("hibernate.connection.password", configuration.password);
    config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
    config.setProperty("hibernate.jdbc.batch_size", "10");
    return config.buildSessionFactory();
  }
}
