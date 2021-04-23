package com.sample.dao.impl.mapper;

import com.sample.domain.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int i) throws SQLException {
        final User user = User.builder()
                .id(rs.getInt("id"))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .first_name(rs.getString("first_name"))
                .last_name(rs.getString("last_name"))
                .build();
        return user;
    }
}
