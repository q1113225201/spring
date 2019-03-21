package com.sjl.one;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
@Slf4j
public class OneDatasourceApplication implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserDao userDao;

    public static void main(String[] args) {
        SpringApplication.run(OneDatasourceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        showConnect();
        showData();
//        userDao.insertData();
        userDao.batchUsers();
    }

    @Bean
    @Autowired
    public SimpleJdbcInsert simpleJdbcInsert(JdbcTemplate jdbcTemplate) {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("user").usingGeneratedKeyColumns("id");
    }

    @Bean
    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    private void showData() {
        jdbcTemplate.queryForList("select * from user")
                .forEach(row -> log.info(row.toString()));
    }

    private void showConnect() throws SQLException {
        log.info(dataSource.toString());
        Connection connection = dataSource.getConnection();
        log.info(connection.toString());
        connection.close();
    }
}
