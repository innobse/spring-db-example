package ru.bse71.learnup.spring.dbexample.dao;

import ru.bse71.learnup.spring.dbexample.dao.interfaces.PostDao;
import ru.bse71.learnup.spring.dbexample.entities.Post;
import ru.bse71.learnup.spring.dbexample.repositories.CommentRepository;
import ru.bse71.learnup.spring.dbexample.repositories.PostRepository;

import java.util.List;

/**
 * Created by bse71
 * Date: 21.08.2021
 * Time: 23:48
 */

public class PostDaoDataJpa implements PostDao {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostDaoDataJpa(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Post getPostById(Integer id) {
        return postRepository.getOne(id);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post addPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Post post) {
        return addPost(post);
    }

    @Override
    public boolean deletePostById(Integer id) {
        postRepository.deleteById(id);
        return true;
    }

    public List<Post> getAllPostsWithText() {
        return postRepository.getAllByTextNotNull();
    }

    public List<Post> getAllPostsWithTitleContains(String template) {
        return postRepository.getAllByTitleContains(template);
    }
}
