package hu.stan.dreamparkour.repository.util;

import org.hibernate.cfg.Configuration;

public interface SessionConfigFactory {

    Configuration getConfiguration();

    String getUrlWithCredentials();

    String getUrl();

    String userName();

    String password();

    String getDatabaseType();
}
