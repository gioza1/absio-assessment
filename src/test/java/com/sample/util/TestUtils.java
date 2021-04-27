package com.sample.util;

import com.sample.configuration.beans.FlywayMigrationBeans;
import com.sample.db.DatabaseConnectionStringProvider;
import com.sample.domain.Address;
import com.sample.domain.User;
import com.sample.dto.common.validator.mapper.ConstraintViolationExceptionMapper;
import com.sample.dto.common.validator.mapper.JerseyViolationExceptionMapper;
import com.sample.env.HttpServletRequestFactory;
import com.sample.exception.mapper.DatabaseExceptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.hsqldb.jdbc.JDBCDataSourceFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.core.Application;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

@Slf4j
public class TestUtils {

    public static Application buildApplication(Object resource) {
        return new ResourceConfig().register(resource)
                .register(new AbstractBinder() {
                    @Override
                    public void configure() {
                        bindFactory(HttpServletRequestFactory.class)
                                .to(HttpServletRequest.class);
                    }
                })
                .register(new ConstraintViolationExceptionMapper())
                .register(new JerseyViolationExceptionMapper())
                .register(new DatabaseExceptionMapper())
                .packages("com.*");
    }

    public static User createUser() {
        return User.builder()
                .id(0)
                .password(UUID.randomUUID().toString())
                .username(UUID.randomUUID().toString())
                .first_name("John")
                .last_name("Doe")
                .build();
    }

    public static Address createAddress() {
        return Address.builder()
                .id(0)
                .userId(0)
                .street("234 Spruce Avenue Belleville")
                .state("NJ")
                .zip("07109")
                .build();
    }

    public static Flyway databaseFlyway(DataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.configure(FlywayMigrationBeans.getFlywayProperties("flyway/flyway.conf"));

        return flyway;
    }

    public static DataSource getDataSource(String dbName) {
        String urlName = "url";
        String userName = "user";
        String passwordName = "password";
        String url = DatabaseConnectionStringProvider
                .builder()
                .dbProviderType(DatabaseConnectionStringProvider.DbProvider.DB_PROVIDER_HSQLDB)
                .dbName(dbName)
                .build()
                .getDbConnectionString();
        Properties properties = new Properties();
        properties.put(userName, "SA");
        properties.put(passwordName, "");
        properties.put(urlName, url);
        try {
            return JDBCDataSourceFactory.createDataSource(properties);
        } catch (Exception exception) {
            throw new RuntimeException("DB source failed", exception);
        }
    }

    public static JdbcTemplate getJdbcOperations(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    public static NamedParameterJdbcOperations getNamedJdbcOperations(DataSource dataSource) throws SQLException {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
