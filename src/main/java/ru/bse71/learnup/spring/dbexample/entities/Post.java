package ru.bse71.learnup.spring.dbexample.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Created by bse71
 * Date: 18.08.2021
 * Time: 23:41
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "posts")
@Cacheable
@org.hibernate.annotations.Cache(include = "non-lazy", region = "post.id", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String title;

    @Column
    private String text;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public Post(Integer id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(
                String.format("Post id: %d\n%s\n%s\n", id, title, text));

        if (comments != null) {
            for (Comment comment : comments) {
                sb
                        .append(
                                String.format("\t%s\n", comment.getText()));
            }
        }

        return sb.toString();
    }
}
