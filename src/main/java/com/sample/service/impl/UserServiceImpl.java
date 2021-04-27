package com.sample.service.impl;

import com.sample.aspect.annotation.EnsureLogOut;
import com.sample.dao.AddressDao;
import com.sample.dao.UserDao;
import com.sample.domain.Address;
import com.sample.domain.AuthenticationCredentials;
import com.sample.domain.User;
import com.sample.exception.AddressNotFoundException;
import com.sample.exception.UserCredentialsException;
import com.sample.exception.UserDuplicateException;
import com.sample.exception.UserNotFoundException;
import com.sample.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    private AddressDao addressDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setAddressDao(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

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
        } else {
            log.trace("Request user " + username + " not found");
            throw new UserNotFoundException("User " + username + " not found");
        }
    }

    @Override
    public User getUser(int userId) {
        log.info("Requesting user " + userId);
        Optional<User> user = userDao.get(userId);
        if (user.isPresent()) {
            User validUser = user.get();
            validUser.setAddresses(addressDao.getAddressByUserId(validUser.getId(), false));
            return validUser;
        } else {
            log.trace("Request user " + userId + " not found");
            throw new UserNotFoundException("User " + userId + " not found");
        }
    }


    @Override
    public List<User> getAllUsers() {
        // NOTE: userDao.get() can be simplified with an SQL query.
        // but MIC decided to use DAO implementation for coherency
        // In case performance is an issue, SQL query would probably be best
        List<User> userList = userDao.get();
        for (User user : userList) {
            List<Address> test = addressDao.getAddressByUserId(user.getId(), false);
            user.setAddresses(test);
        }
        return userList;
    }

    @Override
    @EnsureLogOut
    public void logout() {
        // Add additional logout logic if needed. Session will be removed only after returning from this method.
    }


    @Override
    public User updatePassword(String username, String password) {
        if (!userDao.updatePassword(username, password)) {
            throw new UserNotFoundException("Password could not be updated for " + username + ": not found");
        }
        return get(username);
    }

    @Override
    public int createUser(User createUser) {
        String username = createUser.getUsername();
        log.info(MessageFormat.format("Creating user: {0} ", username));
        Optional<User> user = userDao.get(username);
        int generatedId = -1;
        if (!user.isPresent()) {
            User mappedUser = new ModelMapper().map(createUser, User.class);
            generatedId = userDao.create(mappedUser);
            for (Address address : mappedUser.getAddresses()) {
                address.setUserId(generatedId);
                addressDao.create(address);
            }
        } else {
            log.trace("Creation of new user " + username + " failed (duplicate)");
            throw new UserDuplicateException("User " + username + " already exists");
        }
        return generatedId;
    }

    @Override
    @Transactional
    public void updateUser(User updateUser) {
        String username = updateUser.getUsername();
        log.info(MessageFormat.format("Updating user: {0} ", username));
        Optional<User> user = userDao.get(username);
        if (user.isPresent()) {
            User validUser = user.get();
            validUser.setAddresses(updateUser.getAddresses());
            deleteUserAddresses(validUser); // Delete addresses first
            for (Address address : validUser.getAddresses()) {
                Integer addressId = address.getId();
                if (addressId == null) {
                    address.setUserId(validUser.getId());
                    addressDao.create(address);
                } else {
                    Optional<Address> check = addressDao.get(addressId);
                    if (check.isPresent() && check.get().getUserId() == validUser.getId()) {
                        addressDao.update(address);
                    } else {
                        throw new AddressNotFoundException("Address details could not be updated for " + address.getId() + ": not found");
                    }
                }
            }
            userDao.update(new ModelMapper().map(updateUser, User.class));
        } else {
            throw new UserNotFoundException("User details could not be updated for " + username + ": not found");
        }
    }

    /**
     * Helper method to remove 'unregistered' addresses
     **/
    private void deleteUserAddresses(User user) {
        List<Integer> fromRequestIds = user.getAddresses().stream().map(Address::getId).collect(Collectors.toList());
        List<Integer> fromDbIds = addressDao.getAddressByUserId(user.getId(), false).stream().map(Address::getId).collect(Collectors.toList());
        fromDbIds.removeAll(fromRequestIds);
        if (!fromDbIds.isEmpty()) {
            log.info("Deleting the following addresses: " + fromDbIds);
            addressDao.deleteAddresses(user.getId(), fromDbIds);
        }
    }
}
