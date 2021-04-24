package com.sample.service;

import com.sample.domain.Address;
import com.sample.domain.AuthenticationCredentials;
import com.sample.domain.User;
import com.sample.dto.user.ChangePasswordDto;
import com.sample.dto.user.CreateUserDto;
import com.sample.dto.user.UpdateUserDto;

import java.util.List;

public interface AddressService {
    List<Address> getAddressByUserId(int addressId);

    List<Address> getAddresses();
}
