package hu.stan.dreamparkour.repository.util;

import hu.stan.dreamparkour.configuration.DatabaseConfiguration;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.Configuration;

import java.util.List;

@RequiredArgsConstructor
public class MySqlSessionConfigFactory implements SessionConfigFactory {

    private final List<Class<?>> entityClasses;
    private final DatabaseConfiguration databaseConfiguration;

    @Override
    public Configuration getConfiguration() {
        final Configuration config = new Configuration();
        entityClasses.forEach(
                config::addAnnotatedClass
        );
        config.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        config.setProperty("hibernate.connection.url", getUrlWithCredentials());
        config.setProperty("hibernate.connection.username", databaseConfiguration.username);
        config.setProperty("hibernate.connection.password", databaseConfiguration.password);
        config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        config.setProperty("hibernate.jdbc.batch_size", "10");
        return config;
    }


    public String getUrlWithCredentials() {
        return String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s",
            databaseConfiguration.hostname,
            databaseConfiguration.port,
            databaseConfiguration.database,
            databaseConfiguration.username,
            databaseConfiguration.password);
    }

    @Override
    public String getUrl() {
        return String.format("jdbc:mysql://%s:%d/%s",
            databaseConfiguration.hostname,
            databaseConfiguration.port,
            databaseConfiguration.database);
    }

    @Override
    public String userName() {
        return databaseConfiguration.username;
    }

    @Override
    public String password() {
        return databaseConfiguration.password;
    }

    @Override
    public String getDatabaseType() {
        return "mysql";
    }
}
