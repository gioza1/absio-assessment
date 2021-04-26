package com.sample.dao.impl.mapper;

import com.sample.domain.Address;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressMapper implements RowMapper<Address> {
    @Override
    public Address mapRow(ResultSet rs, int i) throws SQLException {
        Address.AddressBuilder address = Address.builder();
        if (columnExists(rs, "user_id")) {
            address.userId(rs.getInt("user_id"));
        }
        return address
                .id(rs.getInt("id"))
                .state(rs.getString("state"))
                .street(rs.getString("street"))
                .zip(rs.getString("zip")).build();
    }

    //TODO: Can be moved to a general helper class
    private boolean columnExists(ResultSet rs, String column) {
        try {
            rs.findColumn(column);
            return true;
        } catch (SQLException sqlEx) {
            // do nothing
        }
        return false;
    }
}