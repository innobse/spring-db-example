package ru.bse71.learnup.spring.dbexample.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.bse71.learnup.spring.dbexample.dao.exceptions.MyDbBaseException;
import ru.bse71.learnup.spring.dbexample.dao.interfaces.PostDao;
import ru.bse71.learnup.spring.dbexample.entities.Comment;
import ru.bse71.learnup.spring.dbexample.entities.Post;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bse71
 * Date: 21.08.2021
 * Time: 23:48
 */

public class PostDaoJdbcTemplate implements PostDao {

    private final JdbcTemplate jdbcTemplate;

    public PostDaoJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Post getPostById(Integer id) {
        final SqlRowSet resultSet = jdbcTemplate.queryForRowSet("SELECT * FROM posts WHERE id = ?;", id);
        resultSet.next();
        return parsePostFromResultSet(resultSet);
    }

    @Override
    public List<Post> getAllPosts() {
        final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM posts;");
        List<Post> posts = new ArrayList<>();
        while (sqlRowSet.next()) {
            posts.add(
                    parsePostFromResultSet(sqlRowSet));
        }
        return posts;
    }

    @Override
    public Post addPost(Post post) {
        Object[] params = {post.getTitle(), post.getText()};
        if (
                jdbcTemplate.update(
                        "INSERT INTO posts(id, title, text) VALUES (nextval('hibernate_sequence'), ?, ?);", params) == 1L
        ) {
            final SqlRowSet resultSet = jdbcTemplate.queryForRowSet("SELECT * FROM posts WHERE title = ?;", post.getTitle());
            resultSet.next();
            final Post newPost = parsePostFromResultSet(resultSet);

            for (Comment comment : post.getComments()) {
                addComment(comment, newPost.getId());
            }

            return getPostById(newPost.getId());
        } else return null;
    }

    @Override
    public Post updatePost(Post post) {

        final int updateRows = jdbcTemplate.update(
                "UPDATE INTO posts(id, title, text) VALUES (?, ?, ?);",
                post.getId(),
                post.getTitle(),
                post.getText());

        if (updateRows == 1) {
            for (Comment comment : post.getComments()) {
                updateComment(comment, post.getId());
            }
            return getPostById(post.getId());
        } else return null;
    }

    @Override
    public boolean deletePostById(Integer id) {
        return jdbcTemplate.update("DELETE FROM posts WHERE id = ?;", id) == 1;
    }

    public void addComment(Comment comment, Integer postId) {

        jdbcTemplate.update(
                "INSERT INTO comments(id, post_id, text) VALUES (nextval('hibernate_sequence'), ?, ?);",
                postId,
                comment.getText());
    }

    public void updateComment(Comment comment, Integer postId) {

        jdbcTemplate.update(
                "UPDATE INTO comments(id, post_id, text) VALUES (?, ?, ?);",
                comment.getId(),
                postId,
                comment.getText());
    }

    private Post parsePostFromResultSet(SqlRowSet resultSet) {

        final int id = resultSet.getInt("id");
        final String title = resultSet.getString("title");
        final String text = resultSet.getString("text");

        final SqlRowSet commentsResultSet =
                jdbcTemplate.queryForRowSet("SELECT * FROM comments WHERE post_id = ?;", id);

        final Post post = new Post(id, title, text);
        final List<Comment> comments = parseCommentsFromResultSet(commentsResultSet, post);
        post.setComments(comments);

        return post;
    }

    private List<Comment> parseCommentsFromResultSet(SqlRowSet resultSet, Post post) {
        List<Comment> comments = new ArrayList<>();
        while (resultSet.next()) {
            final int id = resultSet.getInt("id");
            final String text = resultSet.getString("text");

            comments.add(
                    new Comment(id, text, post));
        }

        return comments;
    }
}
