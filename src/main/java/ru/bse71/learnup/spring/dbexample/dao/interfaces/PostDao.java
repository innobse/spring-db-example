package ru.bse71.learnup.spring.dbexample.dao.interfaces;

import ru.bse71.learnup.spring.dbexample.entities.Post;

import java.util.List;

/**
 * Created by bse71
 * Date: 21.08.2021
 * Time: 23:48
 */

public interface PostDao {

    Post getPostById(Integer id);
    List<Post> getAllPosts();
    Post addPost(Post post);
    Post updatePost(Post post);
    boolean deletePostById(Integer id);
}
