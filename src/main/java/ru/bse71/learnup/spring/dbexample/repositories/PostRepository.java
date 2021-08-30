package ru.bse71.learnup.spring.dbexample.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import ru.bse71.learnup.spring.dbexample.entities.Post;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * Created by bse71
 * Date: 23.08.2021
 * Time: 1:26
 */

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> getAllByTitleContains(String titleContains);

    List<Post> getAllByTextNotNull();

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT p FROM Post p WHERE p.text = :oldText")
    List<Post> getForModifyTexts(String oldText);

    @Lock(LockModeType.READ)
    @Query("SELECT p FROM Post p WHERE p.likesCount BETWEEN 30 AND 50")
    List<Post> getMiddleLikesPosts();

    @Lock(LockModeType.READ)
    @Query("SELECT p FROM Post p WHERE p.likesCount > :border")
    List<Post> getPopularPosts(Integer border);

    @Query(name = "Post.getPopular")
    List<Post> getNamedPopularPosts();
}
