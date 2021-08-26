package ru.bse71.learnup.spring.dbexample.services;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import ru.bse71.learnup.spring.dbexample.dao.interfaces.PostDao;
import ru.bse71.learnup.spring.dbexample.entities.Comment;
import ru.bse71.learnup.spring.dbexample.entities.Post;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by bse71
 * Date: 22.08.2021
 * Time: 0:05
 */

public class DemoService implements ApplicationContextAware {

    private ApplicationContext ctx;
    private PostDao postDao;
    private final TransactionTemplate txTemplate;

    public DemoService(PostDao postDao, TransactionTemplate txTemplate) {
        this.postDao = postDao;
        this.txTemplate = txTemplate;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.ctx = ctx;
    }

    public void demo() {

        txTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        txTemplate.executeWithoutResult((status) -> {
            try {
                Post newPost = new Post(null, "Новый пост", "Lorem ipsum...");
                newPost.setComments(
                        Collections.singletonList(
                                new Comment(null, "Comment text", newPost)));

                newPost = postDao.addPost(newPost);

                System.out.println("Добавили пост");

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) { }

                System.out.println("Меняем пост");

                //  Невалидное изменение
                newPost.setTitle("title 2");
                postDao.updatePost(newPost);

                System.out.println("Удаляем пост");
                postDao.deletePostById(newPost.getId());

            } catch (RuntimeException err) {
                status.setRollbackOnly();
                err.printStackTrace();
            }
        });
    }

    private void printPosts(Collection<Post> posts) {
        for (Post post : posts) {
            System.out.println(post);
        }
    }
}
