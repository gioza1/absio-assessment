package com.sample.db;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hsqldb.DatabaseURL;

import java.text.MessageFormat;
import java.util.Objects;

@SuppressWarnings("WeakerAccess")
@Builder
@Slf4j
public class DatabaseConnectionStringProvider {

    private final String urlFormatUnEncryptedHsqldb = "{0}{1}{2}{3}{4}/data.db;sql.syntax_pgs=true";

    @Builder.Default
    private String dbFileExtension = ".db";
    private String dbName;
    @SuppressWarnings({"UnusedAssignment", "FieldMayBeFinal"})
    @Builder.Default
    private DbProvider dbProviderType = DbProvider.DB_PROVIDER_HSQLDB;
    @SuppressWarnings({"UnusedAssignment", "FieldMayBeFinal"})
    @Builder.Default
    private DbType dbType = DbType.DB_TYPE_FILE;
    @Builder.Default
    private String path = "./";

    public static String getDbConnectionString(String dbName) {
        Objects.requireNonNull(dbName);
        return DatabaseConnectionStringProvider
                .builder()
                .dbName(dbName)
                .dbProviderType(DbProvider.DB_PROVIDER_HSQLDB)
                .build()
                .getDbConnectionString();
    }

    public String getDbConnectionString() {
        if (dbProviderType == DbProvider.DB_PROVIDER_HSQLDB) {
            return getDbConnectionStringHsqldb();
        }
        else {
            return "";
        }
    }

    public String getDbConnectionStringHsqldb() {
        prerequisites();
        // This represents the URL for an unencrypted DB
        return MessageFormat.format(urlFormatUnEncryptedHsqldb, DatabaseURL.S_URL_PREFIX, dbType.prefix, path, dbName, dbFileExtension);
    }

    private void prerequisites() {
        if (path == null) {
            path = "";
        }
        else if (!path.endsWith("/")) {
            path += "/";
        }
        if (dbName.endsWith(dbFileExtension)) {
            dbName = dbName.replaceAll(dbFileExtension + "$", "");
        }
    }

    public enum DbProvider {
        DB_PROVIDER_HSQLDB
    }

    public enum DbType {
        DB_TYPE_FILE("file:");
        @Getter
        private final String prefix;

        DbType(String prefix) {
            this.prefix = prefix;
        }
    }
}
