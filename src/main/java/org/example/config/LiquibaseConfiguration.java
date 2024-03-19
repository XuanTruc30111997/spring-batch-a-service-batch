package org.example.config;

import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.example.properties.DataSourceProperties;
import org.example.properties.LiquibaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class LiquibaseConfiguration {
    @Autowired
    private LiquibaseProperties liquibaseProperties;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Autowired
    private DataSource dataSource;

    @Bean
    public SpringLiquibase liquibase() {
        log.info("Start init liquibase");

        SpringLiquibase liquibase = getSpringLiquibase(dataSource);

        log.info("Init liquibase success");
        return liquibase;
    }

    private SpringLiquibase getSpringLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:/changelog/db.changelog.yaml");
        liquibase.setDataSource(dataSource);
        liquibase.setLiquibaseSchema(liquibaseProperties.getSchema());

        Map<String, String> parameters = new HashMap<>();
        parameters.put("schema", liquibaseProperties.getSchema());

        liquibase.setChangeLogParameters(parameters);
        return liquibase;
    }
}
