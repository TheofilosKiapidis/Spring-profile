package com.example.Profile.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
public class DatabaseConfig {
    @Bean
    public Connection template(DataSource ds){
        return DataSourceUtils.getConnection(ds);
    }
}
