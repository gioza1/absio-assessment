package com.sample.configuration.beans;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.AnnotatedTypeMetadata;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

@Configuration
@Conditional(FlywayMigrationBeans.FlywayMigrationAware.class)
@Slf4j
public class FlywayMigrationBeans {
    private final static String FLYWAY_MIGRATION_ENABLED_PROPERTY_KEY = "flyway.migration.enable";

    public static Properties getFlywayProperties(String flyWayConfigPath) {
        Properties properties = new Properties();

        try (InputStream flywayConfig = new ClassPathResource(flyWayConfigPath).getInputStream()) {
            try {
                properties.load(flywayConfig);
                log.info("Successfully loaded FlyWay config on classpath '" + flyWayConfigPath + "'");
                return properties;

            }
            catch (IOException e) {
                log.error("Unable to load FlyWay config found on classpath '" + flyWayConfigPath + "'");
                return properties;
            }
        }
        catch (IOException e) {
            log.error("No FlyWay config found on classpath '" + flyWayConfigPath + "'");
            return properties;
        }
    }

    @Bean(initMethod = "migrate")
    public Flyway databaseFlyway(DataSource dataSource, @Value("${flyway.conf.path:#{null}}") String flyWayConfigPath) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.configure(getFlywayProperties(Objects.requireNonNull(flyWayConfigPath)));

        return flyway;
    }

    static class FlywayMigrationAware implements Condition {
        private boolean isMigrationEnabled(Environment environment) {
            return environment.getProperty(FLYWAY_MIGRATION_ENABLED_PROPERTY_KEY, Boolean.class, false);
        }

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return isMigrationEnabled(context.getEnvironment());
        }
    }
}
