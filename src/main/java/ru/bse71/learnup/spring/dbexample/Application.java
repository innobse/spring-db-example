package ru.bse71.learnup.spring.dbexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;
import ru.bse71.learnup.spring.dbexample.config.DbPropertiesConfig;
import ru.bse71.learnup.spring.dbexample.dao.PostDaoJdbc;
import ru.bse71.learnup.spring.dbexample.dao.PostDaoJdbcTemplate;
import ru.bse71.learnup.spring.dbexample.dao.PostDaoNamedJdbcTemplate;
import ru.bse71.learnup.spring.dbexample.dao.interfaces.PostDao;
import ru.bse71.learnup.spring.dbexample.services.DemoService;

/**
 * Created by bse71
 * Date: 21.08.2021
 * Time: 23:46
 */

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        final ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        ctx.getBean(DemoService.class).demo();
    }

    @Bean
    @Profile("jdbc")
    public PostDao postDaoJdbc(DbPropertiesConfig dbConfig) {
        return new PostDaoJdbc(
                dbConfig.getDbUrl(),
                dbConfig.getUsername(),
                dbConfig.getPassword());
    }

    @Bean
    @Profile("jdbcTemplate")
    public PostDao postDaoJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new PostDaoJdbcTemplate(jdbcTemplate);
    }

    @Bean
    @Profile("jdbcTemplateNamed")
    public PostDao postDaoNamedJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new PostDaoNamedJdbcTemplate(namedParameterJdbcTemplate);
    }

    @Bean
    public DemoService demoService(PostDao postDao, TransactionTemplate txTemplate) {
        return new DemoService(postDao, txTemplate);
    }
}
