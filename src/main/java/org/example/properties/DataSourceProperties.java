package org.example.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DataSourceProperties {
    @Autowired
    private Environment env;

    public String getUrl() {
        return getProperty("spring.datasource.url");
    }

    public String getUserName() {
        return getProperty("spring.datasource.username");
    }

    public String getPassword() {
        return getProperty("spring.datasource.password");
    }

    public String getDriverName() {
        return getProperty("spring.datasource.driverClassName");
    }

    public String getSchema() {
        return getProperty("spring.datasource.schema");
    }

    private String getProperty(String property) {
        return env.getProperty(property);
    }
}
