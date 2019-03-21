package com.sjl.multi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class
})
@Slf4j
public class MultiDatasourceApplication{

    public static void main(String[] args) {
        SpringApplication.run(MultiDatasourceApplication.class, args);
    }

    @Bean
    @ConfigurationProperties("one.datasource")
    public DataSourceProperties oneDataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean
    public DataSource oneDataSource(){
        DataSourceProperties dataSourceProperties = oneDataSourceProperties();
        log.info("oneDataSource:"+dataSourceProperties.getUrl());
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    @Resource
    public PlatformTransactionManager oneTxManager(DataSource oneDataSource){
        return new DataSourceTransactionManager(oneDataSource);
    }

    @Bean
    @ConfigurationProperties("other.datasource")
    public DataSourceProperties otherDataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean
    public DataSource otherDataSource(){
        DataSourceProperties dataSourceProperties = otherDataSourceProperties();
        log.info("otherDataSource:"+dataSourceProperties.getUrl());
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    @Resource
    public PlatformTransactionManager otherTxManager(DataSource otherDataSource){
        return new DataSourceTransactionManager(otherDataSource);
    }


}
