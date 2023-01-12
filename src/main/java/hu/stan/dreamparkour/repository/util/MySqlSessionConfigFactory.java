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
        config.setProperty("hibernate.connection.url", getUrl());
        config.setProperty("hibernate.connection.username", databaseConfiguration.username);
        config.setProperty("hibernate.connection.password", databaseConfiguration.password);
        config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        config.setProperty("hibernate.jdbc.batch_size", "10");
        return config;
    }


    public String getUrl() {
        return String.format("jdbc:mysql://%s:%d/%s",
                databaseConfiguration.hostname,
                databaseConfiguration.port,
                databaseConfiguration.database);
    }
}
