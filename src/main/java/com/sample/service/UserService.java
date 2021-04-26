package com.sample.service;

import com.sample.domain.AuthenticationCredentials;
import com.sample.domain.User;
import com.sample.dto.user.ChangePasswordDto;
import com.sample.dto.user.CreateUserDto;
import com.sample.dto.user.UpdateUserDto;

import java.util.List;

public interface UserService {
    void authenticate(AuthenticationCredentials credentials);

    User getUser(int userId);

    List<User> getAllUsers();

    void logout();

    User updatePassword(String username, String password);

    int createUser(CreateUserDto CreateUserDto);

    void updateUser(UpdateUserDto updateUserDto);
}
