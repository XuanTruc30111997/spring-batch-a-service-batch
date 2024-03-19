package org.example.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class LiquibaseProperties {
    @Autowired
    private Environment env;

    public String getSchema() {
        return getProperty("spring.liquibase.schema");
    }

    public String getUser() {
        return getProperty("spring.liquibase.user");
    }

    public String getPassword() {
        return getProperty("spring.liquibase.password");
    }

    public String getRoleName() {
        return getProperty("spring.liquibase.roleName");
    }

    private String getProperty(String property) {
        return env.getProperty(property);
    }
}
