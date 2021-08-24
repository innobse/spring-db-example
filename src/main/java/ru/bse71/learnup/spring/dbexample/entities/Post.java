package ru.bse71.learnup.spring.dbexample.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@NamedQueries({
        @NamedQuery(name = "Post.getPopular", query = "SELECT p FROM Post p WHERE p.likesCount > 50"),
        @NamedQuery(name = "Post.getMiddle", query = "SELECT p FROM Post p WHERE p.likesCount BETWEEN 10 AND 50")
})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String title;

    @Column
    private String text;

    @Column(name = "likes")
    private Integer likesCount;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public Post(Integer id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
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
                String.format("Post id: %d\n%s\n%s\nLIKE:%d\n", id, title, text, likesCount));

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
