package com.sample.service;

import com.sample.dao.AbstractDaoTest;
import com.sample.dao.AddressDao;
import com.sample.dao.impl.AddressDaoImpl;
import com.sample.domain.Address;
import com.sample.service.impl.AddressServiceImpl;
import com.sample.util.TestUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class AddressServiceTest extends AbstractDaoTest {
    private static String DB_NAME = "AddressServiceTest.db";
    private static Integer USER_ID = 0;
    private static String STREET = "234 Spruce Avenue Belleville";
    private static String STATE = "NJ";
    private static String ZIP = "07109";

    private AddressDao addressDao;
    private AddressService addressService;

    @Override
    protected void createDaos(DataSource dataSource) throws Exception {
        addressDao = new AddressDaoImpl(TestUtils.getNamedJdbcOperations(dataSource));
        addressService = new AddressServiceImpl();
        ((AddressServiceImpl) addressService).setAddressDao(addressDao);
    }

    @Override
    public String getDbName() {
        return DB_NAME;
    }

    @Override
    protected void nullOutDaos() {
        addressDao = null;
        addressService = null;
    }

    @Test
    void testGetAllAddresses() {
        Address address1 = Address.builder()
                .id(0)
                .userId(USER_ID)
                .street(STREET)
                .state(STATE)
                .zip(ZIP)
                .build();
        Address address2 = Address.builder()
                .id(1)
                .userId(USER_ID)
                .street("4031 Goodwin Avenue")
                .state("WA")
                .zip("99205")
                .build();
        List<Address> addresses = new ArrayList<>();
        addresses.add(address1);
        addresses.add(address2);
        Assert.assertEquals(addresses, addressService.getAddresses());
    }

    @Test
    void testGetAddressByUserId() {
        Address address1 = Address.builder()
                .id(0)
                .userId(USER_ID)
                .street(STREET)
                .state(STATE)
                .zip(ZIP)
                .build();
        Address address2 = Address.builder()
                .id(1)
                .userId(USER_ID)
                .street("4031 Goodwin Avenue")
                .state("WA")
                .zip("99205")
                .build();
        List<Address> addresses = new ArrayList<>();
        addresses.add(address1);
        addresses.add(address2);
        Assert.assertEquals(addresses, addressService.getAddressByUserId(USER_ID));
    }

    @Test
    void testGetAddressByUserId_nonExistingUser() {
        Assert.assertTrue(addressService.getAddressByUserId(3).isEmpty());
    }

}
