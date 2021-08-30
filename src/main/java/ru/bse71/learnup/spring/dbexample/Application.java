package ru.bse71.learnup.spring.dbexample;

import org.hibernate.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import ru.bse71.learnup.spring.dbexample.config.DbPropertiesConfig;
import ru.bse71.learnup.spring.dbexample.dao.*;
import ru.bse71.learnup.spring.dbexample.dao.interfaces.PostDao;
import ru.bse71.learnup.spring.dbexample.entities.Post;
import ru.bse71.learnup.spring.dbexample.repositories.CommentRepository;
import ru.bse71.learnup.spring.dbexample.repositories.PostRepository;
import ru.bse71.learnup.spring.dbexample.services.DemoService;

import javax.sql.DataSource;

/**
 * Created by bse71
 * Date: 21.08.2021
 * Time: 23:46
 */

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        final ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        final DemoService service = ctx.getBean(DemoService.class);
        final PostDaoDataJpa postDaoDataJpa = ctx.getBean(PostDaoDataJpa.class);

        Post[] posts = new Post[10];
        for (int i = 0; i < posts.length; i++) {
            posts[i] = service.createPost("Новый пост " + i, "text");
        }

        postDaoDataJpa.modifyTexts(
                "text",
                "newText");

        for (Post post : posts) {
            service.deletePost(post);
        }

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
    @Profile("hibernate")
    public PostDao postDaoHibernate(SessionFactory sessionFactory) {
        return new PostDaoHibernate(sessionFactory);
    }

    @Bean
    @Profile("data-jpa")
    public PostDao postDaoDataJpa(PostRepository postRepository, CommentRepository commentRepository) {
        return new PostDaoDataJpa(postRepository, commentRepository);
    }

    @Bean
    public DemoService demoService(PostDao postDao) {
        return new DemoService(postDao);
    }

    @Bean
    @Profile("hibernate")
    public SessionFactory sessionFactory(DataSource dataSource, AsyncTaskExecutor executor) {
        return new LocalSessionFactoryBuilder(dataSource)
                .scanPackages("ru.bse71.learnup.spring.dbexample.entities")
                .buildSessionFactory(executor);
    }
}
