package ru.bse71.learnup.spring.dbexample.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("SELECT p FROM Post p WHERE p.likesCount BETWEEN 30 AND 50")
    List<Post> getMiddleLikesPosts();

    @Query("SELECT p FROM Post p WHERE p.likesCount > :border")
    List<Post> getPopularPosts(Integer border);

    @Query(name = "Post.getPopular")
    List<Post> getNamedPopularPosts();
}
