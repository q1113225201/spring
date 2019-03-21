package com.sjl.one;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

@Slf4j
@Repository
public class UserDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    SimpleJdbcInsert simpleJdbcInsert;

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
        int id = simpleJdbcInsert.execute(map);
        log.info("id=" + id);
        jdbcTemplate.query("select * from USER ", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                return new User.UserBuilder()
                        .id(resultSet.getInt(0) )
                        .build();
            }
        })
                .forEach(row->log.info(row.toString()));
    }
}
