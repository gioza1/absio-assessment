package com.sample.service;

import com.sample.dao.AbstractDaoTest;
import com.sample.dao.UserDao;
import com.sample.dao.impl.UserDaoImpl;
import com.sample.domain.AuthenticationCredentials;
import com.sample.domain.User;
import com.sample.exception.UserCredentialsException;
import com.sample.exception.UserNotFoundException;
import com.sample.service.impl.UserServiceImpl;
import com.sample.util.TestUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class UserServiceTest extends AbstractDaoTest {
    private static String DB_NAME = "UserServiceTest.db";
    private static String PASSWORD = "ktAnvxiWdwjG884FbeCkr/rsPoOqkcNaR/kykh7y8whLNiRcKeu47ACNFnen0zMH";
    private static String USERNAME = "administrator";
    private UserDao userDao;
    private UserService userService;

    @Override
    protected void createDaos(DataSource dataSource) throws Exception {
        userDao = new UserDaoImpl(TestUtils.getNamedJdbcOperations(dataSource));
        userService = new UserServiceImpl();
        ((UserServiceImpl)userService).setUserDao(userDao);
    }

    @Override
    public String getDbName() {
        return DB_NAME;
    }

    @Override
    protected void nullOutDaos() {
        userDao = null;
        userService = null;
    }

    @Test
    public void testAuthenticate_Invalid() {
        Assert.assertThrows(UserCredentialsException.class, () -> userService.authenticate(AuthenticationCredentials.builder()
                                                                                                                    .username(USERNAME)
                                                                                                                    .password("not-me")
                                                                                                                    .build()));
    }

    @Test
    public void testAuthenticate_UserNotFound() {
        Assert.assertThrows(UserNotFoundException.class, () -> userService.authenticate(AuthenticationCredentials.builder()
                                                                                                                 .username("not-me")
                                                                                                                 .password(PASSWORD)
                                                                                                                 .build()));
    }

    @Test
    public void testAuthenticate_Valid() {
        userService.authenticate(AuthenticationCredentials.builder()
                                                          .username(USERNAME)
                                                          .password(PASSWORD)
                                                          .build());
    }

    @Test
    void testGetAllAuthenticated() {
        User theUser = User.builder()
                           .id(0)
                           .username(USERNAME)
                           .password(PASSWORD)
                           .build();
        List<User> theUsers = new ArrayList<>();
        theUsers.add(theUser);
        userService.authenticate(AuthenticationCredentials.builder()
                                                          .username(USERNAME)
                                                          .password(PASSWORD)
                                                          .build());
        List<User> users = userService.getUsers();
        Assert.assertEquals(users, theUsers);
    }

    @Test
    void testGetAuthenticatedFindUserName() {
        User theUser = User.builder()
                           .id(0)
                           .username(USERNAME)
                           .password(PASSWORD)
                           .build();
        userService.authenticate(AuthenticationCredentials.builder()
                                                          .username(USERNAME)
                                                          .password(PASSWORD)
                                                          .build());
        User user = userService.getUser(0);
        Assert.assertEquals(user, theUser);
    }

    @Test
    public void testGet_Invalid() {
        AuthenticationCredentials credentials = AuthenticationCredentials.builder()
                                                                         .username(USERNAME)
                                                                         .password(PASSWORD)
                                                                         .build();

        // Current credentials work...                                                                         .build();
        userService.authenticate(credentials);

        // Create new credentials.
        String newPassword = "new password";
        AuthenticationCredentials newCredentials = AuthenticationCredentials.builder()
                                                                            .username(USERNAME)
                                                                            .password(newPassword)
                                                                            .build();

        // New credentials do not work yet.                                                                             .build();
        Assert.assertThrows(UserCredentialsException.class, () -> userService.authenticate(newCredentials));

        // Update the password
        userService.updatePassword(newCredentials.getUsername(), newCredentials.getPassword());

        // Now they do!
        userService.authenticate(newCredentials);
    }

    @Test
    public void testLogout() {
        userService.logout();
    }
}
