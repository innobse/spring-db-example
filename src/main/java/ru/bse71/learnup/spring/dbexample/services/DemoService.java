package ru.bse71.learnup.spring.dbexample.services;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import ru.bse71.learnup.spring.dbexample.dao.interfaces.PostDao;
import ru.bse71.learnup.spring.dbexample.entities.Comment;
import ru.bse71.learnup.spring.dbexample.entities.Post;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    public void demo() {
        Post newPost = new Post(null, "Новый пост", "Lorem ipsum...");
        try {
            final ArrayList<Comment> comments = new ArrayList<>();
            comments.add(
                    new Comment(null, "Comment text", newPost));
            newPost.setComments(comments);

            newPost = postDao.addPost(newPost);

            System.out.println(newPost);

            newPost.setTitle("Новый пост 2");
            final int postId = newPost.getId();
            new Thread(() -> {
                final Post post = new Post(postId, "Новый пост 3", "текст");
                post.setComments(comments);
                postDao.updatePost(post);
                System.out.println("Обновлено из потока");
            }).start();
            postDao.updatePost(newPost);
            System.out.println("Обновлено");
        } finally {
            postDao.deletePostById(newPost.getId());
            System.out.println("Удалено");

            printPosts(postDao.getAllPosts());
        }





    }

    private void printPosts(Collection<Post> posts) {
        for (Post post : posts) {
            System.out.println(post);
        }
    }
}
