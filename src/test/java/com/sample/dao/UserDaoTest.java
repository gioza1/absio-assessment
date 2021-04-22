package com.sample.dao;

import com.sample.dao.impl.UserDaoImpl;
import com.sample.domain.User;
import com.sample.util.TestUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.util.Optional;

public class UserDaoTest extends AbstractDaoTest {
    private static String DB_NAME = "UserDaoTest.db";
    private UserDao dao;
    private NamedParameterJdbcOperations namedJdbcOperations;

    @Override
    protected void createDaos(DataSource dataSource) throws Exception {
        namedJdbcOperations = TestUtils.getNamedJdbcOperations(dataSource);
        dao = new UserDaoImpl(namedJdbcOperations);
    }

    @Override
    public String getDbName() {
        return DB_NAME;
    }

    @Override
    protected void nullOutDaos() {
        namedJdbcOperations = null;
        dao = null;
    }

    @Test
    void testCreateGetDelete() {
        User user1 = TestUtils.createUser();
        dao.create(user1);
        Optional<User> optional = dao.get(user1.getUsername());
        Assert.assertTrue(optional.isPresent());
        User userDb = optional.get();
        Assert.assertEquals(user1, userDb);
        Assert.assertTrue(dao.delete(userDb.getId()));
        optional = dao.get(user1.getUsername());
        Assert.assertFalse(optional.isPresent());
    }

    @Test
    void testCreateGetId() {
        User user1 = TestUtils.createUser();
        dao.create(user1);
        Optional<User> optional = dao.get(user1.getId());
        Assert.assertTrue(optional.isPresent());
        Assert.assertEquals(user1, optional.get());
    }

    @Test
    void testCreateGetUsername() {
        User user1 = TestUtils.createUser();
        dao.create(user1);
        Optional<User> optional = dao.get(user1.getUsername());
        Assert.assertTrue(optional.isPresent());
        Assert.assertEquals(user1, optional.get());
    }

    @Test
    void testGetIdNone() {
        Optional<User> value = dao.get(1);
        Assert.assertFalse(value.isPresent());
    }

    @Test
    void testGetIdOne() {
        Optional<User> value = dao.get(0);
        Assert.assertTrue(value.isPresent());
    }

    @Test
    void testGetUsernameAdministrator() {

        Optional<User> value = dao.get("administrator");
        Assert.assertTrue(value.isPresent());
    }

    @Test
    void testGetUsernameNone() {
        Optional<User> value = dao.get("test@email.com");
        Assert.assertFalse(value.isPresent());
    }

    @Test
    void testUpdatePassword() {
        Optional<User> value = dao.get("administrator");
        Assert.assertTrue(value.isPresent());
        User user = value.get();
        String newPassword = "this is the new password";
        Assert.assertNotEquals(user.getPassword(), newPassword);
        dao.updatePassword(user.getUsername(), newPassword);
        user.setPassword(newPassword);
        final User userFromDb = dao.get("administrator").get();
        Assert.assertEquals(user, userFromDb);
    }
}
