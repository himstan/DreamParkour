package hu.stan.dreamparkour.repository.util;

import org.hibernate.cfg.Configuration;

public interface SessionConfigFactory {

    Configuration getConfiguration();

    String getUrl();
}
