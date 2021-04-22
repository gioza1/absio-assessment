package com.sample.dao;

import com.sample.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    int create(User user);

    boolean delete(int id);

    Optional<User> get(String username);

    Optional<User> get(int id);

    List<User> get();

    boolean updatePassword(String username, String password);
}
