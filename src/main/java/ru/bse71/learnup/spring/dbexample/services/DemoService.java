package ru.bse71.learnup.spring.dbexample.services;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import ru.bse71.learnup.spring.dbexample.dao.interfaces.PostDao;
import ru.bse71.learnup.spring.dbexample.entities.Comment;
import ru.bse71.learnup.spring.dbexample.entities.Post;

import java.util.*;

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

    public Post createPost() {

        Post post = new Post(null, "Новый пост", "Lorem ipsum...");
        final ArrayList<Comment> comments = new ArrayList<>();
        comments.add(
                new Comment(null, "Comment text", post));
        post.setComments(comments);

        final Post newPost = postDao.addPost(post);
        return newPost;
    }

    public void deletePost(Post post) {
        postDao.deletePostById(post.getId());
        System.out.println("Удалено");

        printPosts(postDao.getAllPosts());
    }

    public void updatePost(Post newPost) {
        newPost.setTitle("Новый пост 2");
        postDao.updatePost(newPost);
        System.out.println("Обновлено");
    }

    private void printPosts(Collection<Post> posts) {
        for (Post post : posts) {
            System.out.println(post);
        }
    }
}
