package ru.bse71.learnup.spring.dbexample.dao;

import ru.bse71.learnup.spring.dbexample.dao.exceptions.MyDbBaseException;
import ru.bse71.learnup.spring.dbexample.dao.interfaces.PostDao;
import ru.bse71.learnup.spring.dbexample.entities.Comment;
import ru.bse71.learnup.spring.dbexample.entities.Post;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bse71
 * Date: 21.08.2021
 * Time: 23:48
 */

public class PostDaoJdbc implements PostDao {

    private Connection connection;

    private String dbUrl;
    private String user;
    private String pass;

    public PostDaoJdbc(String dbUrl, String user, String pass) {
        this.dbUrl = dbUrl;
        this.user = user;
        this.pass = pass;
    }

    @PostConstruct
    public void connectToDb() throws SQLException {
        this.connection = DriverManager.getConnection(dbUrl, user, pass);
    }

    @Override
    public Post getPostById(Integer id) {
        try {
            final PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM posts WHERE id = ?;");
            preparedStatement.setInt(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return parsePostFromResultSet(resultSet);
        } catch (SQLException err) {
            throw new MyDbBaseException("получении поста по id", err);
        }
    }

    @Override
    public List<Post> getAllPosts() {
        try {
            final Statement statement = connection.createStatement();

            final ResultSet resultSet = statement.executeQuery("SELECT * FROM posts;");
            List<Post> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(
                        parsePostFromResultSet(resultSet));
            }
            return result;
        } catch (SQLException err) {
            throw new MyDbBaseException("получении всех постов", err);
        }
    }

    @Override
    public Post addPost(Post post) {
        try {
            final PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO posts(id, title, text) VALUES (nextval('hibernate_sequence'), ?, ?);");

            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getText());

            if (preparedStatement.executeUpdate() == 1L) {
                final PreparedStatement selectStatement =
                        connection.prepareStatement("SELECT * FROM posts WHERE title = ?;");
                selectStatement.setString(1, post.getTitle());
                final ResultSet resultSet = selectStatement.executeQuery();
                resultSet.next();
                final int newId = resultSet.getInt("id");

                for (Comment comment : post.getComments()) {
                    addComment(comment, newId);
                }

                post.setId(newId);
                return post;
            } else {
                return null;
            }

        } catch (SQLException err) {
            throw new MyDbBaseException("добавлении поста", err);
        }
    }

    @Override
    public Post updatePost(Post post) {
        try {
            final PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE posts SET title = ?, text = ? WHERE id = ?;");

            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getText());
            preparedStatement.setInt(3, post.getId());

            if (preparedStatement.executeUpdate() == 1L) {
                for (Comment comment : post.getComments()) {
                    updateComment(comment, post.getId());
                }
                return getPostById(post.getId());
            } else {
                return null;
            }

        } catch (SQLException err) {
            throw new MyDbBaseException("обновлении поста", err);
        }
    }

    @Override
    public boolean deletePostById(Integer id) {
        try {
            final PreparedStatement preparedStatement =
                    connection.prepareStatement("DELETE FROM posts WHERE id = ?;");

            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() == 1L;

        } catch (SQLException err) {
            throw new MyDbBaseException("удалении поста", err);
        }
    }

    public void addComment(Comment comment, Integer postId) throws SQLException {
        final PreparedStatement preparedStatement =
                connection.prepareStatement(
                        "INSERT INTO comments(id, post_id, text) VALUES (nextval('hibernate_sequence'), ?, ?);");

        preparedStatement.setInt(1, postId);
        preparedStatement.setString(2, comment.getText());
        preparedStatement.executeUpdate();
    }

    public void updateComment(Comment comment, Integer postId) throws SQLException {
        final PreparedStatement preparedStatement =
                connection.prepareStatement(
                        "UPDATE comments SET post_id = ?, text = ? WHERE id = ?;");

        preparedStatement.setInt(3, comment.getId());
        preparedStatement.setInt(1, postId);
        preparedStatement.setString(2, comment.getText());
        preparedStatement.executeUpdate();
    }

    private Post parsePostFromResultSet(ResultSet resultSet) throws SQLException {

        final PreparedStatement preparedStatement =
                connection.prepareStatement("SELECT * FROM comments WHERE post_id = ?;");

        final int id = resultSet.getInt("id");
        final String title = resultSet.getString("title");
        final String text = resultSet.getString("text");

        preparedStatement.setInt(1, id);
        final ResultSet commentsResultSet = preparedStatement.executeQuery();

        final Post post = new Post(id, title, text);
        final List<Comment> comments = parseCommentsFromResultSet(commentsResultSet, post);
        post.setComments(comments);

        return post;
    }

    private List<Comment> parseCommentsFromResultSet(ResultSet resultSet, Post post) throws SQLException {
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
