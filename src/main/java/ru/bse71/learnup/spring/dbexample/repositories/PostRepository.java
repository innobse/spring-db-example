package ru.bse71.learnup.spring.dbexample.repositories;

import javafx.geometry.Pos;
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
}
