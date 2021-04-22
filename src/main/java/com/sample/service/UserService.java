package com.sample.service;

import com.sample.domain.AuthenticationCredentials;
import com.sample.domain.User;

import java.util.List;

public interface UserService {
    void authenticate(AuthenticationCredentials credentials);

    User getUser(int userId);

    List<User> getUsers();

    void logout();

    User updatePassword(String username, String password);
}
