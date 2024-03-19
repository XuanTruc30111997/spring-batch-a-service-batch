package org.example.config;

import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.example.properties.DataSourceProperties;
import org.example.properties.LiquibaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class LiquibaseConfiguration {
    @Autowired
    private LiquibaseProperties liquibaseProperties;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean
    public SpringLiquibase liquibase() {
        log.info("Start init liquibase");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dataSourceProperties.getDriverName());
        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setUsername(liquibaseProperties.getUser());
        dataSource.setPassword(liquibaseProperties.getPassword());
        dataSource.setSchema(dataSourceProperties.getSchema());

        SpringLiquibase liquibase = getSpringLiquibase(dataSource);

        log.info("Init liquibase success");
        return liquibase;
    }

    private SpringLiquibase getSpringLiquibase(DriverManagerDataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:/changelog/db.changelog.yaml");
        liquibase.setDataSource(dataSource);
        liquibase.setLiquibaseSchema(liquibaseProperties.getSchema());

        Map<String, String> parameters = new HashMap<>();
        parameters.put("usrName", dataSourceProperties.getUserName());
        parameters.put("userPassword", dataSourceProperties.getPassword());
        parameters.put("schema", dataSourceProperties.getSchema());
        parameters.put("roleName", liquibaseProperties.getRoleName());

        liquibase.setChangeLogParameters(parameters);
        return liquibase;
    }
}
