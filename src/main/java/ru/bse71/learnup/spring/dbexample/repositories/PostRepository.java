package ru.bse71.learnup.spring.dbexample.repositories;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.bse71.learnup.spring.dbexample.entities.Post;

import java.util.List;

/**
 * Created by bse71
 * Date: 23.08.2021
 * Time: 1:26
 */

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> getAllByTitleContains(String titleContains);

    List<Post> getAllByTextNotNull();

    Post getPostByTitle(String title);

    void deleteByTitle(String title);
}
