package com.sjl.one;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Repository
public class UserDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    SimpleJdbcInsert simpleJdbcInsert;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void insertData() {
        for (int i = 3; i < 5; i++) {
            jdbcTemplate.update(String.format("insert into USER (username,password,nickname,type ) " +
                    "values ('user%d','123456','nickname%d',2);", i, i));
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", "username insert");
        map.put("password", "123456");
        map.put("nickname", "nickname insert");
        map.put("type", 2);
        Number id = simpleJdbcInsert.executeAndReturnKey(map);
        log.info("id=" + id);
        showUsers();
    }

    /**
     * 批处理
     */
    public void batchUsers() {
        jdbcTemplate.batchUpdate("insert into USER (id,username,password,nickname,type) values (?,?,?,?,?)", new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, 10 + i);
                preparedStatement.setString(2, "username" + i);
                preparedStatement.setString(3, "password" + i);
                preparedStatement.setString(4, "nickname" + i);
                preparedStatement.setInt(5, 2);
            }

            @Override
            public int getBatchSize() {
                return 2;
            }
        });
        log.info("jdbcTemplate batchUpdate--------------");
        showUsers();
        List<User> list = IntStream.range(0, 3).mapToObj(i -> User.builder()
                .id(20 + i)
                .username("username" + i)
                .password("password" + i)
                .nickname("nickname" + i)
                .type(2)
                .build()).collect(Collectors.toList());
        namedParameterJdbcTemplate.batchUpdate("insert into USER (id,username,password,nickname,type) values (:id,:username,:password,:nickname,:type)",
                SqlParameterSourceUtils.createBatch(list));
        log.info("namedParameterJdbcTemplate batchUpdate--------------");
        showUsers();
    }

    private void showUsers() {
        List<User> list = jdbcTemplate.query("select * from USER", (resultSet, i) -> User.builder()
                .id(resultSet.getInt("id"))
                .username(resultSet.getString("username"))
                .password(resultSet.getString("password"))
                .nickname(resultSet.getString("nickname"))
                .type(resultSet.getInt("type"))
                .build());
        log.info(list.toString());
    }
}
