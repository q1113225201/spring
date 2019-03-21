package com.sjl.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void insert() {
        jdbcTemplate.execute("insert into user (id,name) values (1,'1')");
    }

    @Transactional(rollbackFor = RollbackException.class)
    @Override
    public void insertThenRollback() throws RollbackException {
        //事务回滚
        jdbcTemplate.execute("insert into user (id,name) values (2,'2')");
        throw new RollbackException();
    }

    @Override
    public void invokeInsertThenRollback() throws RollbackException {
        //事务不会回滚，当前方法没有事务注解
        insertThenRollback();
    }
}
