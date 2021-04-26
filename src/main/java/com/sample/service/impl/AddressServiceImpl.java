package com.sample.service.impl;

import com.sample.dao.AddressDao;
import com.sample.domain.Address;
import com.sample.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AddressServiceImpl implements AddressService {
    private AddressDao addressDao;

    @Autowired
    public void setAddressDao(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @Override
    public List<Address> getAddressByUserId(int userId) {
        log.info("Retrieving address(es) of user: " + userId);
        List<Address> address = addressDao.getAddressByUserId(userId, true);
        return address;
    }

    @Override
    public List<Address> getAddresses() {
        return addressDao.getAll();
    }

}
