package ru.bse71.learnup.spring.dbexample.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.bse71.learnup.spring.dbexample.dao.interfaces.PostDao;
import ru.bse71.learnup.spring.dbexample.entities.Post;

import java.util.List;

/**
 * Created by bse71
 * Date: 21.08.2021
 * Time: 23:48
 */

public class PostDaoHibernate implements PostDao {

    private final SessionFactory sessionFactory;

    public PostDaoHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Post getPostById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Post.class, id);
        }
    }

    @Override
    public List<Post> getAllPosts() {
        try (Session session = sessionFactory.openSession()) {
            final Query<Post> query = session.createQuery("from Post", Post.class);
            return query.getResultList();
        }
    }

    @Override
    public Post addPost(Post post) {
        try (Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            final Integer id = (Integer) session.save(post);
            transaction.commit();
            return getPostById(id);
        }
    }

    @Override
    public Post updatePost(Post post) {
        try (Session session = sessionFactory.openSession()) {
            return (Post) session.merge(post);
        }
    }

    @Override
    public boolean deletePostById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            final Post loaded = session.load(Post.class, id);
            session.delete(loaded);
            session.getTransaction().commit();
            return true;
        }
    }
}
