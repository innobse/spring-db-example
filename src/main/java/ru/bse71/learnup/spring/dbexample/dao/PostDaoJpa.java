package ru.bse71.learnup.spring.dbexample.dao;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.bse71.learnup.spring.dbexample.dao.interfaces.PostDao;
import ru.bse71.learnup.spring.dbexample.entities.Post;

import javax.persistence.EntityManager;
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
public class PostDaoJpa implements PostDao {

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
        return entityManager.merge(post);
    }

    @Override
    @Transactional
    public boolean deletePostById(Integer id) {
        entityManager.remove(
                entityManager.find(Post.class, id));
        return true;
    }
}
