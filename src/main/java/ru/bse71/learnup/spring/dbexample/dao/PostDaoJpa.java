package ru.bse71.learnup.spring.dbexample.dao;

import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ru.bse71.learnup.spring.dbexample.dao.interfaces.PostDao;
import ru.bse71.learnup.spring.dbexample.entities.Post;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by bse71
 * Date: 21.08.2021
 * Time: 23:48
 */

@Repository
@Profile("jpa")
@Scope("prototype")
public class PostDaoJpa implements PostDao {

    private static int DEFAULT_TIMEOUT = 5000;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Post getPostById(Integer id) {
        return entityManager.find(Post.class, id);
    }

    @Override
    public List<Post> getAllPosts() {
        final TypedQuery<Post> query = entityManager.createQuery("from Post", Post.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Post addPost(Post post) {
        entityManager.persist(post);
        return post;
    }

    @Override
    @Transactional
    public Post updatePost(Post post) {
        final Post updated = entityManager.find(Post.class, post.getId());
        entityManager.lock(updated, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        System.out.println("Блокировка " + post.getId());

        try {
            Thread.sleep(DEFAULT_TIMEOUT);
        } catch (InterruptedException e) {}

        updated.setTitle(post.getTitle());
        updated.setText(post.getText());
        updated.setComments(post.getComments());
        return entityManager.merge(updated);
    }

    @Override
    @Transactional
    public boolean deletePostById(Integer id) {
        final Post post = entityManager.find(Post.class, id);
        entityManager.lock(post, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        System.out.println("Блокировка для удаления " + post.getId());

        try {
            Thread.sleep(DEFAULT_TIMEOUT);
        } catch (InterruptedException e) {}

        entityManager.remove(
                post);
        return true;
    }
}
