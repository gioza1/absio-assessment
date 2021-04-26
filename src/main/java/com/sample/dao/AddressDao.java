package com.sample.dao;

import com.sample.domain.Address;

import java.util.List;
import java.util.Optional;

public interface AddressDao {
    int create(Address address);

    void update(Address address);

    boolean delete(int id);

    Optional<Address> get(int id);

    List<Address> getAddressByUserId(int id, boolean doReturnUserId);

    List<Address> getAll();

    boolean deleteAddresses(int userId, List<Integer> addressIds);
}
