package ru.bse71.learnup.spring.dbexample.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.bse71.learnup.spring.dbexample.dao.interfaces.PostDao;
import ru.bse71.learnup.spring.dbexample.entities.Comment;
import ru.bse71.learnup.spring.dbexample.entities.Post;

import java.util.*;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

/**
 * Created by bse71
 * Date: 21.08.2021
 * Time: 23:48
 */

public class PostDaoNamedJdbcTemplate implements PostDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PostDaoNamedJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Post getPostById(Integer id) {
        final Post post = jdbcTemplate.queryForObject(
                "SELECT * FROM posts WHERE id = :id;",
                singletonMap("id", id),
                new BeanPropertyRowMapper<>(Post.class));

        post.setComments(
                parseCommentsFromResultSet(
                        jdbcTemplate.queryForRowSet(
                                "SELECT * FROM comments WHERE post_id = :postId;",
                                singletonMap("postId", post.getId())), post));
        return post;
    }

    @Override
    public List<Post> getAllPosts() {
        return parsePostsFromResultSet(
                jdbcTemplate.queryForRowSet("SELECT * FROM posts;", emptyMap()));
    }

    @Override
    public Post addPost(Post post) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", post.getTitle());
        params.put("text", post.getText());
        if (
                jdbcTemplate.update(
                        "INSERT INTO posts(id, title, text) VALUES (nextval('hibernate_sequence'), :title, :text);", params) == 1L
        ) {
            final Post newPost =
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM posts WHERE title = :title;",
                            singletonMap("title", post.getTitle()),
                            new BeanPropertyRowMapper<>(Post.class));

            for (Comment comment : post.getComments()) {
                addComment(comment, newPost.getId());
            }

            return getPostById(newPost.getId());
        } else return null;
    }

    @Override
    public Post updatePost(Post post) {

        Map<String, Object> params = new HashMap<>();
        params.put("id", post.getId());
        params.put("title", post.getTitle());
        params.put("text", post.getText());

        final int updateRows = jdbcTemplate.update(
                "UPDATE INTO posts(id, title, text) VALUES (:id, :title, :text);",
                params);

        if (updateRows == 1) {
            for (Comment comment : post.getComments()) {
                updateComment(comment, post.getId());
            }
            return getPostById(post.getId());
        } else return null;
    }

    @Override
    public boolean deletePostById(Integer id) {
        return jdbcTemplate.update("DELETE FROM posts WHERE id = :id;", singletonMap("id", id)) == 1;
    }

    public void addComment(Comment comment, Integer postId) {

        Map<String, Object> params = new HashMap<>();
        params.put("postId", postId);
        params.put("text", comment.getText());

        jdbcTemplate.update(
                "INSERT INTO comments(id, post_id, text) VALUES (nextval('hibernate_sequence'), :postId, :text);", params);
    }

    public void updateComment(Comment comment, Integer postId) {

        Map<String, Object> params = new HashMap<>();
        params.put("id", comment.getId());
        params.put("postId", postId);
        params.put("text", comment.getText());

        jdbcTemplate.update(
                "UPDATE INTO comments(id, post_id, text) VALUES (:id, :postId, :text);", params);
    }

    private List<Post> parsePostsFromResultSet(SqlRowSet resultSet) {

        List<Post> posts = new ArrayList<>();

        while (resultSet.next()) {
            final int id = resultSet.getInt("id");
            final String title = resultSet.getString("title");
            final String text = resultSet.getString("text");

            final SqlRowSet commentsResultSet =
                    jdbcTemplate.queryForRowSet(
                            "SELECT * FROM comments WHERE post_id = :id;",
                            singletonMap("id", id));

            final Post post = new Post(id, title, text);
            final List<Comment> comments = parseCommentsFromResultSet(commentsResultSet, post);
            post.setComments(comments);

            posts.add(post);
        }

        return posts;
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
