package ru.bse71.learnup.spring.dbexample.services;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import ru.bse71.learnup.spring.dbexample.dao.PostDaoDataJpa;
import ru.bse71.learnup.spring.dbexample.dao.interfaces.PostDao;
import ru.bse71.learnup.spring.dbexample.entities.Comment;
import ru.bse71.learnup.spring.dbexample.entities.Post;

import javax.transaction.Transactional;
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

    public DemoService(PostDao postDao) {
        this.postDao = postDao;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.ctx = ctx;
    }

    public void demo() {

        Post newPost = new Post(null, "Новый пост", "Lorem ipsum...");
        newPost.setComments(
                Collections.singletonList(
                        new Comment(null, "Comment text", newPost)));

        newPost = postDao.addPost(newPost);

        System.out.println(newPost);

        postDao.deletePostById(newPost.getId());

        printPosts(postDao.getAllPosts());

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

    public void demoCache() {
        for (int i = 0; i < 3; i++) {
            postDao.getPostById(1);
            System.out.println("==================================");
        }
    }

    public void demoCacheMethod() {
        final String testTitle = "test";
        PostDaoDataJpa repo = (PostDaoDataJpa) postDao;
        repo.addPost(
                new Post(null, testTitle, testTitle));
        for (int i = 0; i < 3; i++) {
            repo.getPostByTitle(testTitle);
            System.out.println("==================================");
        }
        repo.deletePostByTitle(testTitle);
    }
}
