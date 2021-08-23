package ru.bse71.learnup.spring.dbexample.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bse71.learnup.spring.dbexample.entities.Comment;
import ru.bse71.learnup.spring.dbexample.entities.Post;

import java.util.List;

/**
 * Created by bse71
 * Date: 23.08.2021
 * Time: 1:26
 */

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
