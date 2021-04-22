package com.sample.service.impl;

import com.sample.aspect.annotation.EnsureLogOut;
import com.sample.dao.UserDao;
import com.sample.domain.AuthenticationCredentials;
import com.sample.domain.User;
import com.sample.exception.UserCredentialsException;
import com.sample.exception.UserNotFoundException;
import com.sample.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private UserDao userDao;

    @Override
    @Transactional
    public void authenticate(AuthenticationCredentials credentials) {
        // Make sure the state of the Gateway is NOT secured.
        log.info("Authenticating user " + credentials.getUsername());
        List<User> users = userDao.get();
        log.info("User Count: " + users.size());
        for (User user : users) {
            log.info("user: " + user);
            log.info("compare: " + user.getUsername().equals(credentials.getUsername()));
        }
        User user = get(credentials.getUsername());

        if (!user.getPassword().equals(credentials.getPassword())) {
            throw new UserCredentialsException("Password incorrect!");
        }
    }

    private User get(String username) {
        log.info("Requesting user " + username);
        Optional<User> user = userDao.get(username);

        if (user.isPresent()) {
            return user.get();
        }
        else {
            log.trace("Request user " + username + " not found");
            throw new UserNotFoundException("User " + username + " not found");
        }
    }

    @Override
    public User getUser(int userId) {
        log.info("Requesting user " + userId);
        Optional<User> user = userDao.get(userId);

        if (user.isPresent()) {
            return user.get();
        }
        else {
            log.trace("Request user " + userId + " not found");
            throw new UserNotFoundException("User " + userId + " not found");
        }
    }

    @Override
    public List<User> getUsers() {
        return userDao.get();
    }

    @Override
    @EnsureLogOut
    public void logout() {
        // Add additional logout logic if needed. Session will be removed only after returning from this method.
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User updatePassword(String username, String password) {
        if (!userDao.updatePassword(username, password)) {
            throw new UserNotFoundException("Password could not be updated for " + username + ": not found");
        }
        return get(username);
    }
}
