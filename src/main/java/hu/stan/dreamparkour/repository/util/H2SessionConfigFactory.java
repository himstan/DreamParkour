package hu.stan.dreamparkour.repository.util;

import hu.stan.dreamplugin.DreamPlugin;
import hu.stan.dreamplugin.exception.DreamPluginException;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class H2SessionConfigFactory implements SessionConfigFactory {

    private final List<Class<?>> entityClasses;
    private File databaseFolder;
    private File databaseFile;

    @Override
    public Configuration getConfiguration() {
        setupDatabaseFile();
        final Configuration config = new Configuration();
        entityClasses.forEach(
                config::addAnnotatedClass
        );
        config.setProperty("hibernate.connection.provider_class", "com.zaxxer.hikari.hibernate.HikariConnectionProvider");
        config.setProperty("hibernate.hikari.minimumIdle", "5");
        config.setProperty("hibernate.hikari.maximumPoolSize", "10");
        config.setProperty("hibernate.hikari.idleTimeout", "30000");
        config.setProperty("hibernate.hikari.dataSourceClassName", "org.h2.jdbcx.JdbcDataSource");
        config.setProperty("hibernate.hikari.dataSource.url", getUrl() + ";AUTO_SERVER=TRUE");
        return config;
    }

    public String getUrl() {
        setupDatabaseFile();
        return String.format("jdbc:h2:~/%s/database/database",
            DreamPlugin.getInstance().getDataFolder().getName());
    }

    private void setupDriver() {
        try {
            Class.forName ("org.h2.Driver");
        } catch (final ClassNotFoundException e) {
            throw new DreamPluginException("Couldn't find H2 JDBC Driver.", e);
        }
    }

    private void setupDatabaseFile() {
        setupDriver();
        createDatabaseFolderIfDoesntExist();
        createDatabaseFileIfDoesntExist();
    }

    private void createDatabaseFolderIfDoesntExist() {
        databaseFolder = new File(DreamPlugin.getInstance().getDataFolder(), "database");
        if (!databaseFolder.exists()) {
            databaseFolder.mkdir();
        }
    }

    private void createDatabaseFileIfDoesntExist() {
        try {
            databaseFile = new File(databaseFolder, "database");
            if (!databaseFile.exists()) {
                databaseFile.createNewFile();
            }
        } catch (final IOException e) {
                throw new DreamPluginException("There was a problem creating the H2 database file", e);
        }
    }
}
