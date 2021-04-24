package com.sample.dao.impl.mapper;

import com.sample.domain.Address;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressMapper implements RowMapper<Address> {
    @Override
    public Address mapRow(ResultSet rs, int i) throws SQLException {
        final Address address = Address.builder()
                .id(rs.getInt("id"))
                .userId(rs.getInt("user_id"))
                .street(rs.getString("street"))
                .state(rs.getString("state"))
                .zip(rs.getString("zip"))
                .build();
        return address;
    }
}