package ru.bse71.learnup.spring.dbexample.services;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.bse71.learnup.spring.dbexample.dao.PostDaoDataJpa;
import ru.bse71.learnup.spring.dbexample.dao.interfaces.PostDao;
import ru.bse71.learnup.spring.dbexample.entities.Comment;
import ru.bse71.learnup.spring.dbexample.entities.Post;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * Created by bse71
 * Date: 22.08.2021
 * Time: 0:05
 */

public class DemoService implements ApplicationContextAware {

    private ApplicationContext ctx;
    private PostDao postDao;

    public DemoService(PostDao postDao) {
        this.postDao = postDao;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.ctx = ctx;
    }

    @Transactional(
            propagation = REQUIRED,
            isolation = Isolation.DEFAULT,
            rollbackFor = {EOFException.class, FileNotFoundException.class},
            noRollbackFor = IllegalArgumentException.class,
            timeout = 5,
            readOnly = false)
    public void demo() {

        Post newPost = new Post(null, "Новый пост", "Lorem ipsum...");
        List<Comment> comments = new ArrayList<>(2);
        comments.add(new Comment(null, "Comment text", newPost));
        newPost.setComments(comments);

        newPost = postDao.addPost(newPost);

        System.out.println("Добавили пост");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) { }

        System.out.println("Меняем пост");

        //  Невалидное изменение
        newPost.setTitle("title 23");
        postDao.updatePost(newPost);

        System.out.println("Удаляем пост");
        postDao.deletePostById(newPost.getId());
    }

    public void demo2() {
        PostDaoDataJpa repo = (PostDaoDataJpa) postDao;

        System.out.println("Посты с текстом:\n" + repo.getAllPostsWithText());
        System.out.println("\n\nПосты, содержащие \"tintle\":\n" + repo.getAllPostsWithTitleContains("title"));
    }

    private void printPosts(Collection<Post> posts) {
        for (Post post : posts) {
            System.out.println(post);
        }
    }
}
