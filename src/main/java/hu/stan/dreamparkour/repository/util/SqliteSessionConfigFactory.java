package hu.stan.dreamparkour.repository.util;

import hu.stan.dreamweaver.DreamWeaver;
import hu.stan.dreamweaver.exception.DreamWeaverException;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class SqliteSessionConfigFactory implements SessionConfigFactory {

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
        config.setProperty("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
        config.setProperty("hibernate.connection.provider_class", "com.zaxxer.hikari.hibernate.HikariConnectionProvider");
        config.setProperty("hibernate.hikari.minimumIdle", "5");
        config.setProperty("hibernate.hikari.maximumPoolSize", "10");
        config.setProperty("hibernate.hikari.idleTimeout", "30000");
        config.setProperty("hibernate.hikari.dataSourceClassName", "org.sqlite.SQLiteDataSource");
        config.setProperty("hibernate.hikari.dataSource.url", getUrlWithCredentials());
        return config;
    }

    public String getUrlWithCredentials() {
        setupDatabaseFile();
        return String.format("jdbc:sqlite:plugins/%s/database/database.db",
            DreamWeaver.getInstance().getDataFolder().getName());
    }

    @Override
    public String getUrl() {
        return getUrlWithCredentials();
    }

    @Override
    public String userName() {
        return "";
    }

    @Override
    public String password() {
        return "";
    }

    @Override
    public String getDatabaseType() {
        return "h2";
    }

    private void setupDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (final ClassNotFoundException e) {
            throw new DreamWeaverException("Couldn't find SQLite JDBC Driver.", e);
        }
    }

    private void setupDatabaseFile() {
        setupDriver();
        createDatabaseFolderIfDoesntExist();
        createDatabaseFileIfDoesntExist();
    }

    private void createDatabaseFolderIfDoesntExist() {
        databaseFolder = new File(DreamWeaver.getInstance().getDataFolder(), "database");
        if (!databaseFolder.exists()) {
            databaseFolder.mkdir();
        }
    }

    private void createDatabaseFileIfDoesntExist() {
        try {
            databaseFile = new File(databaseFolder, "database.db");
            if (!databaseFile.exists()) {
                databaseFile.createNewFile();
            }
        } catch (final IOException e) {
                throw new DreamWeaverException("There was a problem creating the Sqlite database file", e);
        }
    }
}
