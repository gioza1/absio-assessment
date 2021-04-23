package com.sample.configuration.beans;

import com.sample.configuration.DatabaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.hsqldb.jdbc.JDBCDataSourceFactory;
import org.hsqldb.persist.HsqlProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DatabaseBeans {
    private static final String databaseName = "database";
    private static final String loginTimeoutName = "loginTimeout";
    private static final String passwordName = "password";
    // HSQLDB properties
    private static final String urlName = "url";
    private static final String userName = "user";
    private static final String userNameName = "username";

    @Bean
    @Primary
    public DataSource dataSource(DatabaseConfig databaseConfig) throws Exception {
        HsqlProperties hsqlProperties = new HsqlProperties();
        hsqlProperties.setProperty(urlName, databaseConfig.getUrl());
        hsqlProperties.setProperty(userName, "SA");
        hsqlProperties.setProperty(passwordName, "");
        return JDBCDataSourceFactory.createDataSource(hsqlProperties.getProperties());
    }

    @Bean
    public JdbcOperations jdbcOperations(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcOperations namedJdbcOperations(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
