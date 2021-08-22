package ru.bse71.learnup.spring.dbexample.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by bse71
 * Date: 22.08.2021
 * Time: 0:00
 */

@Data
@Configuration
@EnableConfigurationProperties
public class DbPropertiesConfig {

    @Value("${config.db.url:}")
    private String dbUrl;

    @Value("${config.db.username:}")
    private String username;

    @Value("${config.db.pass:}")
    private String password;
}
