package com.sample.dao;

import com.sample.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.SQLException;

@Listeners(TestMethodCapture.class)
@Slf4j
public abstract class AbstractDaoTest {
    private volatile long counter = 0L;
    private DataSource dataSource;

    @AfterMethod
    public void afterTest() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.close();
        dataSource = null;
        nullOutDaos();
        deleteDbFile();
        System.err.println();
    }

    @BeforeMethod
    public void beforeTest() throws Exception {
        counter = TestMethodCapture.getCounter();
        deleteDbFile();
        dataSource = TestUtils.getDataSource(getPrefix() + getDbName());
        TestUtils.databaseFlyway(dataSource).migrate();
        createDaos(dataSource);
    }

    protected abstract void createDaos(DataSource dataSource) throws Exception;

    private void deleteDbFile() {
        final Path path = Paths.get(getPrefix() + getDbName());
        System.out.println(path);
        try {
            if (Files.isDirectory(path)) {
                Files.walkFileTree(path,
                                   new SimpleFileVisitor<Path>() {
                                       @Override
                                       public FileVisitResult postVisitDirectory(
                                               Path dir, IOException exc) throws IOException {
                                           Files.delete(dir);
                                           return FileVisitResult.CONTINUE;
                                       }

                                       @Override
                                       public FileVisitResult visitFile(
                                               Path file, BasicFileAttributes attrs)
                                               throws IOException {
                                           Files.delete(file);
                                           return FileVisitResult.CONTINUE;
                                       }
                                   });
            }
            else {
                Files.delete(path);
            }
        }
        catch (IOException e) {
            log.info("could not delete db file... delete later if possible.", e);
            try {
                path.toFile().deleteOnExit();
            }
            catch (Exception e1) {
                log.info("Delete on exit failed as well.", e);
            }
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public abstract String getDbName();

    private String getPrefix() {
        return "T" + counter;
    }

    protected abstract void nullOutDaos();
}
