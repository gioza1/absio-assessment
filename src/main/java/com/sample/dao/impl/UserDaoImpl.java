package com.sample.dao.impl;

import com.sample.dao.UserDao;
import com.sample.dao.impl.mapper.UserMapper;
import com.sample.domain.User;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Slf4j
public class UserDaoImpl implements UserDao {
    private final NamedParameterJdbcOperations jdbc;

    @Inject
    public UserDaoImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc);
    }

    @Override
    public int create(User user) {
        String sql = "insert into user(username, password) " +
                     "values(:username, :password)";

        final KeyHolder holder = new GeneratedKeyHolder();

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("username", user.getUsername())
                .addValue("password", user.getPassword());

        if (jdbc.update(sql, namedParameters, holder) != 1) {
            throw new DatabaseException("There was an error creating a user.");
        }

        int id = holder.getKey().intValue();
        user.setId(id);

        return id;
    }

    @Override
    public boolean delete(int id) {
        String sql = "delete from user " +
                     "where id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
        return jdbc.update(sql, namedParameters) == 1;
    }

    @Override
    public Optional<User> get(String username) {
        String sql = "select * from user " +
                     "where username = :username";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("username", username);
        try {
            return Optional.of(jdbc.queryForObject(sql, namedParameters, new UserMapper()));
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> get(int id) {
        String sql = "select * from user " +
                     "where id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
        try {
            return Optional.of(jdbc.queryForObject(sql, namedParameters, new UserMapper()));
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> get() {
        String sql = "select * from user";
        return jdbc.query(sql, new UserMapper());
    }

    @Override
    public boolean updatePassword(String username, String password) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("username", username)
                .addValue("password", password);
        String sql = "update user " +
                     "set password = :password " +
                     "where username = :username";
        return jdbc.update(sql, namedParameters) == 1;
    }
}
