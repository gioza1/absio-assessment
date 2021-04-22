package com.sample.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static com.sample.db.DatabaseConnectionStringProvider.getDbConnectionString;

@Data
@Component
public class DatabaseConfig {

    private final String url;

    @Inject
    public DatabaseConfig(@Value("${database.name:#{null}}") String dbName) {
        this.url = getDbConnectionString(dbName);
    }
}
