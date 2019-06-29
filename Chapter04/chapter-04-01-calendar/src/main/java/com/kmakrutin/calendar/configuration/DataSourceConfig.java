package com.kmakrutin.calendar.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setName("datasource")
                .setType(EmbeddedDatabaseType.H2)
                .addScript("/database/h2/calendar-schema.sql")
                .addScript("/database/h2/calendar-data.sql")
                .addScript("/database/h2/security-schema.sql")
                .addScript("/database/h2/security-users.sql")
                .addScript("/database/h2/security-user-authorities.sql")
                .addScript("/database/h2/security-groups-schema.sql")
                .addScript("/database/h2/security-groups-mappings.sql")
                .build();
    }
}
