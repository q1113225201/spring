package com.sjl.tx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

@EnableTransactionManagement(mode = AdviceMode.PROXY)
@Slf4j
@SpringBootApplication
public class TxApplication implements CommandLineRunner {

    @Autowired
    TransactionTemplate transactionTemplate;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(TxApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        executeTx();
        executeProxyTx();
    }

    private void executeProxyTx() {
        userService.insert();
        log.info("insert list{}",getUserList());

        try {
            userService.insertThenRollback();
        } catch (RollbackException e) {
            log.info("insertThenRollback list{}",getUserList());
        }
        try {
            userService.invokeInsertThenRollback();
        } catch (RollbackException e) {
            log.info("invokeInsertThenRollback list{}",getUserList());
        }
    }

    private void executeTx() {
        log.info("before count={}", getUserCount());
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                jdbcTemplate.execute("insert into user (id,name) values (1,'123')");
                log.info("transaction count={}", getUserCount());
                //回滚
                transactionStatus.setRollbackOnly();
            }
        });
        log.info("after count={}", getUserCount());
    }

    private long getUserCount() {
        return (long) jdbcTemplate.queryForList("select count(*) as cnt from user").get(0).get("cnt");
    }
    private List<Map<String,Object>> getUserList() {
        return jdbcTemplate.queryForList("select * from user");
    }
}
