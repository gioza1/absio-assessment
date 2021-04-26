package com.sample.dao.impl;

import com.sample.dao.AddressDao;
import com.sample.dao.impl.mapper.AddressMapper;
import com.sample.dao.impl.mapper.UserMapper;
import com.sample.domain.Address;
import com.sample.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.*;

@Repository
@Slf4j
public class AddressDaoImpl implements AddressDao {

    private final NamedParameterJdbcOperations jdbc;

    @Inject
    public AddressDaoImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc);
    }

    @Override
    public int create(Address address) {
        String sql = "insert into address(user_id, street, state, zip) " +
                "values(:user_id, :street, :state, :zip)";

        final KeyHolder holder = new GeneratedKeyHolder();

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("user_id", address.getUserId())
                .addValue("street", address.getStreet())
                .addValue("state", address.getState())
                .addValue("zip", address.getZip());

        if (jdbc.update(sql, namedParameters, holder) != 1) {
            throw new DatabaseException("There was an error creating an address.");
        }

        int id = holder.getKey().intValue();
        address.setId(id);

        return id;
    }

    @Override
    public void update(Address address) {
        String sql = "update address " +
                "set street = IsNull(:street, street), " +
                "state = IsNull(:state, state), " +
                "zip = IsNull(:zip, zip) " +
                "where id = :id";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", address.getId())
                .addValue("street", address.getStreet())
                .addValue("state", address.getState())
                .addValue("zip", address.getZip());

        if (jdbc.update(sql, namedParameters) != 1) {
            throw new DatabaseException("There was an error updating an address.");
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "delete from address " +
                "where id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
        return jdbc.update(sql, namedParameters) == 1;
    }

    @Override
    public Optional<Address> get(int id) {
        String sql = "select * from address " +
                "where id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
        try {
            return Optional.of(jdbc.queryForObject(sql, namedParameters, new AddressMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Address> getAddressByUserId(int userId, boolean doReturnUserId) {
        // SQL Excludes user_id in response
        String sql = "";
        if (!doReturnUserId) {
            sql = "select id,street,state,zip from address " +
                    "where user_id = :user_id";
        } else {
            sql = "select * from address " +
                    "where user_id = :user_id";
        }
        log.info("getAddressByUserId(): " + userId);
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("user_id", userId);
        try {
            return jdbc.query(sql, namedParameters, new AddressMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Address> getAll() {
        String sql = "select * from address";
        return jdbc.query(sql, new AddressMapper());
    }

    @Override
    public boolean deleteAddresses(int userId, List<Integer> addressIds) {
        String sql = "delete from address where id in (:ids)";
        MapSqlParameterSource  namedParameters = new MapSqlParameterSource().addValue("ids", addressIds);
        return jdbc.update(sql, namedParameters) == 1;
    }
}
